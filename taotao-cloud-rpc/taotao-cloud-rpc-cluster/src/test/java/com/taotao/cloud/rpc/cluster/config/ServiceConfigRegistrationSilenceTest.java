package com.taotao.cloud.rpc.cluster.config;

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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ServiceConfig 的注册失败面：`registerToRegistry()` 里那句 `registryService.register(serviceUrl)`
 * 在生产链路上恒空指针，被同一支 catch 吞成一行 ERROR，导出对外仍然是绿的。
 * 这一族的三条主张分别钉"日志是唯一的痕迹"、"进程里没有可编程位点"、
 * 以及"那句『适配暂时禁用』的 WARN 在注册真的成功时照样打印"。
 */
class ServiceConfigRegistrationSilenceTest {

    interface Echo {
        String say(String what);
    }

    static final class EchoImpl implements Echo {
        @Override
        public String say(String what) {
            return "echo:" + what;
        }
    }

    /** 与 ServiceConfigTest.SpyRegistry 分家：这里的 unregister 也要计数，注销侧的守卫形状是被审的对象。 */
    static final class CountingRegistry implements RegistryService {
        final AtomicInteger registerCalls = new AtomicInteger();
        final AtomicInteger unregisterCalls = new AtomicInteger();
        volatile URL lastRegistered;
        volatile URL lastUnregistered;

        @Override
        public void register(URL url) {
            lastRegistered = url;
            registerCalls.incrementAndGet();
        }

        @Override
        public void unregister(URL url) {
            lastUnregistered = url;
            unregisterCalls.incrementAndGet();
        }

        @Override
        public void subscribe(URL url, NotifyListener listener) {
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
        }
    }

    /**
     * 阳性对照夹具：字段扫描那把尺必须读得出"有人把失败记进了进程状态"这个形状，
     * 否则『没有可编程位点』会是一次空跑。
     */
    static final class RecordedFailure {
        volatile String lastRegistrationFailure;

        String getLastRegistrationFailure() {
            return lastRegistrationFailure;
        }
    }

    private static final String REGISTRY = "127.0.0.1:8084";
    private static final String FAILURE_PREFIX = "Failed to register service to registry: ";
    private static final String SUCCESS_PREFIX = "Service registered to registry: ";
    private static final String EXPORTED_PREFIX = "Exported service: ";

    /** port 0 让内核自己分配，避开 freePort() 那种 probe-then-bind race。 */
    private static ServiceConfig<Echo> config(RegistryService registryService) {
        ServiceConfig<Echo> cfg = new ServiceConfig<>();
        cfg.setInterfaceClass(Echo.class);
        cfg.setRef(new EchoImpl());
        cfg.setHost("127.0.0.1");
        cfg.setPort(0);
        cfg.setRegistry(REGISTRY);
        if (registryService != null) {
            cfg.setRegistryService(registryService);
        }
        return cfg;
    }

    private static List<String> names(Field[] fields) {
        List<String> out = new ArrayList<>();
        for (Field f : fields) {
            out.add(f.getName());
        }
        return out;
    }

    private static List<String> methodNames(Method[] methods) {
        List<String> out = new ArrayList<>();
        for (Method m : methods) {
            out.add(m.getName());
        }
        return out;
    }

    private static List<String> matching(List<String> candidates, Pattern p) {
        List<String> out = new ArrayList<>();
        for (String c : candidates) {
            if (p.matcher(c).find()) {
                out.add(c);
            }
        }
        return out;
    }

    // ---------- 捕获尺 ----------

    /**
     * core 这边 `LogManager` 实测直连 log4j-core（logger 是 `org.apache.logging.log4j.core.Logger`），
     * 与 starter 那边"API 被 slf4j 桥走、只能挂 logback appender"的形状相反，所以这里挂 log4j-core 的 appender。
     * 全仓没有任何 log4j2 配置文件，root 实测就是 DEBUG + Console；这里显式设成同一个值并在 close 里还原，
     * 不是为了降噪，是为了让"被降级成 debug 的那条"仍然进得来捕获，从而能被 level 判出来。
     */
    private static final class Capturer extends AbstractAppender implements AutoCloseable {
        private static final String NAME = "r26-capture";

        private final List<Shot> events = Collections.synchronizedList(new ArrayList<Shot>());
        private final LoggerConfig root;
        private final Level previousLevel;
        private final LoggerContext context;

        /** 一条事件的副本。log4j2 会复用 LogEvent 实例，留住引用会读到后面别的行。 */
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

        void reset() {
            events.clear();
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

        boolean levelIsDebug() {
            return root.getLevel() == Level.DEBUG;
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
    @DisplayName("bug_registrationFailureIsVisibleOnlyInTheLog：注册炸了，对外仍然是导出成功")
    void bug_registrationFailureIsVisibleOnlyInTheLog() {
        Capturer cap = Capturer.open();
        try {
            assertTrue(cap.levelIsDebug(), "捕获挂不上或 level 被改动，后面的缺失型断言全部无意义");

            ServiceConfig<Echo> cfg = config(null);
            cfg.export();
            try {
                List<String> seen = cap.allMessages(ServiceConfig.class);
                assertTrue(containsPrefix(seen, EXPORTED_PREFIX),
                        "捕获里没有 Exported service 说明这把尺一条都没读到，下面的『成功行不在场』是空跑而不是证据");

                List<String> errors = cap.messages(ServiceConfig.class, Level.ERROR);
                assertEquals(1, errors.size(), "注册失败的 ERROR 行数：" + errors);
                assertTrue(errors.get(0).startsWith(FAILURE_PREFIX + REGISTRY),
                        "ERROR 行不是预期的那句：" + errors.get(0));

                // 原来这一带是靠"空 registryService 被解引用、抛 NPE、再被 catch 吞掉"走完的。
                // 现在事先判空，不再制造那个 NPE —— 失败原因直接进 ERROR 行与可查字段。
                // 真正抛异常的分支由 throwingRegistry 那条用例覆盖。
                assertTrue(errors.get(0).contains("RegistryService is not configured"),
                        "ERROR 行没说明真实原因（少了事先判空）：" + errors.get(0));

                assertFalse(containsPrefix(seen, SUCCESS_PREFIX),
                        "注册根本没成功，成功行却在场：" + seen);

                assertTrue(cfg.isExported(), "调用方看到的旗子是绿的");
                assertNull(cfg.getRegistryService());

                // 失败现在有了对外位点：不再只存在于日志里。
                String failure = cfg.getLastRegistrationFailure();
                assertNotNull(failure, "注册炸了却没有留下可查的失败描述，调用方依然无从判断");
                assertTrue(failure.contains("RegistryService"),
                        "失败原因应当说明是 RegistryService 没注入，而不是别的： " + failure);
            } finally {
                cfg.unexport();
            }

            // 阳性对照：把实现真交进去，同一条链路上成功行在场、ERROR 不在场
            cap.reset();
            CountingRegistry spy = new CountingRegistry();
            ServiceConfig<Echo> wired = config(spy);
            wired.export();
            try {
                assertEquals(1, spy.registerCalls.get());
                assertEquals(0, cap.messages(ServiceConfig.class, Level.ERROR).size());
                assertTrue(containsPrefix(cap.messages(ServiceConfig.class, Level.INFO), SUCCESS_PREFIX),
                        "这把尺读不到成功行，上面那条『成功行不在场』就永远是白过的");
                assertNotNull(spy.lastRegistered);
                assertEquals(Echo.class.getName(), spy.lastRegistered.getServiceInterface());
            } finally {
                wired.unexport();
            }
        } finally {
            cap.close();
        }
    }

    @Test
    @DisplayName("bug_noAccessorReportsTheSwallowedRegistrationFailure：失败过，但进程里没有一格能问它")
    void bug_noAccessorReportsTheSwallowedRegistrationFailure() {
        Pattern failureSurface = Pattern.compile("(?i)fail|error|cause|thrown|exception");

        Capturer cap = Capturer.open();
        try {
            ServiceConfig<Echo> cfg = config(null);
            cfg.export();
            try {
                assertEquals(1, cap.messages(ServiceConfig.class, Level.ERROR).size(),
                        "这一条主张的前提是失败确实发生过；ERROR 不在场说明夹具没造出病灶");
                assertNull(cfg.getRegistryService());
                assertEquals(REGISTRY, cfg.getRegistry());

                // 修复后：失败有了位点。这两条断言从"必须为空"翻成"必须读得到"，
                // 原来的主张（没有位点）正是它们钉住的缺陷。
                List<String> fields = names(ServiceConfig.class.getDeclaredFields());
                assertFalse(fields.isEmpty(), "字段清单本身为空的话下面两条断言都是空跑");
                assertFalse(matching(fields, failureSurface).isEmpty(),
                        "ServiceConfig 的字段里读不到记账用的名字，失败又没有落点：" + fields);

                List<String> methods = methodNames(ServiceConfig.class.getDeclaredMethods());
                assertFalse(methods.isEmpty());
                assertFalse(matching(methods, failureSurface).isEmpty(),
                        "ServiceConfig 的方法里读不到能问失败的名字：" + methods);

                assertNotNull(cfg.getLastRegistrationFailure(),
                        "位点在签名上但读不出内容，说明记账那一步漏了");
            } finally {
                cfg.unexport();
            }

            // 阳性对照：同一把尺在"真的有人记账"的类上必须非空
            assertFalse(matching(names(RecordedFailure.class.getDeclaredFields()), failureSurface).isEmpty(),
                    "字段扫描这把尺读不出 lastRegistrationFailure，上面那条『没有位点』是无牙的");
            assertFalse(matching(methodNames(RecordedFailure.class.getDeclaredMethods()), failureSurface).isEmpty(),
                    "方法扫描这把尺读不出 getLastRegistrationFailure，同上");
        } finally {
            cap.close();
        }
    }

    @Test
    @DisplayName("fix_registrationOutcomeIsUnambiguous：注入可用时只有『已注册』，没有自相矛盾的『适配禁用』")
    void fix_registrationOutcomeIsUnambiguous() {
        Capturer cap = Capturer.open();
        try {
            CountingRegistry spy = new CountingRegistry();
            ServiceConfig<Echo> wired = config(spy);
            wired.export();
            try {
                assertEquals(1, spy.registerCalls.get(), "注册真的成功了，这条主张才有意义");
                // 原来无论成败都会打「ZConfigRegistry 适配暂时禁用」，紧接着又打
                // 「已注册」—— 两句自相矛盾，日志读起来像坏了。这里注册既已发生，
                // 就不该再宣称适配被禁用。
                assertEquals(Collections.emptyList(), cap.messages(ServiceConfig.class, Level.WARN),
                        "注册成功时不该再打印『适配暂时禁用』这种与事实相反的警告");
                assertTrue(containsPrefix(cap.messages(ServiceConfig.class, Level.INFO), SUCCESS_PREFIX),
                        "成功行不在场说明这条对照没走到");
                assertNull(wired.getLastRegistrationFailure(),
                        "注册成功却留着失败描述，说明记账那一步没在成功分支清干净");
            } finally {
                wired.unexport();
            }

            // 失败那侧同样不该有「已注册」，且失败原因可查
            cap.reset();
            ServiceConfig<Echo> cfg = config(null);
            cfg.export();
            try {
                assertEquals(Collections.emptyList(), cap.messages(ServiceConfig.class, Level.WARN),
                        "失败侧也不该打印『适配禁用』，失败有自己的 ERROR 行");
                assertFalse(containsPrefix(cap.allMessages(ServiceConfig.class), SUCCESS_PREFIX),
                        "注册没发生却打了成功行");
                assertNotNull(cfg.getLastRegistrationFailure());
            } finally {
                cfg.unexport();
            }
        } finally {
            cap.close();
        }
    }

    @Test
    @DisplayName("unregisterIsGuardedAwayAndLeavesNoLogLine：注销侧整块被卫兵跳过，一句都不打")
    void unregisterIsGuardedAwayAndLeavesNoLogLine() {
        Capturer cap = Capturer.open();
        try {
            ServiceConfig<Echo> cfg = config(null);
            cfg.export();
            cap.reset();
            cfg.unexport();

            List<String> during = cap.allMessages(ServiceConfig.class);
            assertTrue(containsPrefix(during, "Unexported service: "),
                    "捕获里没有 Unexported service，说明这把尺在注销阶段一条都没读到");
            assertEquals(0, cap.messages(ServiceConfig.class, Level.ERROR).size(),
                    "注销阶段冒出 ERROR 说明 `registryService != null` 那道守卫不在了：" + during);
            assertFalse(containsAny(during, "unregister", "Unregister"),
                    "注销侧出现了注册相关的日志，这条『零痕迹』主张要改：" + during);

            // 阳性对照：交进实现之后 unregister 真的被调用，注销这条路本身是通的
            cap.reset();
            CountingRegistry spy = new CountingRegistry();
            ServiceConfig<Echo> wired = config(spy);
            wired.export();
            wired.unexport();
            assertEquals(1, spy.registerCalls.get());
            assertEquals(1, spy.unregisterCalls.get(),
                    "unregister 一次都没被调用，上面那条『守卫把整块跳过』就不是在量守卫");
            assertNotNull(spy.lastUnregistered);
        } finally {
            cap.close();
        }
    }

    @Test
    @DisplayName("fix_registrationThatThrowsIsRecordedWithItsCause：注册真炸了，原因也进得了格")
    void fix_registrationThatThrowsIsRecordedWithItsCause() {
        Capturer cap = Capturer.open();
        try {
            ServiceConfig<Echo> cfg = config(new RegistryService() {
                @Override
                public void register(URL url) {
                    throw new IllegalStateException("registry unreachable");
                }

                @Override
                public void unregister(URL url) {
                }

                @Override
                public void subscribe(URL url, NotifyListener listener) {
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
                }
            });
            cfg.export();
            try {
                String failure = cfg.getLastRegistrationFailure();
                assertNotNull(failure, "注册抛了异常却没有记下来，调用方仍然无从判断");
                assertTrue(failure.contains("IllegalStateException")
                                && failure.contains("registry unreachable"),
                        "失败描述里没有异常类型/原因：" + failure);
                assertFalse(containsPrefix(cap.allMessages(ServiceConfig.class), SUCCESS_PREFIX),
                        "注册抛异常却打了成功行");
                // 服务本身仍应可用：注册失败不阻断导出
                assertTrue(cfg.isExported());
            } finally {
                cfg.unexport();
            }
        } finally {
            cap.close();
        }
    }

    private static boolean containsPrefix(List<String> lines, String prefix) {
        for (String l : lines) {
            if (l.contains(prefix)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsAny(List<String> lines, String... needles) {
        for (String l : lines) {
            for (String n : needles) {
                if (l.contains(n)) {
                    return true;
                }
            }
        }
        return false;
    }
}
