package com.taotao.cloud.mq.consistency.ttc.spring.host;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * z-mq 子进程启动器 (NameServer + Broker).
 * <p>
 * z-mq-nameserver 和 z-mq-broker 各自带 {@code Thread.currentThread().join()} 阻塞主线程,
 * 不能在 z-opc 主进程内 inline 启动 (会卡死 Spring Tomcat). 必须 spawn 子进程.
 * <p>
 * 启动方式: Spring Boot Loader PropertiesLauncher 把 z-opc-main-starter-*.jar 当作 executable,
 * 用 {@code -Dloader.main=...} 切换 Main class.
 *
 * <pre>
 *   java -Dloader.main=com.taotao.cloud.mq.consistency.ttc.nameserver.NameServerStartup \
 *        -jar target/z-opc-main-starter-1.0.0-SNAPSHOT-boot.jar 9876
 * </pre>
 *
 * <p>jvm 共享 fat jar classpath, 但 z-mq Main 是不同进程, 不会卡死 z-opc.
 * 子进程 stdout/stderr 重定向到 {@code ${java.io.tmpdir}/z-mq-nameserver.log} 等,
 * shutdown hook 在 z-opc 关停时一并 destroy 子进程.
 */
@Configuration
@ConditionalOnProperty(prefix = "z.mq", name = "enabled", havingValue = "true")
public class ZMqEmbeddedServerConfig {

    private static final Logger log = LoggerFactory.getLogger(ZMqEmbeddedServerConfig.class);

    @Value("${z.mq.namesrv-addr:127.0.0.1:9876}")
    private String namesrvAddr;

    @Value("${z.mq.namesrv-port:9876}")
    private int namesrvPort;

    @Value("${z.mq.broker-port:10911}")
    private int brokerPort;

    @Value("${z.mq.broker-store-path:${java.io.tmpdir}/z-mq-store}")
    private String brokerStorePath;

    /**
     * z-opc-main-starter fat jar 绝对路径, 由 main() 静态块设置.
     * 默认 null, 通过 {@link #setBootJarPath(String)} 注入.
     */
    private static volatile String BOOT_JAR_PATH;
    private static final AtomicBoolean STARTED = new AtomicBoolean(false);

    public static void setBootJarPath(String path) {
        BOOT_JAR_PATH = path;
    }

    private Process nameserverProcess;
    private Process brokerProcess;

    @EventListener(ApplicationStartedEvent.class)
    public void onApplicationStarted(ApplicationStartedEvent event) {
        if (!STARTED.compareAndSet(false, true)) {
            return;
        }
        String bootJar = BOOT_JAR_PATH;
        if (bootJar == null) {
            // fallback: 找 target/z-opc-main-starter-*-boot.jar
            bootJar = findBootJar();
        }
        if (bootJar == null || !new File(bootJar).exists()) {
            log.error("[z-mq] boot jar not found, skip starting nameserver/broker");
            return;
        }
        File storeDir = new File(brokerStorePath);
        if (!storeDir.exists() && !storeDir.mkdirs()) {
            log.warn("[z-mq] cannot create store dir: {}", brokerStorePath);
        }
        try {
            nameserverProcess = spawn(bootJar, "com.taotao.cloud.mq.consistency.ttc.nameserver.NameServerStartup",
                    Arrays.asList(String.valueOf(namesrvPort)), "nameserver");
            Thread.sleep(1500); // wait nameserver listening
            brokerProcess = spawn(bootJar, "com.taotao.cloud.mq.consistency.ttc.broker.BrokerStartup",
                    Arrays.asList(String.valueOf(brokerPort), namesrvAddr, brokerStorePath), "broker");
            Runtime.getRuntime().addShutdownHook(new Thread(this::destroy, "z-mq-shutdown"));
        } catch (Exception e) {
            log.error("[z-mq] failed to spawn nameserver/broker", e);
        }
    }

    private Process spawn(String bootJar, String mainClass, List<String> args, String label) throws IOException {
        String javaHome = System.getProperty("java.home");
        String javaBin = (javaHome != null && !javaHome.isEmpty() ? javaHome : "") + "/bin/java";
        // 用 Spring Boot JarLauncher 跑不同 Main 类 (Start-Class 通过 PropertiesLauncher 不可用)
        // 实际做法: 提取 BOOT-INF/lib + BOOT-INF/classes 到 classpath, java -cp 直接跑 Main
        // (Spring Boot Loader 2.7+ 没有 PropertiesLauncher, 走简化路线)
        String extractedDir = extractBootJar(bootJar);
        String cp = extractedDir + "/BOOT-INF/classes";
        File libDir = new File(extractedDir, "BOOT-INF/lib");
        File[] jars = libDir.listFiles((d, n) -> n.endsWith(".jar"));
        if (jars != null) {
            for (File j : jars) cp += ":" + j.getAbsolutePath();
        }
        List<String> cmd = new ArrayList<>();
        cmd.add(javaBin);
        // 让子进程走 log4j2 (跟主进程一致)
        cmd.add("-Dorg.springframework.boot.logging.LoggingSystem=org.springframework.boot.logging.log4j2.Log4J2LoggingSystem");
        cmd.add("-cp");
        cmd.add(cp);
        cmd.add(mainClass);
        cmd.addAll(args);
        log.info("[z-mq] spawning {}: {}", label, String.join(" ", cmd));
        ProcessBuilder pb = new ProcessBuilder(cmd);
        File logFile = new File(System.getProperty("java.io.tmpdir"), "z-mq-" + label + ".log");
        // 每代构建覆盖重写，不能用 appendTo: MqProxyController 会把这个文件里的 BindException 条数
        // 当作"本次子进程 bind 失败"的证据摆在页面顶部。实测 appendTo 下 5 次历史失败会盖住
        // 第 6 次（本次）的成功 —— 页面于是拿一份真健康的集群报"bind 失败原文"，而且这句话无法反驳。
        // 合并 stderr 进 stdout 后只开一个 to(): 若 output/error 各自 to() 同一个文件, 是两个
        // 独立 fd 各带一份写偏移, 会互相覆盖 (BindException 走 stderr、log4j2 走 stdout, 两路都有)。
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.to(logFile));
        Process p = pb.start();
        log.info("[z-mq] {} started, pid={}, log={}", label, pid(p), logFile.getAbsolutePath());
        return p;
    }

    /**
     * 提取 boot.jar 的 BOOT-INF/lib 和 BOOT-INF/classes 到 tmp 目录 (按 jar 内容指纹复用).
     */
    private static String extractBootJar(String bootJar) {
        File bootFile = new File(bootJar);
        // 用 jar 名的 hash 当 subdir, 防止同名 jar 互相覆盖
        File extractDir = new File(System.getProperty("java.io.tmpdir"),
                "z-opc-bootjar-" + Integer.toHexString(bootJar.hashCode()));
        File marker = new File(extractDir, ".extracted");
        // ⚠ 上面那行 hash 是 String.hashCode(路径字符串)，与 jar 内容无关 ⇒ 目录名跨构建恒定；
        //    而旧版 marker 只记"曾经解过包"。两条合起来的结果是：重新构建 boot.jar 之后，
        //    子进程照旧跑**上一代**的 BOOT-INF/classes，而 z-mq 页面看着一切正常 ——
        //    正是本卡反复踩的那条"出数 ≠ 出的是本次构建的数"。
        //    所以 marker 现在写内容指纹 (length:mtime)，指纹变了就重解。
        String fingerprint = bootFile.length() + ":" + bootFile.lastModified();
        String extracted = marker.exists() ? readMarker(marker) : null;
        if (!fingerprint.equals(extracted)) {
            if (extracted != null) {
                log.info("[z-mq] boot.jar fingerprint changed ({} -> {}), re-extracting", extracted, fingerprint);
            }
            extractDir.mkdirs();
            try {
                ProcessBuilder pb = new ProcessBuilder("unzip", "-q", "-o", bootFile.getAbsolutePath(),
                        "BOOT-INF/*", "-d", extractDir.getAbsolutePath());
                pb.inheritIO();
                Process p = pb.start();
                int rc = p.waitFor();
                if (rc != 0) throw new IOException("unzip failed rc=" + rc);
                writeMarker(marker, fingerprint);
                log.info("[z-mq] boot.jar extracted to {} (fingerprint={})", extractDir, fingerprint);
            } catch (Exception e) {
                throw new RuntimeException("failed to extract boot.jar: " + e.getMessage(), e);
            }
        }
        return extractDir.getAbsolutePath();
    }

    /** marker 里存的是 jar 内容指纹；读不到（旧版 0 字节 marker / 权限问题）就返回 null，让上层判"要重解" */
    private static String readMarker(File marker) {
        try {
            byte[] raw = java.nio.file.Files.readAllBytes(marker.toPath());
            String s = new String(raw, "UTF-8").trim();
            return s.isEmpty() ? null : s;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 先写临时文件再 rename：直接覆写 marker 的话， unzip 中途被 kill 会留下
     * "marker 已是新指纹、BOOT-INF 却只解了一半" 的组合 —— 那比不更新更糟，因为它看起来是最新的。
     */
    private static void writeMarker(File marker, String fingerprint) throws IOException {
        File tmp = new File(marker.getParentFile(), ".extracted.writing");
        java.io.FileOutputStream out = new java.io.FileOutputStream(tmp);
        try {
            out.write(fingerprint.getBytes("UTF-8"));
            out.flush();
        } finally {
            out.close();
        }
        java.nio.file.Files.move(tmp.toPath(), marker.toPath(),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    }

    private static long pid(Process p) {
        // 首选 JDK 9+ 的 public Process.pid() —— 它是唯一不需要 --add-opens 就能拿到 pid 的路。
        // 反射 java.lang.ProcessImpl 的私有 pid 字段在 JDK 25 上必失败: 类名判断是过的, 但
        // setAccessible 会被强封装挡掉 (InaccessibleObjectException), 于是原来这里恒返回 -1,
        // 而 -1 一路传染到 MqProxyController 的归属比对 ⇒ 健康集群被页面判成"外来"。
        try {
            return ((Number) Process.class.getMethod("pid").invoke(p)).longValue();
        } catch (Throwable ignore) {
            // 只有真跑在 Java 8 上才会走到这里 (pid() 是 Java 9 才有的)
        }
        try {
            java.lang.reflect.Field pidField = p.getClass().getDeclaredField("pid");
            pidField.setAccessible(true);
            return pidField.getLong(p);
        } catch (Exception ignored) {
            return -1;
        }
    }

    private void destroy() {
        if (brokerProcess != null && brokerProcess.isAlive()) {
            brokerProcess.destroy();
            log.info("[z-mq] broker destroyed");
        }
        if (nameserverProcess != null && nameserverProcess.isAlive()) {
            nameserverProcess.destroy();
            log.info("[z-mq] nameserver destroyed");
        }
    }

    private String findBootJar() {
        // 用户工作目录是 bootstraps/z-opc-main-starter, 上溯找 target/*-boot.jar
        File cur = new File(System.getProperty("user.dir"));
        for (int i = 0; i < 5 && cur != null; i++) {
            File target = new File(cur, "target");
            if (target.isDirectory()) {
                File[] jars = target.listFiles((d, n) -> n.endsWith("-boot.jar"));
                if (jars != null && jars.length > 0) {
                    // 选最大的 (fat jar)
                    File max = jars[0];
                    for (File f : jars) {
                        if (f.length() > max.length()) max = f;
                    }
                    return max.getAbsolutePath();
                }
            }
            cur = cur.getParentFile();
        }
        return null;
    }
}
