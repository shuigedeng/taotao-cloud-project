package com.taotao.cloud.mq.broker.processor;

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
 * 「Topic 配置从盘上读回来」这条边的结构守卫（机检，不起进程）。
 * <p>
 * 一张只在内存里的 topic 表，行为测试量不出来：进程活着的时候它一切正常，
 * 只有"换一个新实例、同一存储目录"才暴露它。除了那条端到端用例之外，还需要一把<b>静态</b>的尺，
 * 因为本仓出现过"能力写进 javadoc、src/main 里零实现"的形状 —— 那种东西没有行为可测，
 * 只能量"这个名字在 src/main 里除了声明之外有没有读者"。
 * <ol>
 *   <li><b>启动路径必须有 load 的读者</b>：后端的 {@code start()} 必须出现在 {@code BrokerController}
 *       里，且位置早于 {@code remotingServer.start()}。晚于绑端口的话，头几条请求会读到一张还没填好的表，
 *       那是个竞态而不是错误，行为测试基本抓不到。</li>
 *   <li><b>只能有一张表</b>：除持久化后端自己的声明文件之外，{@code src/main} 里不许再存在第二块
 *       {@code ConcurrentHashMap<String, TopicConfig>}。两处状态 = "内存改了、文件没改"的入口。</li>
 *   <li><b>两条写边都经过后端</b>：建/改 topic 与全量替换都必须打在同一个后端上。</li>
 *   <li><b>版本号那半不许掉</b>：topic 版本号自增的两个调用点（建/改、全量替换）是 Slave
 *       增量同步的唯一依据；落盘解决的是"重启后还在"，不解决"slave 何时覆盖"。</li>
 *   <li><b>尺自检</b>：同一把尺必须既量得出非 0（已知存在项）也量得出 0（不存在项），
 *       还要能区分"代码里真调了"与"javadoc 里提了一句"（本文件 javadoc 里那个
 *       TOPIC_PROSE_ONLY_NEEDLE_4d7 就是给这条用的）。</li>
 * </ol>
 * 针全部是字符串字面量而不是类型引用：这样"实现还不存在"时这个文件照样编译得出来，
 * 才量得出"先红后绿"。
 */
public class TopicConfigWiringGuardTest {

    /** 持久化后端的声明文件自身（它自己那块表是唯一的表，不算重复）. */
    private static final String MANAGER_FILE = "TopicConfigManager.java";

    /** 后端上三个动作的调用形状：建/改一条、全量替换、按 topic 查. */
    private static final String PUT_CALL = "topicConfigManager.putTopicConfig(";
    private static final String REPLACE_CALL = "topicConfigManager.replaceAllTopicConfigs(";
    private static final String SELECT_CALL = "topicConfigManager.selectTopicConfig(";
    /** 启动路径上那条"从盘上读回来"的边. */
    private static final String LOAD_CALL = "topicConfigManager.start()";
    /** 表类型字面量：出现两处就说明有两份状态. */
    private static final String TABLE_FIELD = "ConcurrentHashMap<String, TopicConfig>";
    /** 版本号自增（Slave 增量同步的依据）. */
    private static final String VERSION_BUMP = "topicConfigDataVersion.assignNewVersion()";

    // ==================== 1. 启动路径必须把 topic 表从盘上读回来 ====================

    @Test
    @DisplayName("机检: 启动路径上必须有后端 start()（内含 load）这个读者, 且早于 remotingServer.start()")
    public void topicConfigsAreLoadedFromDiskBeforeTheBrokerServesRequests() throws IOException {
        Scan all = scanMain(LOAD_CALL);
        report(LOAD_CALL, all);
        assertTrue(all.hitLines >= 1,
                "topic 配置的后端从来没有被任何 src/main 代码从盘上读回来 ⇒ 表只在内存里, "
                        + "broker 一重启就空, 心跳上报空 topic 列表, 路由里就没有这个 topic ⇒ "
                        + "「Topic 配置跨重启保留」是假的。全部命中=" + all.describeHits());
        assertTrue(all.touchesFile("BrokerController.java"),
                "load 的调用点应当在 BrokerController 的初始化路径上（本仓位点后端就在那儿）, 实际="
                        + all.describeHits());

        // 位置：读盘必须早于绑端口收请求
        File controller = findMainFile("BrokerController.java");
        String text = new String(Files.readAllBytes(controller.toPath()), StandardCharsets.UTF_8);
        int loadAt = codeIndexOf(text, LOAD_CALL);
        int serveAt = codeIndexOf(text, "remotingServer.start()");
        assertTrue(loadAt >= 0, "BrokerController 里没有 " + LOAD_CALL + " 这个代码调用点");
        assertTrue(serveAt >= 0, "BrokerController 里没有 remotingServer.start(), 尺的假设有变");
        assertTrue(loadAt < serveAt,
                "topic 表必须在 Netty 开始收请求之前读回来, 否则头几条请求看到的是一张空表: loadAt=" + loadAt
                        + " serveAt=" + serveAt);
    }

    // ==================== 2. 只能有一张表 ====================

    @Test
    @DisplayName("唯一真相源: 除后端自己的声明外, src/main 里不许存在第二块 topic 配置表")
    public void onlyOneTopicConfigTableExistsInSrcMain() throws IOException {
        Scan all = scanMain(TABLE_FIELD);
        report(TABLE_FIELD, all);
        Scan others = all.excludingFiles(MANAGER_FILE);
        assertEquals(0, others.hitLines,
                "出现了第二份 topic 配置状态（" + others.describeHits() + "）⇒ 表改了文件未必改、文件改了表未必改, "
                        + "「重启后还在」就不成立了");

        // 后端自己那份必须存在, 否则上面那条 0 命中是空跑
        Scan declared = scanModules("ttc-mq-store", TABLE_FIELD);
        report("store 里的 " + TABLE_FIELD, declared);
        assertTrue(declared.touchesFile(MANAGER_FILE),
                "持久化后端必须自己持有这张表（上面那条 0 命中才是有内容的）, 实际=" + declared.describeHits());
    }

    // ==================== 3. 两条写边都经过后端 ====================

    @Test
    @DisplayName("写路径: 建/改 topic 与全量替换都必须打在持久化后端上")
    public void bothWritePathsGoThroughThePersistentBackend() throws IOException {
        Scan put = scanMain(PUT_CALL);
        report(PUT_CALL, put);
        assertTrue(put.touchesFile("AdminBrokerProcessor.java"),
                "CREATE/UPDATE_AND_CREATE 的写点必须经后端（只有内存 put 的话重启就没了）, 实际="
                        + put.describeHits());

        Scan replace = scanMain(REPLACE_CALL);
        report(REPLACE_CALL, replace);
        assertTrue(replace.touchesFile("AdminBrokerProcessor.java"),
                "全量替换（slave 从主覆盖进来那条路）也必须经后端, 否则那条路上表与盘会分叉, 实际="
                        + replace.describeHits());

        Scan select = scanMain(SELECT_CALL);
        report(SELECT_CALL, select);
        assertTrue(select.touchesFile("AdminBrokerProcessor.java"),
                "读也要问同一个后端（读内存副本 = 又一次两份状态）, 实际=" + select.describeHits());

        // 这三个名字必须在后端上真有声明 —— 只有调用点没声明编译不过, 但反过来"声明在、没人调"是本守卫要防的
        assertTrue(scanModules("ttc-mq-store", "public TopicConfig putTopicConfig(").hitLines >= 1,
                "后端缺少写入口的声明");
        assertTrue(scanModules("ttc-mq-store", "public void replaceAllTopicConfigs(").hitLines >= 1,
                "后端缺少全量替换口的声明");
        assertTrue(scanModules("ttc-mq-store", "public TopicConfig selectTopicConfig(").hitLines >= 1,
                "后端缺少按 topic 查询口的声明");
    }

    // ==================== 4. 版本号那半不许掉 ====================

    @Test
    @DisplayName("回归锁: topic 版本号自增的两个调用点（建/改、全量替换）必须还在")
    public void topicConfigDataVersionIsStillBumpedOnBothWritePaths() throws IOException {
        Scan bumps = scanMain(VERSION_BUMP);
        report(VERSION_BUMP, bumps);
        assertTrue(bumps.touchesFile("AdminBrokerProcessor.java"),
                "版本号自增点应当在写 topic 的那两处, 实际=" + bumps.describeHits());
        assertTrue(bumps.hitLines >= 2,
                "topic 版本号只在内存里，但不许丢：slave 的增量判断（比版本再覆盖）全靠它。"
                        + "落盘解决「重启后还在」, 不解决「slave 何时覆盖」。实测自增点=" + bumps.hitLines
                        + " 处, 命中=" + bumps.describeHits());
    }

    // ==================== 5. 尺自检 ====================

    @Test
    @DisplayName("尺自检: 阳性对照非 0、阴性对照 0、注释行不算读者、分母可信")
    public void theScannerSeesBothZeroAndNonZero() throws IOException {
        Scan positive = scanMain("consumerOffsetManager.start()");
        report("consumerOffsetManager.start()", positive);
        assertTrue(positive.hitLines >= 1,
                "阳性对照失败: 位点后端的 start() 在 BrokerController 的初始化路径上本该有读者, 却量到 0"
                        + " ⇒ 这把尺没在扫真的东西, 上面所有 0 命中都是空跑");
        assertTrue(positive.touchesFile("BrokerController.java"),
                "阳性对照要看得见 BrokerController 上那条 start(): " + positive.describeHits());

        Scan absent = scanMain("w2dNeedleThatCannotExistInThisRepo_9f31ab(");
        report("不存在的针", absent);
        assertTrue(absent.javaFiles >= 100,
                "扫描器只读了 " + absent.javaFiles + " 个 src/main 文件, 分母小到不可信");
        assertEquals(0, absent.hitLines, "不存在的针必须 0 命中: " + absent.describeHits());

        // 注释里的针：不过滤注释量得到、过滤之后必须归零 —— 否则守卫会把 javadoc 当成接线
        String proseNeedle = "TOPIC_PROSE_ONLY_" + "NEEDLE_4d7";
        int withProse = countInSelf(proseNeedle, false);
        int withoutProse = countInSelf(proseNeedle, true);
        System.out.println("[w2d-guard] proseNeedle=" + proseNeedle + " withProse=" + withProse
                + " withoutProse=" + withoutProse);
        assertTrue(withProse >= 1,
                "本文件 javadoc 里写了这根针, 不过滤注释时该量到, 实际=" + withProse + " ⇒ 尺自检的前提没了");
        assertEquals(0, withoutProse,
                "同一个针在过滤掉注释行之后必须 0 命中（它只出现在 javadoc 里）, 实际=" + withoutProse);
    }

    // ==================== 扫描工具 ====================

    private static void report(String needle, Scan scan) {
        System.out.println("[w2d-guard] needle=" + needle + " javaFilesScanned=" + scan.javaFiles
                + " hitLines=" + scan.hitLines + " hits=" + scan.describeHits()
                + " | user.dir=" + System.getProperty("user.dir"));
    }

    /** 扫仓里每个 ttc-mq-* 模块的 src/main（不含 target）. */
    private static Scan scanMain(String needle) throws IOException {
        return scanModules(null, needle);
    }

    /**
     * @param moduleName 非 null 时只扫那一个模块
     */
    private static Scan scanModules(String moduleName, String needle) throws IOException {
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
            if (moduleName != null && !moduleName.equals(module.getName())) {
                continue;
            }
            File srcMain = new File(module, "src/main");
            if (srcMain.isDirectory()) {
                walk(srcMain, scan, true);
            }
        }
        return scan;
    }

    /** 在本文件里数这根针（用来量"注释算不算读者"）. */
    private static int countInSelf(String needle, boolean skipProse) throws IOException {
        File self = new File(resolveRepoRoot(),
                "ttc-mq-broker/src/test/java/com/shuigedeng/ttc/mq/broker/processor/TopicConfigWiringGuardTest.java");
        assertTrue(self.isFile(), "尺自检要读自己: " + self.getAbsolutePath());
        Scan scan = new Scan(needle);
        walk(self, scan, skipProse);
        return scan.hitLines;
    }

    /** 找到 src/main 下某个名字的文件. */
    private static File findMainFile(String fileName) throws IOException {
        File repoRoot = resolveRepoRoot();
        File[] children = repoRoot.listFiles();
        if (children == null) {
            throw new IOException("cannot list repo root: " + repoRoot.getAbsolutePath());
        }
        Arrays.sort(children);
        List<String> seen = new ArrayList<String>();
        for (File module : children) {
            if (!module.isDirectory() || !module.getName().startsWith("ttc-mq-")) {
                continue;
            }
            File srcMain = new File(module, "src/main");
            if (!srcMain.isDirectory()) {
                continue;
            }
            File hit = findByName(srcMain, fileName, seen);
            if (hit != null) {
                return hit;
            }
        }
        throw new IOException("cannot find " + fileName + " under src/main; scanned " + seen);
    }

    private static File findByName(File file, String fileName, List<String> seen) {
        if (file.isFile()) {
            seen.add(file.getName());
            return fileName.equals(file.getName()) ? file : null;
        }
        File[] children = file.listFiles();
        if (children == null) {
            return null;
        }
        Arrays.sort(children);
        for (File child : children) {
            File hit = findByName(child, fileName, seen);
            if (hit != null) {
                return hit;
            }
        }
        return null;
    }

    /**
     * surefire 的工作目录是模块目录, 往上找到同时含 ttc-mq-broker / ttc-mq-client / ttc-mq-store 的那一层。
     * 找不到就 fail —— 让"扫错地方"表现为红, 而不是表现为一个漂亮的 0 命中。
     */
    private static File resolveRepoRoot() {
        File dir = new File(System.getProperty("user.dir")).getAbsoluteFile();
        List<String> tried = new ArrayList<String>();
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

    private static void walk(File file, Scan scan, boolean skipProse) throws IOException {
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
                walk(child, scan, skipProse);
            }
            return;
        }
        if (!file.getName().endsWith(".java")) {
            return;
        }
        scan.javaFiles++;
        String text = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        int hitLines = 0;
        String[] lines = text.split("\r?\n", -1);
        for (int i = 0; i < lines.length; i++) {
            if (!lines[i].contains(scan.needle)) {
                continue;
            }
            if (skipProse && isProse(lines[i])) {
                continue;
            }
            hitLines++;
            scan.hits.add(file.getName() + ":" + (i + 1));
        }
        if (hitLines > 0) {
            scan.hitLines += hitLines;
            scan.files.add(new HitFile(file, hitLines));
        }
    }

    /** 这一行是散文（javadoc / 行注释 / 块注释续行）而不是代码? */
    private static boolean isProse(String line) {
        String t = line.trim();
        return t.startsWith("*") || t.startsWith("/*") || t.startsWith("//");
    }

    /** 针在代码行里的位置（跳过注释行）; 找不到返回 -1. */
    private static int codeIndexOf(String text, String needle) {
        String[] lines = text.split("\r?\n", -1);
        int offset = 0;
        for (String line : lines) {
            if (!isProse(line)) {
                int at = line.indexOf(needle);
                if (at >= 0) {
                    return offset + at;
                }
            }
            offset += line.length() + 1;
        }
        return -1;
    }

    /** 一个文件对某根针的命中读数. */
    private static final class HitFile {
        private final File file;
        private final int hitLines;

        HitFile(File file, int hitLines) {
            this.file = file;
            this.hitLines = hitLines;
        }
    }

    /** 一趟扫描的读数：读了多少文件、命中多少行、命中在哪. */
    private static final class Scan {
        private final String needle;
        private int javaFiles;
        private int hitLines;
        private final List<HitFile> files = new ArrayList<HitFile>();
        private final List<String> hits = new ArrayList<String>();

        Scan(String needle) {
            this.needle = needle;
        }

        Scan excludingFiles(String... fileNameSuffixes) {
            Scan out = new Scan(needle);
            out.javaFiles = javaFiles;
            for (HitFile hf : files) {
                boolean drop = false;
                for (String suffix : fileNameSuffixes) {
                    if (hf.file.getName().endsWith(suffix)) {
                        drop = true;
                        break;
                    }
                }
                if (!drop) {
                    out.files.add(hf);
                    out.hitLines += hf.hitLines;
                }
            }
            for (HitFile hf : out.files) {
                for (String hit : hits) {
                    if (hit.startsWith(hf.file.getName() + ":")) {
                        out.hits.add(hit);
                    }
                }
            }
            return out;
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
