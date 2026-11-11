package com.taotao.cloud.rpc.registry;

import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.common.invoke.Invocation;
import com.taotao.cloud.rpc.common.invoke.Invoker;
import com.taotao.cloud.rpc.common.invoke.Result;
import com.taotao.cloud.rpc.common.invoke.RpcInvocation;
import com.taotao.cloud.rpc.core.cluster.FailoverCluster;
import com.taotao.cloud.rpc.core.net.RpcServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link RegistryDirectory}：注册中心推送 → 可调用列表 → 真 socket 调用。
 * <p>
 * 目录里的 invoker 会真建连接，所以每个用例都起一台本地 {@link RpcServer}；
 * 需要第二台时（验证"地址才是键"）再临时起一台。
 */
class RegistryDirectoryTest {

	interface Echo {
		String say(String what);
	}

	static final class Plain implements Echo {
		@Override
		public String say(String what) {
			return "plain:" + what;
		}
	}

	static final class V5 implements Echo {
		@Override
		public String say(String what) {
			return "v5:" + what;
		}
	}

	/**
	 * 只记录调用、可按需返回固定 provider 列表的注册中心。
	 */
	static class FakeRegistry implements RegistryService {
		final AtomicInteger subscribeCalls = new AtomicInteger();
		final AtomicInteger lookupCalls = new AtomicInteger();
		final AtomicInteger destroyCalls = new AtomicInteger();
		List<URL> answer = Collections.emptyList();
		RuntimeException throwOnSubscribe;

		@Override
		public void register(URL url) {
		}

		@Override
		public void unregister(URL url) {
		}

		@Override
		public void subscribe(URL url, NotifyListener listener) {
			if (throwOnSubscribe != null) {
				throw throwOnSubscribe;
			}
			subscribeCalls.incrementAndGet();
			listener.notify(answer);
		}

		@Override
		public void unsubscribe(URL url, NotifyListener listener) {
		}

		@Override
		public List<URL> lookup(URL url) {
			lookupCalls.incrementAndGet();
			return answer;
		}

		@Override
		public void destroy() {
			destroyCalls.incrementAndGet();
		}
	}

	private RpcServer server;
	private final List<RpcServer> extraServers = new ArrayList<>();
	private int port;
	private URL consumer;

	private static int freePort() throws IOException {
		try (ServerSocket s = new ServerSocket(0)) {
			return s.getLocalPort();
		}
	}

	private URL providerUrl(int atPort, String version) {
		URL u = new URL("ttc-rpc", "127.0.0.1", atPort);
		u.setServiceInterface(Echo.class.getName());
		u.setVersion(version);
		return u;
	}

	private URL live(int atPort) {
		return providerUrl(atPort, null);
	}

	/**
	 * 再起一台真在听的 server，返回它的端口。绑 0 再读回真实端口 —— 探针式取号在"关掉到真正 bind"之间有抢端口竞态。
	 */
	private int anotherListeningPort() throws Exception {
		RpcServer s = new RpcServer("127.0.0.1", 0);
		s.registerService(Echo.class, new Plain());
		s.start(true);
		extraServers.add(s);
		return s.getPort();
	}

	@BeforeEach
	void startServer() throws Exception {
		server = new RpcServer("127.0.0.1", 0);
		server.registerService(Echo.class, new Plain());
		server.start(true);
		port = server.getPort();
		consumer = new URL("consumer", "127.0.0.1", 0);
		consumer.setServiceInterface(Echo.class.getName());
	}

	@AfterEach
	void stopServer() {
		for (RpcServer s : extraServers) {
			s.stop();
		}
		extraServers.clear();
		if (server != null) {
			server.stop();
		}
	}

	private static Invocation call() {
		return new RpcInvocation(Echo.class.getName(), "say",
			new Class<?>[]{String.class}, new Object[]{"bob"});
	}

	// ---------- 订阅 ----------

	@Test
	@DisplayName("构造即订阅，并把当前 provider 一次性拉齐")
	void constructionSubscribesAndPulls() {
		FakeRegistry reg = new FakeRegistry();
		reg.answer = Collections.singletonList(live(port));

		RegistryDirectory<Echo> dir = new RegistryDirectory<>(Echo.class, consumer, reg);
		assertEquals(1, reg.subscribeCalls.get());
		assertEquals(1, dir.list().size(), "实际: " + dir.list());
		assertTrue(dir.list().get(0).isAvailable(), "刚建好的 invoker 应当连着活着的 server");
	}

	@Test
	@DisplayName("订阅用的 URL 只带身份，不带地址")
	void subscribeUrlCarriesOnlyIdentity() {
		List<URL> seen = new ArrayList<>();
		FakeRegistry reg = new FakeRegistry() {
			@Override
			public void subscribe(URL url, NotifyListener listener) {
				seen.add(url);
				listener.notify(Collections.emptyList());
			}
		};
		new RegistryDirectory<>(Echo.class, consumer, reg);
		assertEquals(1, seen.size());
		URL sub = seen.get(0);
		assertEquals(Echo.class.getName(), sub.getServiceInterface());
		assertNull(sub.getHost(), "订阅 URL 是 new URL() 只填了接口/组/版本");
		assertEquals(0, sub.getPort());
		assertFalse(sub.getAddress().equals(consumer.getAddress()));
	}

	@Test
	@DisplayName("bug_subscribeFailureIsOnlyLogged：注册中心抛异常，目录照样『健康』地空着")
	void bug_subscribeFailureIsSwallowed() {
		FakeRegistry reg = new FakeRegistry();
		reg.throwOnSubscribe = new RuntimeException("registry down");

		RegistryDirectory<Echo> dir = new RegistryDirectory<>(Echo.class, consumer, reg);
		assertTrue(dir.list().isEmpty());
		assertFalse(dir.isDestroyed());
		assertEquals(0, reg.subscribeCalls.get(),
			"计数没加上，说明异常发生在订阅那一步并被 subscribe() 的 catch 整个吞掉");

		// 猎物：异常本身是照原样往上抛的，吞它的是 RegistryDirectory 而不是注册中心
		RuntimeException e = assertThrows(RuntimeException.class,
			() -> reg.subscribe(consumer, urls -> {
			}));
		assertEquals("registry down", e.getMessage());
	}

	@Test
	@DisplayName("bug_nullRegistryIsAccepted：传 null 也能构造，直到调用才报『没有 provider』")
	void bug_nullRegistryIsAccepted() {
		RegistryDirectory<Echo> dir = new RegistryDirectory<>(Echo.class, consumer, null);
		assertTrue(dir.list().isEmpty());
		assertFalse(dir.isDestroyed());

		IllegalStateException e = assertThrows(IllegalStateException.class,
			() -> new FailoverCluster().join(dir).invoke(call()));
		assertTrue(e.getMessage().contains("No provider available"), "实际: " + e.getMessage());
	}

	// ---------- 列表来源 ----------

	@Test
	@DisplayName("bug_inheritedInvokerMutatorsAreDeadHere：addInvoker/setInvokers 对 list() 毫无影响")
	void bug_inheritedMutatorsAreIgnoredByDoList() {
		RegistryDirectory<Echo> dir = new RegistryDirectory<>(Echo.class, consumer, new FakeRegistry());
		Invoker<Echo> fake = new Invoker<Echo>() {
			@Override
			public Class<Echo> getInterface() {
				return Echo.class;
			}

			@Override
			public Result invoke(Invocation invocation) {
				return Result.success("local");
			}

			@Override
			public URL getUrl() {
				return consumer;
			}

			@Override
			public boolean isAvailable() {
				return true;
			}

			@Override
			public void destroy() {
			}
		};

		dir.addInvoker(fake);
		dir.setInvokers(Collections.singletonList(fake));
		assertTrue(dir.list().isEmpty(),
			"AbstractDirectory 的 invokers 字段被继承下来，但 RegistryDirectory.doList() 只读 urlInvokerMap");

		// 猎物：同一份数据走 notify() 就会出现在 list() 里
		FakeRegistry reg = new FakeRegistry();
		reg.answer = Collections.singletonList(live(port));
		RegistryDirectory<Echo> viaNotify = new RegistryDirectory<>(Echo.class, consumer, reg);
		assertEquals(1, viaNotify.list().size());
	}

	@Test
	@DisplayName("bug_providersAreKeyedByAddressOnly：同地址的两个版本塌成一条，留下谁取决于推送顺序")
	void bug_providersAreKeyedByAddressOnly() {
		FakeRegistry reg = new FakeRegistry();
		reg.answer = Arrays.asList(providerUrl(port, "1.0.0"), providerUrl(port, "2.0.0"));

		RegistryDirectory<Echo> dir = new RegistryDirectory<>(Echo.class, consumer, reg);
		assertEquals(1, dir.list().size(),
			"urlInvokerMap 的键是 getAddress()，版本被完全忽略，留下谁取决于推送顺序");
	}

	@Test
	@DisplayName("地址不同的两条 provider 各建一个连接；连不上的那条静默消失")
	void distinctAddressesAreKeptApart() throws Exception {
		int other = anotherListeningPort();
		FakeRegistry reg = new FakeRegistry();
		reg.answer = Arrays.asList(live(port), live(other));
		RegistryDirectory<Echo> dir = new RegistryDirectory<>(Echo.class, consumer, reg);
		assertEquals(2, dir.list().size(), "两台都活着时确实各一条");

		// 连不上的地址：createInvoker 把异常吞成 null，视图里既没有它也听不见报错
		int nothingListensHere = freePort();
		reg.answer = Arrays.asList(live(port), live(nothingListensHere));
		dir.notify(reg.answer);
		assertEquals(1, dir.list().size(),
			"没人听的地址只是不见了，没有任何地方报告『provider 存在但连不上』");
	}

	@Test
	@DisplayName("bug_emptyPushNeverClearsTheDirectory：注册中心推空表时旧 provider 全部留着")
	void bug_emptyNotificationIsIgnored() {
		FakeRegistry reg = new FakeRegistry();
		reg.answer = Collections.singletonList(live(port));
		RegistryDirectory<Echo> dir = new RegistryDirectory<>(Echo.class, consumer, reg);
		assertEquals(1, dir.list().size());

		dir.notify(Collections.emptyList());
		dir.notify(null);
		assertEquals(1, dir.list().size(),
			"provider 全部下线时注册中心推的是空表，而空表在 notify() 第一行就被 return 掉了");

		// 猎物：点名删除是有效的，缺的只是"空表也要刷"这一条
		dir.onServiceRemoved(live(port));
		assertTrue(dir.list().isEmpty());
	}

	@Test
	@DisplayName("onServiceAdded 走 lookup 重拉全表；onServiceRemoved 按地址销毁连接")
	void addAndRemoveNotifications() {
		FakeRegistry reg = new FakeRegistry();
		RegistryDirectory<Echo> dir = new RegistryDirectory<>(Echo.class, consumer, reg);
		assertTrue(dir.list().isEmpty());

		reg.answer = Collections.singletonList(live(port));
		dir.onServiceAdded(live(port));
		assertEquals(1, dir.list().size());
		assertEquals(1, reg.lookupCalls.get(), "added 事件是『拿事件 URL 去 lookup 全表』实现的");

		Invoker<Echo> liveInvoker = dir.list().get(0);
		assertTrue(liveInvoker.isAvailable());
		dir.onServiceRemoved(live(port));
		assertTrue(dir.list().isEmpty());
		assertFalse(liveInvoker.isAvailable(), "移除时必须把连接一起销毁，否则半开 client 一直占着 fd");
	}

	@Test
	@DisplayName("同一地址重复推送不重建 invoker，版本变了也不重建")
	void refreshIsStableForKnownAddresses() {
		FakeRegistry reg = new FakeRegistry();
		reg.answer = Collections.singletonList(live(port));
		RegistryDirectory<Echo> dir = new RegistryDirectory<>(Echo.class, consumer, reg);
		Invoker<Echo> first = dir.list().get(0);

		dir.notify(Collections.singletonList(providerUrl(port, "9.9.9")));
		assertEquals(1, dir.list().size());
		assertSame(first, dir.list().get(0), "地址没变就不该重建连接 —— 新版本号根本进不了键");
	}

	// ---------- 真调用 ----------

	@Test
	@DisplayName("目录里的 invoker 打的是一次真 socket 往返")
	void invokerTalksOverTheWire() throws Throwable {
		FakeRegistry reg = new FakeRegistry();
		reg.answer = Collections.singletonList(live(port));
		RegistryDirectory<Echo> dir = new RegistryDirectory<>(Echo.class, consumer, reg);

		Result r = dir.list().get(0).invoke(call());
		assertFalse(r.hasException(), "实际: " + r.getException());
		assertEquals("plain:bob", r.getValue());
	}

	@Test
	@DisplayName("bug_providerVersionIsDroppedFromTheRequest：要 5.0.0 拿到的却是默认实现")
	void bug_versionIsDroppedOnTheWayOut() throws Throwable {
		server.register(Echo.class, new V5(), "5.0.0");
		assertTrue(server.getServiceMap().containsKey(Echo.class.getName() + ":5.0.0"),
			"服务端确实按版本挂了两个键: " + server.getServiceMap().keySet());

		FakeRegistry reg = new FakeRegistry();
		reg.answer = Collections.singletonList(providerUrl(port, "5.0.0"));
		RegistryDirectory<Echo> dir = new RegistryDirectory<>(Echo.class, consumer, reg);

		Result r = dir.list().get(0).invoke(call());
		assertFalse(r.hasException(), "实际: " + r.getException());
		assertFalse("v5:bob".equals(r.getValue()),
			"若哪天 DubboInvoker 把 url.getVersion() 写进附件，这一条会先在这里变红");
		assertEquals("plain:bob", r.getValue(),
			"provider URL 上写着 version=5.0.0，DubboInvoker 构造 RpcRequest 时一个字节都没带上");
	}

	@Test
	@DisplayName("bug_oneFailureBricksTheInvokerForever：一次失败之后再也打不通，也不重建")
	void bug_oneFailureBricksTheInvoker() throws Throwable {
		FakeRegistry reg = new FakeRegistry();
		reg.answer = Collections.singletonList(live(port));
		RegistryDirectory<Echo> dir = new RegistryDirectory<>(Echo.class, consumer, reg);
		Invoker<Echo> invoker = dir.list().get(0);
		assertTrue(invoker.isAvailable());

		server.stop();
		// DubboInvoker.invoke 的 catch 块把 sendRequest 的失败原样抛出去，顺手把 available 永久置 false
		assertThrows(Throwable.class, () -> invoker.invoke(call()));
		assertFalse(invoker.isAvailable(), "available 一旦被置 false 就没人再把它翻回来");

		// 服务恢复后，同一个 invoker 仍然打不通；只有新目录/新地址才会重建
		server = new RpcServer("127.0.0.1", port);
		server.registerService(Echo.class, new V5());
		server.start(true);
		dir.notify(Collections.singletonList(providerUrl(port, "1.0.0")));
		assertSame(invoker, dir.list().get(0), "地址还在表里，refreshInvokers 不会重建它");
		assertThrows(Throwable.class, () -> invoker.invoke(call()),
			"服务已经回来了，这个 invoker 却永远躺在『不可用』里");

		// 猎物：砖掉的是这条连接不是服务端 —— 同一个地址换一个目录就打得通
		RegistryDirectory<Echo> fresh = new RegistryDirectory<>(Echo.class, consumer, newLiveRegistry());
		assertEquals("v5:bob", fresh.list().get(0).invoke(call()).getValue(),
			"新目录连的是同一个 host:port");
	}

	private FakeRegistry newLiveRegistry() {
		FakeRegistry reg = new FakeRegistry();
		reg.answer = Collections.singletonList(live(port));
		return reg;
	}

	@Test
	@DisplayName("destroy() 之后再 list() 一律空表")
	void destroyClearsTheView() {
		FakeRegistry reg = new FakeRegistry();
		reg.answer = Collections.singletonList(live(port));
		RegistryDirectory<Echo> dir = new RegistryDirectory<>(Echo.class, consumer, reg);
		assertEquals(1, dir.list().size());

		dir.destroy();
		assertTrue(dir.isDestroyed());
		assertTrue(dir.list().isEmpty());
		assertNotNull(dir.getUrl());
	}
}
