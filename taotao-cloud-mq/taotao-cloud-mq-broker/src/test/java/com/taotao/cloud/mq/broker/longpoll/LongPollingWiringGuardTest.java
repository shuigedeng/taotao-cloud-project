package com.taotao.cloud.mq.broker.longpoll;

import com.taotao.cloud.mq.broker.processor.PullMessageProcessor;
import com.taotao.cloud.mq.client.consumer.DefaultMQPullConsumer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * W2c 机检守卫（工单 §5 CP-C + §2.3 不变式 + §3.1 契约）。
 * <p>
 * 全部是"扫真实源文件 + 读常量"的结构性检查，不依赖起进程：
 * <ol>
 *   <li><b>接线守卫</b>：长轮询服务的 {@code suspendPull} 与 {@code notifyMessageArrived}
 *       在各模块 src/main 里除自身声明文件外<b>各必须至少有一个读者</b>。
 *       工单 09-26 的读数是"零读者"（只有 {@code store/ha/HAService} 那个同名方法），
 *       所以这条守卫在接上之前应该是红的。</li>
 *   <li><b>按类区分</b>：{@code notifyMessageArrived} 在 HA 那一侧是<b>另一个类的同名方法</b>
 *       （签名 (long, byte[])），不许把它算成长轮询的读者；反之 HA 那几个文件也必须在守卫的
 *       命中里出现（否则"0 命中"可能只是扫错了地方）。</li>
 *   <li><b>阳性对照</b>：同一次运行里必须量出非 0 的已知存在项，并量出 0 的已知不存在项 ——
 *       只报"0 命中"不证明尺看得见非 0，等于没跑。</li>
 *   <li><b>§2.3 不变式</b>：客户端 RPC 超时严格大于它自己写进请求的挂起预算，
 *       且 broker 不许回落到服务级 15s 默认值（processor 里不许出现 {@code DEFAULT_HOLD_TIMEOUT_MS}）。</li>
 *   <li><b>§3.1 契约</b>：请求/响应两个字段名在 client 与 broker 两侧必须是同一个字符串。</li>
 * </ol>
 */
public class LongPollingWiringGuardTest {

    /** 长轮询服务的声明文件自身（它的声明与 javadoc 不算读者）. */
    private static final String HOLD_SERVICE_FILE = "PullRequestHoldService.java";
    /** {@code notifyMessageArrived(long, byte[])} 那个同名方法所在文件（不许被算成长轮询的读者）. */
    private static final List<String> HA_SAME_NAME_FILES =
            Arrays.asList("HAService.java", "DefaultHAService.java");

    /** {@code PullRequestHoldService} 扫描周期的镜像值（该类没暴露这个 private 常量）. */
    private static final long HOLD_SCAN_INTERVAL_MILLIS = 1_000L;

    // ==================== 1. 接线守卫 + 2. 按类区分 ====================

    @Test
    @DisplayName("CP-C: suspendPull 在各模块 src/main 里除自身文件外必须有读者（工单读数=零读者, 接上之前该红）")
    public void suspendPullHasARealReaderInMain() throws IOException {
        Scan scan = scanModules("suspendPull(", true);
        report("suspendPull(", scan);
        Scan readers = scan.excluding(HOLD_SERVICE_FILE);
        assertTrue(readers.hitLines >= 1,
                "suspendPull 在 src/main 里除自身声明文件外零读者 ⇒ 长轮询根本没接进 pull 路径。"
                        + " 全部命中=" + scan.describeHits());
        assertTrue(readers.touchesFile("PullMessageProcessor.java"),
                "suspendPull 的读者必须落在 pull 请求路径上（PullMessageProcessor），实际="
                        + readers.describeHits());
    }

    @Test
    @DisplayName("CP-C: 长轮询那个 notifyMessageArrived 必须有读者, 且不许拿 HA 的同名方法充数")
    public void longPollingNotifyHasARealReaderDistinctFromHa() throws IOException {
        Scan all = scanModules("notifyMessageArrived(", true);
        report("notifyMessageArrived(", all);
        assertTrue(all.hitLines >= 3,
                "这次扫描一共只读到 " + all.hitLines + " 行命中, 连 HA 那两行 + 长轮询的都没量到 ⇒ 范围不对: "
                        + all.describeHits());

        // 尺本身要看得见 HA 那个同名方法（工单 §1 的读数就是它两条），否则下面的"排除 HA"是空话
        Scan haSide = all.onlyFiles(HA_SAME_NAME_FILES);
        assertTrue(haSide.hitLines >= 1,
                "守卫没在 HA 的同名方法上看见 notifyMessageArrived( ⇒ 扫的不是这片源码, 0 命中不可信: "
                        + all.describeHits());
        assertFalse(haSide.referencesLongPollingClass(),
                "HA 那几个文件不该引用 PullRequestHoldService（它们的是 (long, byte[]) 那个同名方法）");

        Scan longPollingReaders = all.excluding(HOLD_SERVICE_FILE).thatReferenceLongPollingClass();
        assertTrue(longPollingReaders.hitLines >= 1,
                "长轮询的 notifyMessageArrived(topic, queueId) 零读者 ⇒ 到达侧没接线（HA 那个同名方法不算）。"
                        + " 全部命中=" + all.describeHits());
        assertTrue(longPollingReaders.touchesFile("SendMessageProcessor.java"),
                "唤醒点必须落在知道 (topic, queueId) 的写入层（SendMessageProcessor），实际="
                        + longPollingReaders.describeHits());
    }

    // ==================== 3. 阳性 / 阴性对照 ====================

    @Test
    @DisplayName("尺自检: 同一个扫描器必须既量得出非 0（两个已知存在项）也量得出 0（一个不存在项）")
    public void theScannerSeesBothZeroAndNonZero() throws IOException {
        Scan getter = scanModules("getPullRequestHoldService(", true);
        report("getPullRequestHoldService(", getter);
        assertTrue(getter.hitLines >= 1,
                "阳性对照失败: getPullRequestHoldService( 在 src/main 里本该有命中（BrokerController 的 getter 就是）,"
                        + " 却量到 0 ⇒ 扫描器没在扫真的东西, 上面所有 0 命中都是空跑");

        Scan haCall = scanModules("haService.notifyMessageArrived(", true);
        report("haService.notifyMessageArrived(", haCall);
        assertTrue(haCall.hitLines >= 1,
                "阳性对照失败: 长轮询接上之后 HA 那条同名调用照样必须在（BrokerController 里）, 量到 0 说明扫描范围错了");

        Scan absent = scanModules("w2cNeedleThatCannotExistInThisRepo_9f3c1a(", true);
        report("不存在的针", absent);
        assertTrue(absent.javaFiles >= 20,
                "扫描器只读了 " + absent.javaFiles + " 个 src/main 文件, 分母小到不可信");
        assertEquals(0, absent.hitLines,
                "不存在的针必须 0 命中, 否则命中数没有意义: " + absent.describeHits());
    }

    // ==================== 4. §2.3 不变式 ====================

    @Test
    @DisplayName("§2.3: 客户端 pull 的 RPC 超时严格大于它写进请求的挂起预算")
    public void clientRpcTimeoutAlwaysOutlivesTheHoldBudget() throws IOException {
        long[] budgets = new long[] {0L, 1L, 500L, 999L, 1_000L, 2_999L, 3_000L, 3_001L, 5_000L,
                15_000L, PullMessageProcessor.MAX_SUSPEND_BUDGET_MILLIS,
                PullMessageProcessor.MAX_SUSPEND_BUDGET_MILLIS * 4L, 600_000L};
        for (int i = 0; i < budgets.length; i++) {
            long b = budgets[i];
            long timeout = DefaultMQPullConsumer.pullRpcTimeoutMillis(b);
            assertTrue(timeout > b,
                    "预算 " + b + "ms 对应的客户端超时只有 " + timeout + "ms ⇒ broker 还没醒客户端就先超时,"
                            + " 工单 §2.3 的账（3000ms 客户端 vs 15000ms 默认挂起）会原样复发");
            if (b > 0) {
                // broker 的到期由 hold 服务的扫描线程兑现 ⇒ 超时至少要再留一个扫描周期
                assertTrue(timeout - b >= HOLD_SCAN_INTERVAL_MILLIS,
                        "预算 " + b + "ms 时余量只有 " + (timeout - b)
                                + "ms, 不到一个挂起扫描周期(" + HOLD_SCAN_INTERVAL_MILLIS + "ms) ⇒ 会假超时");
            }
        }
        // 不带预算 = 接线前那个数, 逐字不许动 (§3.1)
        assertEquals(3_000L, DefaultMQPullConsumer.pullRpcTimeoutMillis(0L),
                "不带预算的 pull 超时必须仍是接线前的 3000ms");
        assertEquals(3_000L, DefaultMQPullConsumer.PULL_RPC_TIMEOUT_MILLIS);

        // broker 侧不许回落到服务级 15s 默认值: processor 里不能出现那个常量
        Scan defaultHold = scanModules("DEFAULT_HOLD_TIMEOUT_MS", true);
        report("DEFAULT_HOLD_TIMEOUT_MS", defaultHold);
        assertTrue(defaultHold.touchesFile(HOLD_SERVICE_FILE),
                "阳性对照: DEFAULT_HOLD_TIMEOUT_MS 本该在 " + HOLD_SERVICE_FILE + " 里, 量不到说明扫描范围错了: "
                        + defaultHold.describeHits());
        assertFalse(defaultHold.excluding(HOLD_SERVICE_FILE).touchesFile("PullMessageProcessor.java"),
                "PullMessageProcessor 引用了服务级默认挂起时长 ⇒ 挂起时长不再由请求带来 (§2.2②): "
                        + defaultHold.describeHits());
    }

    @Test
    @DisplayName("§3.1: 挂起预算/唤醒原因两个字段名在 client 与 broker 两侧是同一个字符串")
    public void wireFieldNamesMatchOnBothSides() throws IOException {
        assertEquals(PullMessageProcessor.EXT_SUSPEND_TIMEOUT_MILLIS, DefaultMQPullConsumer.SUSPEND_TIMEOUT_FIELD,
                "客户端写的预算字段与 broker 读的字段不同名 ⇒ 长轮询永远不触发, 而且是静默的");
        assertEquals(PullMessageProcessor.EXT_SUSPEND_WAKEUP, DefaultMQPullConsumer.SUSPEND_WAKEUP_FIELD,
                "broker 写的唤醒原因字段与客户端读的字段不同名 ⇒ suspendWakeup 恒为 null, CP-B 失去靶子");

        Scan budgetField = scanModules("\"" + DefaultMQPullConsumer.SUSPEND_TIMEOUT_FIELD + "\"", false);
        report("client 里的预算字段字面量", budgetField);
        assertEquals(1, budgetField.hitLines,
                "ttc-mq-client 的 src/main 里这个字段字面量只该出现在一处（写请求的地方）: "
                        + budgetField.describeHits());
        assertTrue(budgetField.touchesFile("DefaultMQPullConsumer.java"),
                "只有 pull 消费者会往请求上写挂起预算, 别处冒出来要先解释: " + budgetField.describeHits());
    }

    // ==================== 扫描工具 ====================

    private static void report(String needle, Scan scan) {
        System.out.println("[w2c-guard] needle=" + needle + " javaFilesScanned=" + scan.javaFiles
                + " hitLines=" + scan.hitLines + " hits=" + scan.describeHits()
                + " | user.dir=" + System.getProperty("user.dir"));
    }

    /** 扫仓里每个 ttc-mq-* 模块的 src/main（不含 target）. */
    private static Scan scanModules(String needle, boolean allModules) throws IOException {
        File repoRoot = resolveRepoRoot();
        Scan scan = new Scan(needle);
        File[] children = repoRoot.listFiles();
        if (children == null) {
            throw new IOException("cannot list repo root: " + repoRoot.getAbsolutePath());
        }
        Arrays.sort(children);
        for (File module : children) {
            if (!module.isDirectory() || !module.getName().startsWith("ttc-mq-")) {
                continue;
            }
            if (!allModules && !"ttc-mq-client".equals(module.getName())) {
                continue;
            }
            File srcMain = new File(module, "src/main");
            if (srcMain.isDirectory()) {
                walk(srcMain, scan);
            }
        }
        return scan;
    }

    /**
     * surefire 的工作目录是模块目录, 往上找到同时含 ttc-mq-broker / ttc-mq-client / ttc-mq-store 的那一层。
     * 找不到就 fail —— 让"扫错地方"表现为红, 而不是表现为一个漂亮的 0 命中。
     */
    private static File resolveRepoRoot() {
        File dir = new File(System.getProperty("user.dir")).getAbsoluteFile();
        List<String> tried = new ArrayList<>();
        for (int up = 0; up < 6 && dir != null; up++) {
            tried.add(dir.getAbsolutePath());
            if (new File(dir, "ttc-mq-broker").isDirectory() && new File(dir, "ttc-mq-client").isDirectory()
                    && new File(dir, "ttc-mq-store").isDirectory()) {
                return dir;
            }
            dir = dir.getParentFile();
        }
        fail("cannot resolve repo root from user.dir; tried " + tried);
        return null;
    }

    private static void walk(File file, Scan scan) throws IOException {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children == null) {
                throw new IOException("cannot list " + file.getAbsolutePath());
            }
            Arrays.sort(children);
            for (File child : children) {
                if (child.isDirectory() && "target".equals(child.getName())) {
                    continue; // 生成物不算源码
                }
                walk(child, scan);
            }
            return;
        }
        if (!file.getName().endsWith(".java")) {
            return;
        }
        scan.javaFiles++;
        String text = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        boolean fileKnowsHoldService = text.contains("PullRequestHoldService");
        int hitLines = 0;
        int longPollingShaped = 0;
        String[] lines = text.split("\r?\n", -1);
        for (int i = 0; i < lines.length; i++) {
            if (!lines[i].contains(scan.needle)) {
                continue;
            }
            hitLines++;
            boolean haReceiver = lines[i].contains("haService");
            if (fileKnowsHoldService && !haReceiver) {
                longPollingShaped++;
            }
            scan.hits.add(file.getName() + ":" + (i + 1) + (haReceiver ? "(HA)" : ""));
        }
        if (hitLines > 0) {
            scan.hitLines += hitLines;
            scan.files.add(new HitFile(file, hitLines, longPollingShaped));
        }
    }

    /**
     * 一个 src/main 文件对某根针的命中读数.
     * <p>
     * "按类区分同名方法"落到**行级**：这一行是不是长轮询那个类的调用，判据是
     * ①该文件确实引用 {@code PullRequestHoldService}，且 ②这一行不是 {@code haService.} 上的
     * 那个同名方法（HA 的签名是 (long, byte[])，接收者在本仓恒为 {@code haService}）。
     */
    private static final class HitFile {
        private final File file;
        private final int hitLines;
        private final int longPollingShapedLines;

        HitFile(File file, int hitLines, int longPollingShapedLines) {
            this.file = file;
            this.hitLines = hitLines;
            this.longPollingShapedLines = longPollingShapedLines;
        }
    }

    /** 一趟扫描的读数：读了多少文件、命中多少行、命中在哪. */
    private static final class Scan {
        private final String needle;
        private int javaFiles;
        private int hitLines;
        private final List<HitFile> files = new ArrayList<>();
        private final List<String> hits = new ArrayList<>();

        Scan(String needle) {
            this.needle = needle;
        }

        Scan excluding(String fileNameSuffix) {
            Scan out = new Scan(needle);
            out.javaFiles = javaFiles;
            out.hits.addAll(hits);
            for (HitFile hf : files) {
                if (!hf.file.getName().endsWith(fileNameSuffix)) {
                    out.files.add(hf);
                    out.hitLines += hf.hitLines;
                }
            }
            return out;
        }

        Scan onlyFiles(List<String> fileNameSuffixes) {
            Scan out = new Scan(needle);
            out.javaFiles = javaFiles;
            out.hits.addAll(hits);
            for (HitFile hf : files) {
                for (String suffix : fileNameSuffixes) {
                    if (hf.file.getName().endsWith(suffix)) {
                        out.files.add(hf);
                        out.hitLines += hf.hitLines;
                        break;
                    }
                }
            }
            return out;
        }

        /** 只留"这一行是长轮询那个类的调用"的命中（把 HA 的同名方法分出去）. */
        Scan thatReferenceLongPollingClass() {
            Scan out = new Scan(needle);
            out.javaFiles = javaFiles;
            out.hits.addAll(hits);
            for (HitFile hf : files) {
                if (hf.longPollingShapedLines > 0) {
                    out.files.add(hf);
                    out.hitLines += hf.longPollingShapedLines;
                }
            }
            return out;
        }

        boolean referencesLongPollingClass() {
            for (HitFile hf : files) {
                if (hf.longPollingShapedLines > 0) {
                    return true;
                }
            }
            return false;
        }

        boolean touchesFile(String fileNameSuffix) {
            for (HitFile hf : files) {
                if (hf.file.getName().endsWith(fileNameSuffix)) {
                    return true;
                }
            }
            return false;
        }

        String describeHits() {
            return hits.isEmpty() ? "<无命中>" : hits.toString();
        }
    }
}
