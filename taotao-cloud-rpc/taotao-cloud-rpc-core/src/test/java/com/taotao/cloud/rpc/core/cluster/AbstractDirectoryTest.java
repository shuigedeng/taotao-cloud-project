package com.taotao.cloud.rpc.core.cluster;

import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invocation;
import com.taotao.cloud.rpc.common.invoke.Invoker;
import com.taotao.cloud.rpc.common.invoke.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link AbstractDirectory} 的目录语义：可调用列表、销毁、路由链。
 * <p>
 * 用一个小而全的具体子类驱动，不去碰注册中心 —— 这一层的契约与发现机制无关。
 */
class AbstractDirectoryTest {

    /** 读一次 protected 的 invokers 字段，同时让 doList 走真实实现。 */
    static final class Dir extends AbstractDirectory<String> {
        Dir(URL url) {
            super(String.class, url);
        }

        Dir(URL url, URL consumerUrl) {
            super(String.class, url, consumerUrl);
        }

        @Override
        protected List<Invoker<String>> doList() {
            return invokers;
        }

        Map<String, String> config() {
            return queryMap;
        }

        List<Router> routerChain() {
            return routers;
        }
    }

    static class Noop implements Invoker<String> {
        final String id;
        boolean destroyed;
        boolean available = true;

        Noop(String id) {
            this.id = id;
        }

        @Override
        public Class<String> getInterface() {
            return String.class;
        }

        @Override
        public Result invoke(Invocation invocation) {
            return Result.success(id);
        }

        @Override
        public URL getUrl() {
            return new URL("ttc-rpc", "127.0.0.1", 1);
        }

        @Override
        public boolean isAvailable() {
            return available;
        }

        @Override
        public void destroy() {
            destroyed = true;
        }

        @Override
        public String toString() {
            return "invoker-" + id;
        }
    }

    /** 抛异常的 destroy，用来验证"一个坏 invoker 不能中断销毁循环"。 */
    static final class ExplodingNoop extends Noop {
        ExplodingNoop(String id) {
            super(id);
        }

        @Override
        public void destroy() {
            destroyed = true;
            throw new IllegalStateException("boom-" + id);
        }
    }

    static URL url() {
        URL u = new URL("consumer", "127.0.0.1", 0);
        u.setServiceInterface("com.zifang.demo.Demo");
        u.setVersion("9.9.9");
        return u;
    }

    /** 记录调用顺序的 Router；返回 null 用来验证短路分支。 */
    static final class RecordingRouter implements Router {
        final String name;
        final int priority;
        final List<Invoker<String>> returns;
        final List<String> log;
        final List<Invocation> seenInvocations = new ArrayList<>();

        RecordingRouter(String name, int priority, List<Invoker<String>> returns, List<String> log) {
            this.name = name;
            this.priority = priority;
            this.returns = returns;
            this.log = log;
        }

        @Override
        public URL getUrl() {
            return url();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> List<Invoker<T>> route(List<Invoker<T>> invokers, URL url, Invocation invocation) {
            log.add(name);
            seenInvocations.add(invocation);
            return (List<Invoker<T>>) (List<?>) returns;
        }

        @Override
        public int getPriority() {
            return priority;
        }
    }

    @Test
    @DisplayName("两参构造把同一个 URL 同时当作目录 URL 与消费者 URL")
    void twoArgCtorAliasesUrls() {
        URL u = url();
        Dir d = new Dir(u);
        assertSame(u, d.getUrl());
        assertSame(u, d.getConsumerUrl());
        assertEquals(String.class, d.getInterface());
        assertFalse(d.isDestroyed());
    }

    @Test
    @DisplayName("三参构造允许两个 URL 不同")
    void threeArgCtorKeepsBothUrls() {
        URL provider = url();
        URL consumer = new URL("consumer", "10.0.0.9", 0);
        Dir d = new Dir(provider, consumer);
        assertSame(provider, d.getUrl());
        assertSame(consumer, d.getConsumerUrl());
    }

    @Test
    @DisplayName("bug_queryMapIsOneShotSnapshot：构造之后再往 URL 加参数，目录永远看不见")
    void bug_queryMapIsOneShotSnapshot() {
        URL u = url();
        u.addParameter("before", "1");
        Dir d = new Dir(u);
        assertEquals("1", d.config().get("before"));
        u.addParameter("after", "2");
        assertNull(u.getParameter("nope"));
        assertFalse(d.config().containsKey("after"), "快照语义：URL 后来的变化不进 queryMap");
    }

    @Test
    @DisplayName("list() 就是 doList()，销毁后一律空表")
    void listDelegatesToDoListThenShutsDown() {
        Dir d = new Dir(url());
        Noop a = new Noop("a");
        d.addInvoker(a);
        assertEquals(1, d.list().size());
        assertSame(a, d.list().get(0));

        d.destroy();
        assertTrue(d.isDestroyed());
        assertTrue(d.list().isEmpty(), "已销毁的目录不得再交出 invoker");
        assertTrue(a.destroyed, "destroy() 必须逐个销毁 invoker");
    }

    @Test
    @DisplayName("destroy() 幂等：第二次不再触碰已清空的表")
    void destroyIsIdempotent() {
        Dir d = new Dir(url());
        Noop a = new Noop("a");
        d.addInvoker(a);
        d.destroy();
        d.destroy();
        assertTrue(d.isDestroyed());
        assertTrue(a.destroyed);
    }

    @Test
    @DisplayName("一个 invoker 的 destroy 抛异常，不得让后面的 invoker 逃过销毁")
    void destroyLoopSurvivesThrowingInvoker() {
        Dir d = new Dir(url());
        ExplodingNoop bad = new ExplodingNoop("bad");
        Noop good = new Noop("good");
        d.addInvoker(bad);
        d.addInvoker(good);

        d.destroy();
        assertTrue(bad.destroyed);
        assertTrue(good.destroyed, "异常把循环打断的话，good 的远端连接就泄漏了");
    }

    @Test
    @DisplayName("setInvokers 拷贝一份并拒绝 null 参数；addInvoker 去重并拒绝 null")
    void invokerMutatorsAreDefensive() {
        Dir d = new Dir(url());
        Noop a = new Noop("a");

        d.setInvokers(null);
        assertTrue(d.list().isEmpty());

        d.addInvoker(a);
        d.addInvoker(a);
        d.addInvoker(null);
        assertEquals(1, d.list().size(), "同一实例不得重复、null 不得进表: " + d.list());
        d.removeInvoker(a);
        assertTrue(d.list().isEmpty());
        d.removeInvoker(new Noop("other"));
        assertTrue(d.list().isEmpty());
    }

    @Test
    @DisplayName("bug_setInvokersLetsNullElementsThrough：addInvoker 挡 null，setInvokers 不挡")
    void bug_setInvokersLetsNullElementsThrough() {
        Dir d = new Dir(url());
        Noop a = new Noop("a");
        d.setInvokers(Arrays.asList(a, null));
        assertEquals(2, d.list().size(), "同一个 null 参数在 addInvoker 那里是被显式挡掉的");
        assertNull(d.list().get(1));

        // 猎物：换一个把 null 挡在外面的入口，同一份数据就只有 1 条
        Dir other = new Dir(url());
        other.setInvokers(Collections.emptyList());
        other.addInvoker(a);
        other.addInvoker(null);
        assertEquals(1, other.list().size());
    }

    @Test
    @DisplayName("setInvokers 传进来的列表被复制，外部继续改不影响目录")
    void setInvokersCopiesTheList() {
        Dir d = new Dir(url());
        List<Invoker<String>> src = new ArrayList<>();
        src.add(new Noop("a"));
        d.setInvokers(src);
        src.add(new Noop("b"));
        assertEquals(1, d.list().size(), "实际: " + d.list());
    }

    @Test
    @DisplayName("route() 按加入顺序执行，getPriority() 从不参与排序")
    void bug_routerPriorityIsNeverApplied() {
        Dir d = new Dir(url());
        List<String> order = new ArrayList<>();
        // 两个 Router 都必须交回非空表：空表会让 route() 短路，第二个 Router 根本轮不到
        List<Invoker<String>> pass = Collections.singletonList(new Noop("x"));

        RecordingRouter high = new RecordingRouter("smallNumber", 1, pass, order);
        RecordingRouter low = new RecordingRouter("largeNumber", 100, pass, order);
        // 先加优先级数字大的：若按"数字越小优先级越高"排序，smallNumber 必须先跑
        d.addRouter(low);
        d.addRouter(high);
        assertEquals(2, d.routerChain().size());

        List<Invoker<String>> out = d.route(new ArrayList<>(Collections.singletonList(new Noop("y"))),
                d.getUrl(), null);
        assertEquals(1, out.size(), "两个 Router 都放行，最后一个的表就是结果");
        assertEquals(Arrays.asList("largeNumber", "smallNumber"), order,
                "Router.getPriority()  javadoc 写着『数字越小优先级越高』，实际顺序只等于插入顺序");
    }

    @Test
    @DisplayName("任一 Router 交空表就短路，后续 Router 不再执行")
    void routeShortCircuitsOnEmptyResult() {
        Dir d = new Dir(url());
        List<String> order = new ArrayList<>();
        Noop a = new Noop("a");
        d.addRouter(new RecordingRouter("empties", 0, Collections.emptyList(), order));
        d.addRouter(new RecordingRouter("neverRuns", 0, Collections.singletonList(a), order));

        List<Invoker<String>> out = d.route(new ArrayList<>(Collections.singletonList(a)), d.getUrl(), null);
        assertTrue(out.isEmpty());
        assertEquals(Collections.singletonList("empties"), order);
    }

    @Test
    @DisplayName("Router 返回 null 也算空，交给调用方的是不可变空表")
    void routeTreatsNullAsEmpty() {
        Dir d = new Dir(url());
        List<String> order = new ArrayList<>();
        d.addRouter(new RecordingRouter("nully", 0, null, order));
        List<Invoker<String>> out = d.route(new ArrayList<>(Collections.singletonList(new Noop("a"))),
                d.getUrl(), null);
        assertTrue(out.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> out.add(new Noop("b")),
                "短路返回的必须是空表本身");
    }

    @Test
    @DisplayName("空 Router 链时 route() 原样返回同一个列表对象")
    void routeWithoutRoutersIsIdentity() {
        Dir d = new Dir(url());
        List<Invoker<String>> in = new ArrayList<>();
        in.add(new Noop("a"));
        assertSame(in, d.route(in, d.getUrl(), null));
    }

    @Test
    @DisplayName("RegistryDirectory 之外无人传 Invocation：route 收到 null 是常态，Router 不能假设非空")
    void nullInvocationReachesTheRouter() {
        Dir d = new Dir(url());
        List<String> order = new ArrayList<>();
        RecordingRouter r = new RecordingRouter("peek", 0, null, order);
        d.addRouter(r);
        d.route(new ArrayList<>(), d.getUrl(), null);
        assertEquals(1, r.seenInvocations.size());
        assertNull(r.seenInvocations.get(0), "AbstractDirectory.route 不做 null 保护，Router 作者必须自己判");
    }

    @Test
    @DisplayName("bug_urlParameterWithNullValueMakesTheDirectoryThrow：URL 收得下，目录构造不了")
    void bug_nullParameterBricksDirectoryConstruction() {
        URL u = url();
        Map<String, String> raw = new HashMap<>();
        raw.put("k", null);
        u.setParameters(raw);

        assertThrows(NullPointerException.class, () -> new Dir(u),
                "URL.getParameters() 允许 null 值（HashMap），queryMap 是 ConcurrentHashMap");

        // 猎物：同一个 URL 自己完全可用，问题只在构造目录这一步
        assertNull(u.getParameter("k"));
        assertEquals("v", new Dir(new URL("consumer", "h", 1)).getUrl().getParameter("k", "v"));
    }

    @Test
    @DisplayName("bug_urlParametersEscapeByReference：getParameters() 交出活映射，外面能把 URL 改脏")
    void bug_urlParametersEscapeByReference() {
        URL u = url();
        u.addParameter("side", "consumer");
        Map<String, String> leaked = u.getParameters();
        leaked.put("injected", "1");
        assertTrue(u.getParameters().containsKey("injected"),
                "这是 com.taotao.cloud.rpc.common.URL 的暴露面，目录只是它的受害者之一");
    }
}
