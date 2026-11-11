package com.taotao.cloud.rpc.spring.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.taotao.cloud.rpc.starter.properties.TtcRpcProperties;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 第 25 轮：{@link TtcRpcAutoConfiguration} 构造函数里那次 SPI 自检（TtcRpcAutoConfiguration.java:29-34）。
 *
 * <p>形状与第 22/23/24 轮同族：`catch (Exception e)` 之后只 `log.warn(..., e.getMessage())`，
 * 构造函数照常返回、容器照常 refresh。"没有痕迹"这句要限定清楚：**日志侧其实是有字的**（实测两次
 * 自检失败会在日志里留下 `ExtensionLoader - No ttc-RPC extensions found for type …` 与
 * `TtcRpcAutoConfiguration - Protocol SPI not loaded: …` 两行 WARN），本用例钉的是**可编程侧**：
 * 失败既不上抛、也不记账，这个 @Configuration 类除了 logger 字段一个状态都没留，
 * 而日志那两行有没有打，今天没有任何尺在读（见报告 §19.3 的 m25-04）。
 *
 * <p>两条"到不到得了"都是量出来的，不是推的（用一次性量具把值逼进红消息，见报告 §19.1）：
 * ① 正常装配里自检**成功** —— 本模块 classpath 上 `getDefaultExtension()` 实测返回
 * {@code com.taotao.cloud.rpc.protocol.TtcRpcProtocolImpl}，所以那支 catch 在装配齐的树里到不了；
 * ② 要让它到，得把 `ttc-rpc-protocol` 从 classpath 摘掉 —— 下面的子 classloader 就是干这件事的，
 * 摘掉之后同一句 `getDefaultExtension()` 实测抛
 * {@code IllegalStateException: No such extension: ttc-rpc for type com.taotao.cloud.rpc.api.Protocol}，
 * 而构造函数**不抛**。
 *
 * <p>日志一侧用的是 logback 的 appender，不是 log4j-core 的：本模块的 log4j2 API 实测被
 * {@code org.apache.logging.slf4j.SLF4JLogger} 接走（{@code LogManager.getContext(false)} 返回的是
 * {@code SLF4JLoggerContext}，不是 core 的 {@code LoggerContext}），所以往 log4j-core 上挂 appender
 * 会当场 ClassCastException —— 这是一个真实的绑定形状，不是选择。
 */
public class TtcRpcAutoConfigurationSelfCheckTest {

    /** 捕获用的 logback appender：只收 level + 渲染后的消息，detach 干净是本文件的一条腿。 */
    static class Recorder extends AppenderBase<ILoggingEvent> {
        final List<String> lines = Collections.synchronizedList(new ArrayList<String>());

        Recorder(String name) {
            setName(name);
        }

        @Override
        protected void append(ILoggingEvent event) {
            lines.add(event.getLevel().toString() + " " + event.getFormattedMessage());
        }
    }

    /** 同一个类上的字段名清单 —— 用来问"自检结果有没有留下任何可编程读取的痕迹"。 */
    private static List<String> fieldNames(Class<?> type) {
        List<String> out = new ArrayList<String>();
        for (Field f : type.getDeclaredFields()) {
            out.add(f.getName());
        }
        return out;
    }

    /**
     * 装配齐的那一侧：自检成功，但**先**打了 "framework initialized" 才去做自检。
     * 于是任何一次自检失败，日志里那句"已初始化"都早已出口。
     */
    @Test
    public void bannerIsCommittedBeforeTheProbeThatItClaimsToVerify() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger root = context.getLogger(Logger.ROOT_LOGGER_NAME);
        Level before = root.getLevel();
        Recorder rec = new Recorder("r25-recorder");
        rec.start();
        root.addAppender(rec);
        root.setLevel(Level.INFO);
        Throwable failure = null;
        try {
            assertNotNull(new TtcRpcAutoConfiguration());
        } catch (Throwable t) {
            failure = t;
        } finally {
            root.detachAppender("r25-recorder");
            rec.stop();
            root.setLevel(before);
        }
        assertNull(failure, "构造函数自己不该抛（本轮钉的是它不抛）");

        int banner = -1;
        int probe = -1;
        for (int i = 0; i < rec.lines.size(); i++) {
            String line = rec.lines.get(i);
            if (banner < 0 && line.contains("ttc-RPC framework initialized")) banner = i;
            if (probe < 0 && line.contains("Default Protocol SPI loaded")) probe = i;
        }
        assertTrue(banner >= 0, "前提腿：那句 framework initialized 必须真的出口过 —— "
                + "一次都没捕到就是这条尺在空跑。捕获到的：" + rec.lines);
        assertTrue(probe >= 0, "前提腿：这个装配下自检必须真的成功过（捕获到的：" + rec.lines
                + "）；如果这里出现的是 Protocol SPI not loaded，说明 classpath 变了，本用例的结论要重读");
        assertTrue(banner < probe,
                "主张：日志先说 framework initialized（#" + banner + "），之后才第一次问 SPI（#" + probe
                        + "）—— 顺序反过来就等于自检失败时那句『已初始化』已经收不回了：" + rec.lines);
        assertNull(root.getAppender("r25-recorder"), "appender 必须已摘干净，别把捕获钩子留给下一个测试类");
    }

    /**
     * 把 protocol 实现从 classpath 摘掉 —— 这是唯一能让那支 catch 活过来的装配，
     * 而摘掉之后：构造函数不抛、容器不受影响、失败本身在进程里零痕迹。
     */
    @Test
    public void bug_spiProbeFailureLeavesTheConstructorGreenAndNoProgrammaticTrace() throws Exception {
        List<URL> urls = new ArrayList<URL>();
        int dropped = 0;
        for (String entry : System.getProperty("java.class.path").split(File.pathSeparator)) {
            if (entry.contains("ttc-rpc-protocol")) {
                dropped++;
                continue;
            }
            urls.add(new File(entry).toURI().toURL());
        }
        assertTrue(dropped >= 1, "前提腿：本模块 classpath 上确实带着 ttc-rpc-protocol，"
                + "否则这个『摘掉它』的夹具什么都没摘（摘掉 " + dropped + " 项）");
        assertTrue(urls.size() > 10, "前提腿：剩下的 URL 必须还够把 spi/api/starter 三样跑起来："
                + urls.size() + " 项");

        URLClassLoader child = new URLClassLoader(urls.toArray(new URL[0]), null);
        try {
            Class<?> protocol = child.loadClass("com.taotao.cloud.rpc.api.Protocol");
            Class<?> loaderClass = child.loadClass("com.taotao.cloud.rpc.common.spi.ExtensionLoader");
            Class<?> auto = child.loadClass("com.taotao.cloud.rpc.starter.config.TtcRpcAutoConfiguration");
            assertSame(child, protocol.getClassLoader(),
                    "前提腿：这个子 classloader 必须真的自己加载了 Protocol，而不是从 parent 借来的");

            String probeOutcome;
            try {
                Object loader = loaderClass.getMethod("getExtensionLoader", Class.class).invoke(null, protocol);
                Object def = loaderClass.getMethod("getDefaultExtension").invoke(loader);
                probeOutcome = def == null ? "NULL" : def.getClass().getName();
            } catch (Throwable t) {
                Throwable real = t.getCause() == null ? t : t.getCause();
                probeOutcome = real.getClass().getName() + ": " + real.getMessage();
            }
            assertTrue(probeOutcome.startsWith("java.lang.IllegalStateException"),
                    "前提腿（猎物在场）：这个装配里 getDefaultExtension() 必须真的抛，"
                            + "构造函数那段 catch 才有东西可吞。实得：" + probeOutcome);
            assertTrue(probeOutcome.contains("No such extension: ttc-rpc"),
                    "前提腿：抛的必须是那句找不到扩展，而不是别的意外。实得：" + probeOutcome);

            Object instance;
            try {
                instance = auto.getDeclaredConstructor().newInstance();
            } catch (Throwable t) {
                fail("导出装配自检失败本该照常用 Exception 兜住，实测构造函数向外抛了：" + t);
                return;
            }
            assertNotNull(instance, "构造函数返回了 null？");
            List<String> fields = fieldNames(instance.getClass());
            assertEquals(Collections.singletonList("log"), fields,
                    "主张：这台机器上没有任何**可编程**位点能问出『默认 Protocol 没加载』（日志那两行 WARN 不算位点，"
                            + "而它们有没有打今天没有尺在读）—— 这个 @Configuration 类除了 log4j 的 logger 字段，"
                            + "一个状态都没留。自检失败的结果既不上抛、也不记账：" + fields);
        } finally {
            child.close();
        }

        // 阳性对照片段：同一把尺必须能读出"有状态的 @Configuration 长什么样"，否则上面那句是空跑。
        List<String> control = fieldNames(TtcRpcProperties.class);
        assertFalse(control.isEmpty(),
                "阳性对照：同一把 fieldNames 尺量 TtcRpcProperties 必须非空（它是有配置字段的那一类），"
                        + "否则『只有 log 一个字段』那句结论没有任何牙齿：" + control);
        assertFalse(control.contains("log"), "阳性对照：TtcRpcProperties 里的字段都是业务字段，不该混着 logger");
    }
}
