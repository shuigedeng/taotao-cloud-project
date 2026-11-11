package com.taotao.cloud.rpc.cluster.config;

import com.taotao.cloud.rpc.cluster.RegistryDirectory;
import com.taotao.cloud.rpc.common.URL;
import com.taotao.cloud.rpc.registry.NotifyListener;
import com.taotao.cloud.rpc.registry.RegistryService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 消费侧的订阅静默面。`ReferenceConfig.connectRegistry()` 在没有任何注册中心实现被 new 出来的情况下
 * 打印 `Connected to registry: <地址>`（`:205`），紧接着 `RegistryDirectory.subscribe()` 把
 * `registryService.subscribe(...)` 抛出的空指针（`:52`，字段恒 null）吞成一行 ERROR（`:55`），
 * 于是 `get()` 照样返回一个可用的代理 —— 谎称已连接、失败只活在日志里，是同一次初始化的两面。
 *
 * 第 26 轮审的是导出侧那次被吞掉的注册（`ServiceConfig`），这一族是它在消费侧的镜像副本：
 * 三条主张分别钉"订阅失败唯一的痕迹是日志"、"那句已连接是在给一次没发生的连接背书"、
 * "那句『适配暂时禁用』在订阅真的成功时照样打印"，第四条钉"注销侧既不复述也不否认那句已连接"。
 *
 * 第五条（第 28 轮补）钉的是"那句已连接与有没有接上线无关"：接线成功那一次它照样打印。
 * 它存在的理由是第 27 轮量出的一个判据缺口 —— 把整行摘掉（m27-04）与给它加
 * `registryService != null` 守卫（m27-05，即"把谎改成真话"）在前四条上的红集逐字相同，
 * 只有这一条能让两支分开：摘行让它红，加守卫让它绿。
 */
class ReferenceConfigSubscriptionSilenceTest {

    interface Speaker {
        String speak(String what);
    }

    /** 订阅与销毁都计数；destroyCanThrow 打开时把注销侧的日志语句变成可达的。 */
    static final class CountingRegistry implements RegistryService {
        final AtomicInteger subscribeCalls = new AtomicInteger();
        final AtomicInteger destroyCalls = new AtomicInteger();
        volatile boolean destroyCanThrow;

        @Override
        public void register(URL url) {
        }

        @Override
        public void unregister(URL url) {
        }

        @Override
        public void subscribe(URL url, NotifyListener listener) {
            subscribeCalls.incrementAndGet();
        }

        @Override
        public void unsubscribe(URL url, NotifyListener listener) {
        }

        @Override
        public List<URL> lookup(URL url) {
            return Collections.emptyList();
        }

        @Override
        public void destroy() {
            destroyCalls.incrementAndGet();
            if (destroyCanThrow) {
                throw new IllegalStateException("r27 对照：注销侧自己炸了");
            }
        }
    }

    private static final String REGISTRY = "127.0.0.1:8084";
    private static final String SUBSCRIBE_FAILURE_PREFIX = "Failed to subscribe service: ";
    private static final String SUBSCRIBED_PREFIX = "Subscribed to service: ";
    private static final String CONNECTED_CLAIM_PREFIX = "Connected to registry: ";
    private static final String DIRECT_MODE_PREFIX = "Reference using direct URL mode";
    private static final String DISABLED_WARN_KEY = "ZConfigRegistry";
    private static final String DESTROY_REGISTRY_FAILURE = "Failed to destroy registry service";

    private static ReferenceConfig<Speaker> reference(RegistryService registryService) {
        ReferenceConfig<Speaker> ref = new ReferenceConfig<>();
        ref.setInterfaceClass(Speaker.class);
        ref.setRegistry(REGISTRY);
        if (registryService != null) {
            ref.setRegistryService(registryService);
        }
        return ref;
    }

    private static boolean containsPrefix(List<String> lines, String prefix) {
        for (String l : lines) {
            if (l.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    private static int countContaining(List<String> lines, String key) {
        int n = 0;
        for (String l : lines) {
            if (l.contains(key)) {
                n++;
            }
        }
        return n;
    }

    /**
     * 与第 26 轮同一套 core 侧挂法：本模块的 `LogManager` 实测直连 log4j-core，
     * root 默认就是 DEBUG + Console，这里显式设成同一个值并在 close 里还原，
     * 目的是让被降级成 debug 的那条仍然进得来捕获、并且能被 level 判出来。
     */
    private static final class Capturer extends AbstractAppender implements AutoCloseable {
        private static final String NAME = "r27-capture";

        private final List<Shot> events = Collections.synchronizedList(new ArrayList<Shot>());
        private final LoggerConfig root;
        private final Level previousLevel;
        private final LoggerContext context;

        /** log4j2 会复用 LogEvent 实例，留住引用会读到后面别的行，所以逐条留副本。 */
        private static final class Shot {
            final Level level;
            final String logger;
            final String message;
            final Throwable thrown;

            Shot(LogEvent event) {
                this.level = event.getLevel();
                this.logger = event.getLoggerName();
                this.message = event.getMessage().getFormattedMessage();
                this.thrown = event.getThrown();
            }
        }

        private Capturer(LoggerContext context, LoggerConfig root, Level previousLevel) {
            super(NAME, null, null, true, null);
            this.context = context;
            this.root = root;
            this.previousLevel = previousLevel;
            start();
            Configuration conf = context.getConfiguration();
            conf.addAppender(this);
            root.addAppender(this, (Level) null, null);
            root.setLevel(Level.DEBUG);
            context.updateLoggers();
        }

        static Capturer open() {
            LoggerContext context = (LoggerContext) LogManager.getContext(false);
            LoggerConfig root = context.getConfiguration().getRootLogger();
            return new Capturer(context, root, root.getLevel());
        }

        @Override
        public void append(LogEvent event) {
            events.add(new Shot(event));
        }

        List<String> messages(Class<?> owner, Level level) {
            List<String> out = new ArrayList<>();
            for (Shot e : snapshot()) {
                if (owner.getName().equals(e.logger) && e.level == level) {
                    out.add(e.message);
                }
            }
            return out;
        }

        List<String> allMessages(Class<?> owner) {
            List<String> out = new ArrayList<>();
            for (Shot e : snapshot()) {
                if (owner.getName().equals(e.logger)) {
                    out.add(e.level + " " + e.message);
                }
            }
            return out;
        }

        int count(Class<?> owner, Level level, String prefix) {
            int n = 0;
            for (String m : messages(owner, level)) {
                if (m.startsWith(prefix)) {
                    n++;
                }
            }
            return n;
        }

        Throwable thrown(Class<?> owner, Level level) {
            for (Shot e : snapshot()) {
                if (owner.getName().equals(e.logger) && e.level == level) {
                    return e.thrown;
                }
            }
            return null;
        }

        private List<Shot> snapshot() {
            synchronized (events) {
                return new ArrayList<>(events);
            }
        }

        void reset() {
            events.clear();
        }

        boolean levelIsDebug() {
            return root.getLevel() == Level.DEBUG;
        }

        boolean detached() {
            return !root.getAppenders().containsKey(NAME);
        }

        Level rootLevel() {
            return root.getLevel();
        }

        Level levelBeforeCapture() {
            return previousLevel;
        }

        @Override
        public void close() {
            root.removeAppender(NAME);
            root.setLevel(previousLevel);
            context.updateLoggers();
            stop();
        }
    }

    // ---------- 用例 ----------

    @Test
    @DisplayName("bug_subscribeFailureIsVisibleOnlyInTheLog：订阅炸了，对外仍然是代理可用")
    void bug_subscribeFailureIsVisibleOnlyInTheLog() {
        Capturer cap = Capturer.open();
        try {
            assertTrue(cap.levelIsDebug(), "捕获挂不上或 level 被改动，后面的缺失型断言全部无意义");

            ReferenceConfig<Speaker> ref = reference(null);
            Speaker proxy = ref.get();
            try {
                List<String> errors = cap.messages(RegistryDirectory.class, Level.ERROR);
                assertEquals(1, errors.size(), "订阅失败的 ERROR 行数：" + errors);
                assertTrue(errors.get(0).startsWith(SUBSCRIBE_FAILURE_PREFIX),
                        "ERROR 行不是预期的那句：" + errors.get(0));
                assertEquals(0, cap.count(RegistryDirectory.class, Level.INFO, SUBSCRIBED_PREFIX),
                        "订阅没成功，成功行却在场");

                Throwable thrown = cap.thrown(RegistryDirectory.class, Level.ERROR);
                assertNotNull(thrown);
                // 只断言类型不断言消息文本：JDK 14+ 的 helpful NPE 文本在 JDK 8 上不存在，
                // 250 那台是 1.8.0_362，把消息串写进断言会造出一台机器上的假红。
                assertTrue(thrown instanceof NullPointerException,
                        "被吞掉的异常实测是 " + thrown.getClass().getName());

                assertNotNull(proxy, "调用方拿到的是非空代理");
                assertTrue(ref.isInitialized(), "初始化旗标是绿的 —— 失败对容器完全不可见");
                assertNull(ref.getRegistryService(), "字段在场即为恒 null 那一格被改掉了，这条主张要重读代码");
            } finally {
                ref.destroy();
            }

            // 阳性对照：真交一个注册中心进去，同一把尺必须读到成功行、且读不到那行 ERROR。
            // 少了这一腿，上面的"ERROR 恰好 1 行"会分不清是病灶还是在空跑。
            cap.reset();
            CountingRegistry wired = new CountingRegistry();
            ReferenceConfig<Speaker> ok = reference(wired);
            Speaker okProxy = ok.get();
            try {
                assertEquals(1, wired.subscribeCalls.get(),
                        "对照腿前提：接线之后订阅真的要发生一次，否则下面的零 ERROR 读数是空的");
                assertEquals(0, cap.count(RegistryDirectory.class, Level.ERROR, SUBSCRIBE_FAILURE_PREFIX),
                        "接上之后仍报订阅失败，说明对照腿没走通：" + cap.messages(RegistryDirectory.class, Level.ERROR));
                assertEquals(1, cap.count(RegistryDirectory.class, Level.INFO, SUBSCRIBED_PREFIX),
                        "成功行不在场，则第一条用例的『成功行不在场』是空跑而不是证据");
                assertNotNull(okProxy);
            } finally {
                ok.destroy();
            }
        } finally {
            cap.close();
        }
        assertTrue(cap.detached(), "appender 没摘干净，会污染后面每一条读日志的尺");
        assertEquals(cap.levelBeforeCapture(), cap.rootLevel(), "root level 没还原");
    }

    @Test
    @DisplayName("bug_connectionClaimIsLoggedForAConnectionThatNeverHappened：已连接那句是给一次没发生的连接背书")
    void bug_connectionClaimIsLoggedForAConnectionThatNeverHappened() {
        Capturer cap = Capturer.open();
        try {
            ReferenceConfig<Speaker> ref = reference(null);
            ref.get();
            try {
                List<String> infos = cap.messages(ReferenceConfig.class, Level.INFO);
                assertEquals(1, cap.count(ReferenceConfig.class, Level.INFO, CONNECTED_CLAIM_PREFIX),
                        "已连接那句不在场，这条主张就没有对象可审了：" + infos);
                assertTrue(infos.contains(CONNECTED_CLAIM_PREFIX + REGISTRY),
                        "那句报的地址与实际配置不同：" + infos);
                assertNull(ref.getRegistryService(), "打印这句时字段是 null —— 没有任何东西连上过");
                assertEquals(1, cap.count(RegistryDirectory.class, Level.ERROR, SUBSCRIBE_FAILURE_PREFIX),
                        "同一批里没有订阅失败，说明『已连接』那句这回是真话，主张要改");
            } finally {
                ref.destroy();
            }

            // 阳性对照（反向）：直连模式根本不进这条路，那句已连接一行都不该有。
            cap.reset();
            ReferenceConfig<Speaker> direct = new ReferenceConfig<>();
            direct.setInterfaceClass(Speaker.class);
            direct.setUrl("ttc-rpc://127.0.0.1:1");
            direct.get();
            try {
                List<String> infos = cap.messages(ReferenceConfig.class, Level.INFO);
                assertEquals(0, countContaining(infos, CONNECTED_CLAIM_PREFIX),
                        "直连模式也报已连接，那第一条用例读到的就不是病灶：" + infos);
                assertTrue(containsPrefix(infos, DIRECT_MODE_PREFIX),
                        "直连模式那句绕开注册中心的日志不在场，捕获可能整批为空：" + infos);
            } finally {
                direct.destroy();
            }
        } finally {
            cap.close();
        }
    }

    @Test
    @DisplayName("bug_disabledWarningIsPrintedEvenWhenSubscriptionSucceeds：订阅真成功时，那句『暂时禁用』照样打印")
    void bug_disabledWarningIsPrintedEvenWhenSubscriptionSucceeds() {
        Capturer cap = Capturer.open();
        try {
            CountingRegistry wired = new CountingRegistry();
            ReferenceConfig<Speaker> ref = reference(wired);
            ref.get();
            try {
                assertEquals(1, wired.subscribeCalls.get(), "对照前提：这次订阅是真的成功了");
                List<String> warns = cap.messages(ReferenceConfig.class, Level.WARN);
                assertEquals(1, countContaining(warns, DISABLED_WARN_KEY),
                        "适配禁用那句 WARN 行数：" + warns);
                assertEquals(1, cap.count(RegistryDirectory.class, Level.INFO, SUBSCRIBED_PREFIX),
                        "成功行不在场，两句互相否定这一格读不出来");
                assertNotNull(ref.getRegistryService(), "这次是接了线的 —— WARN 说的不是现场");
            } finally {
                ref.destroy();
            }

            // 反向腿：没接线时同一句 WARN 也在场。两句 WARN 同形而现场相反，说明它是无条件打印的。
            cap.reset();
            ReferenceConfig<Speaker> unwired = reference(null);
            unwired.get();
            try {
                assertEquals(1, countContaining(cap.messages(ReferenceConfig.class, Level.WARN), DISABLED_WARN_KEY),
                        "没接线时这句 WARN 不在了，说明上一腿的读数是条件打印，主张要改");
                assertNull(unwired.getRegistryService());
            } finally {
                unwired.destroy();
            }
        } finally {
            cap.close();
        }
    }

    @Test
    @DisplayName("disconnectIsGuardedAwayAndNeverRetractsTheConnectionClaim：注销侧既不承认也不收回那句已连接")
    void disconnectIsGuardedAwayAndNeverRetractsTheConnectionClaim() {
        Capturer cap = Capturer.open();
        try {
            assertTrue(cap.levelIsDebug(), "捕获挂不上，缺失型断言无意义");

            ReferenceConfig<Speaker> ref = reference(null);
            ref.get();
            List<String> claimed = cap.messages(ReferenceConfig.class, Level.INFO);
            assertEquals(1, countContaining(claimed, CONNECTED_CLAIM_PREFIX),
                    "前提：那句已连接在场，注销侧才有该收回的东西：" + claimed);

            cap.reset();
            ref.destroy();
            List<String> batch = cap.allMessages(ReferenceConfig.class);
            List<String> infos = cap.messages(ReferenceConfig.class, Level.INFO);
            assertTrue(containsPrefix(infos, "ReferenceConfig destroyed"),
                    "销毁本身没留日志，那下面的零读数是尺没在跑：" + batch);
            assertEquals(0, cap.messages(ReferenceConfig.class, Level.ERROR).size(),
                    "注销阶段冒出 ERROR 说明那道守卫不在了：" + cap.messages(ReferenceConfig.class, Level.ERROR));
            assertEquals(1, batch.size(),
                    "注销阶段这个 logger 只留『已销毁』那一句 —— 它既不收回前面那句已连接，也不提那个注册中心：" + batch);

            // 阳性对照：接线且注册中心自己抛，`:141` 那句 ERROR 必须打得出来 —— 上面那个零不是死代码。
            cap.reset();
            CountingRegistry blow = new CountingRegistry();
            blow.destroyCanThrow = true;
            ReferenceConfig<Speaker> wired = reference(blow);
            wired.get();
            wired.destroy();
            List<String> errors = cap.messages(ReferenceConfig.class, Level.ERROR);
            assertEquals(1, countContaining(errors, DESTROY_REGISTRY_FAILURE),
                    "接线注销失败时那句 ERROR 没出现，对照腿空跑：" + errors);
            assertEquals(1, blow.destroyCalls.get(), "注销没被调到，上面的读数是空的");
        } finally {
            cap.close();
        }
    }

    /**
     * 判别腿：这一条只在"那句被整行摘掉"时红，在"那句被加上 `registryService != null` 守卫"时绿。
     * 前四条都在读没接线那一支（那里两种改法产生的字节差别看不出来），所以第 27 轮的两支注入红集相同。
     */
    @Test
    @DisplayName("bug_connectionClaimIsPrintedWhetherOrNotAnythingIsWired：接线成功那一次，那句已连接照样是无条件打印的")
    void bug_connectionClaimIsPrintedWhetherOrNotAnythingIsWired() {
        Capturer cap = Capturer.open();
        try {
            assertTrue(cap.levelIsDebug(), "捕获挂不上，下面的『恰好 1 条』就是空跑");

            CountingRegistry wired = new CountingRegistry();
            ReferenceConfig<Speaker> ref = reference(wired);
            ref.get();
            try {
                List<String> infos = cap.messages(ReferenceConfig.class, Level.INFO);
                assertNotNull(ref.getRegistryService(), "前提：这一次字段里真的有注册中心 —— 审的是『接上线时它说不说』");
                assertEquals(1, wired.subscribeCalls.get(), "前提：这次订阅真的发生了一次");
                assertEquals(1, cap.count(RegistryDirectory.class, Level.INFO, SUBSCRIBED_PREFIX),
                        "成功行不在场，这一支就不是『订阅真成功』的那一支：" + cap.messages(RegistryDirectory.class, Level.INFO));
                assertTrue(containsPrefix(infos, "ReferenceConfig initialized"),
                        "同一批里 ReferenceConfig 的其它 INFO 都不在，说明尺没在跑：" + infos);
                assertEquals(1, cap.count(ReferenceConfig.class, Level.INFO, CONNECTED_CLAIM_PREFIX),
                        "接上线、订阅也成功了，那句已连接却不在场 —— 那它就变成了条件打印，"
                                + "第 27 轮那条主张（无条件背书）要改，而 m27-05 那支注入正是改法之一：" + infos);
            } finally {
                ref.destroy();
            }
        } finally {
            cap.close();
        }
    }
}
