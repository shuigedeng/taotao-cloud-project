package com.taotao.cloud.mq.client.consumer.retry;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 「死信取走侧得有读者」的结构守卫（机检，不起进程）。
 * <p>
 * 一个只有入口没有出口的容器，行为测试量不出来：消息塞进去之后一切「正常」，
 * 没有任何一条路径能把它取出来 —— 除了重启进程把它清空。这种形状只能量
 * 「这个名字在 {@code src/main} 里除了声明之外有没有读者」。
 * <ol>
 *   <li><b>取走侧必须有生产读者</b>：{@code DeadLetterQueue} 的 {@code pollAll()} /
 *       {@code getDlqTopic()} 在 {@code src/main} 里除声明处之外必须有读者。
 *       死信 Topic 名要是只活在常量里，那「订阅死信 Topic」这句话就没有承载它的代码。</li>
 *   <li><b>阳性对照</b>：同一把尺必须量得出非 0。这里量的是投放侧
 *       （{@code putMessage} 的调用点），它一直有读者 —— 只报「0 命中」而不证明尺看得见非 0，
 *       等于没跑。</li>
 *   <li><b>尺能区分「代码里真调了」与「注释里提了一句」</b>：拿一个只出现在注释里的针名量一次，
 *       必须量出 0。</li>
 * </ol>
 * 针全部是字符串字面量而不是类型引用：这样「实现还不存在」时这个文件照样编译得出来，
 * 才量得出先红后绿。
 */
public class DeadLetterQueueReaderGuardTest {

    /** 死信容器自己的声明文件：它自身的声明行不算读者. */
    private static final String DLQ_FILE = "DeadLetterQueue.java";

    /** 取走侧的两根针. */
    private static final String POLL_ALL = "pollAll(";
    private static final String DLQ_TOPIC = "getDlqTopic(";

    /** 阳性对照：投放侧的调用形状. */
    private static final String PUT_CALL = "deadLetterQueue.putMessage(";

    @Test
    @DisplayName("机检: 死信取走侧在 src/main 里除声明处之外必须有读者")
    public void theDeadLetterSideHasAProductionReader() throws IOException {
        Scan polled = scanMain(POLL_ALL).excludingFile(DLQ_FILE);
        Scan topic = scanMain(DLQ_TOPIC).excludingFile(DLQ_FILE);
        report("取走侧 pollAll（DeadLetterQueue.java 之外）", polled);
        report("取走侧 getDlqTopic（DeadLetterQueue.java 之外）", topic);

        int readers = polled.codeHitLines() + topic.codeHitLines();
        assertTrue(readers >= 1,
                "死信的取走侧在 src/main 里零读者 ⇒ 消息一旦转入死信就再也出不来, 进程一停就没了, "
                        + "「消费者可以订阅死信 Topic 进行人工处理或告警」这句话没有承载它的代码。"
                        + " pollAll 命中=" + polled.describe() + " ; getDlqTopic 命中=" + topic.describe());
    }

    @Test
    @DisplayName("阳性对照: 同一把尺在投放侧必须量出非 0（否则上一条的 0 什么都说明不了）")
    public void theSameRulerSeesThePutSide() throws IOException {
        Scan put = scanMain(PUT_CALL).excludingFile(DLQ_FILE);
        report("投放侧 putMessage（DeadLetterQueue.java 之外）", put);
        assertTrue(put.codeHitLines() >= 1,
                "投放侧都量不出读者，说明这把尺坏了（针写错 / 根本没扫到源码树），"
                        + "那么「取走侧 0 命中」也就不能当结论: " + put.describe());
        assertTrue(put.touchesFile("ConsumeRetryService.java"),
                "重投服务必须就是那个往死信里放东西的人, 实际命中=" + put.describe());
    }

    @Test
    @DisplayName("尺自检: 注释里提一句不算读者, 真代码行才算")
    public void proseDoesNotCountAsAReader() throws IOException {
        // %DLQ% 这个串同时出现在类注释/javadoc 里和常量声明那一行上：
        // 尺要是分不清这两者, 上面两条「0 命中」的读数就没有意义。
        Scan prefix = scanMain("%DLQ%");
        report("死信 topic 前缀字面量", prefix);
        assertTrue(prefix.rawHitLines() >= 2,
                "这根针至少在「注释里提了一句」和「常量声明」两处都出现, 量不到两处说明尺没扫全: "
                        + prefix.describe());
        assertTrue(prefix.codeHitLines() >= 1,
                "常量声明那行是真代码行, 应当被算成命中: " + prefix.describe());
        assertTrue(prefix.codeHitLines() < prefix.rawHitLines(),
                "注释里那些 %DLQ% 不许被算成读者, 否则「有没有读者」这把尺量的是文档而不是代码: "
                        + prefix.describe());
    }

    // ==================== 扫描实现 ====================

    /** 一次扫描的读数：raw = 文本里出现（含注释），code = 出现在非注释代码行且不是声明行. */
    private static final class Scan {
        final String needle;
        final List<Hit> hits = new ArrayList<Hit>();

        Scan(String needle) {
            this.needle = needle;
        }

        Scan(String needle, List<Hit> hits) {
            this.needle = needle;
            this.hits.addAll(hits);
        }

        int rawHitLines() {
            return hits.size();
        }

        int codeHitLines() {
            int n = 0;
            for (Hit h : hits) {
                if (h.isCode) {
                    n++;
                }
            }
            return n;
        }

        List<String> files() {
            List<String> out = new ArrayList<String>();
            for (Hit h : hits) {
                if (!out.contains(h.file)) {
                    out.add(h.file);
                }
            }
            return out;
        }

        Scan excludingFile(String fileName) {
            List<Hit> kept = new ArrayList<Hit>();
            for (Hit h : hits) {
                if (!h.file.endsWith("/" + fileName)) {
                    kept.add(h);
                }
            }
            return new Scan(needle + " (除 " + fileName + ")", kept);
        }

        boolean touchesFile(String fileName) {
            for (Hit h : hits) {
                if (h.file.endsWith("/" + fileName)) {
                    return true;
                }
            }
            return false;
        }

        String describe() {
            if (hits.isEmpty()) {
                return "0 命中";
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < hits.size(); i++) {
                sb.append(hits.get(i));
                if (i < hits.size() - 1) {
                    sb.append(" | ");
                }
            }
            return sb.toString();
        }
    }

    /** 一根针的一次命中：在哪个文件、哪一行、算不算代码行上的读者. */
    private static final class Hit {
        final String file;
        final int line;
        final boolean isCode;

        Hit(String file, int line, boolean isCode) {
            this.file = file;
            this.line = line;
            this.isCode = isCode;
        }

        @Override
        public String toString() {
            return file + ":" + line + (isCode ? "" : "(注释)");
        }
    }

    private static Scan scanMain(String needle) throws IOException {
        File repoRoot = resolveRepoRoot();
        Scan scan = new Scan(needle);
        File[] children = repoRoot.listFiles();
        assertTrue(children != null && children.length > 0,
                "列不出仓库根: " + repoRoot.getAbsolutePath());
        Arrays.sort(children);
        for (File module : children) {
            if (!module.isDirectory() || !module.getName().startsWith("ttc-mq-")) {
                continue;
            }
            File srcMain = new File(module, "src/main");
            if (srcMain.isDirectory()) {
                walk(srcMain, needle, scan);
            }
        }
        return scan;
    }

    private static void walk(File dir, String needle, Scan scan) throws IOException {
        File[] children = dir.listFiles();
        if (children == null) {
            return;
        }
        Arrays.sort(children);
        for (File child : children) {
            if (child.isDirectory()) {
                walk(child, needle, scan);
                continue;
            }
            if (!child.getName().endsWith(".java")) {
                continue;
            }
            List<String> lines = Files.readAllLines(child.toPath(), StandardCharsets.UTF_8);
            boolean insideBlockComment = false;
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                boolean code = false;
                if (line.contains(needle)) {
                    String stripped = stripComment(line, insideBlockComment);
                    code = stripped != null && !stripped.trim().isEmpty()
                            && !isDeclaration(stripped, needle);
                    scan.hits.add(new Hit(child.getPath(), i + 1, code));
                }
                insideBlockComment = trackBlockComment(line, insideBlockComment);
            }
        }
    }

    /** 声明行的形状：{@code public List<...> pollAll(} / {@code public String getDlqTopic(}. */
    private static boolean isDeclaration(String codeLine, String needle) {
        String name = needle.replace("(", "");
        return codeLine.contains("public ") && codeLine.contains(" " + name + "(");
    }

    /** 去掉行内注释部分；整行是注释（或在块注释里）时返回 null. */
    private static String stripComment(String line, boolean insideBlockComment) {
        if (insideBlockComment) {
            return null;
        }
        String trimmed = line.trim();
        if (trimmed.startsWith("//") || trimmed.startsWith("*") || trimmed.startsWith("/*")) {
            return null;
        }
        int slash = line.indexOf("//");
        if (slash >= 0) {
            return line.substring(0, slash);
        }
        return line;
    }

    private static boolean trackBlockComment(String line, boolean inside) {
        String trimmed = line.trim();
        if (inside) {
            return !trimmed.contains("*/");
        }
        int open = trimmed.indexOf("/*");
        if (open < 0) {
            return false;
        }
        return trimmed.indexOf("*/", open + 2) < 0;
    }

    private static void report(String what, Scan scan) {
        System.out.println("[dlq-guard] " + what + ": raw=" + scan.rawHitLines()
                + " code=" + scan.codeHitLines() + " files=" + scan.describe());
    }

    private static File resolveRepoRoot() {
        File dir = new File("").getAbsoluteFile();
        for (int i = 0; i < 6 && dir != null; i++) {
            if (new File(dir, "ttc-mq-client").isDirectory() && new File(dir, "ttc-mq-broker").isDirectory()) {
                return dir;
            }
            dir = dir.getParentFile();
        }
        throw new IllegalStateException("找不到仓库根（从 " + new File("").getAbsoluteFile()
                + " 往上六层里没有同时含 ttc-mq-client 与 ttc-mq-broker 的目录）");
    }
}
