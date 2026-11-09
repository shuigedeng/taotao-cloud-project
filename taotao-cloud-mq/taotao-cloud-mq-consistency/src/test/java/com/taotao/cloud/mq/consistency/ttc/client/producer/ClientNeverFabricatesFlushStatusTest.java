package com.taotao.cloud.mq.consistency.ttc.client.producer;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * W2a §3.4：结构守卫 —— 以后谁也不许在 client 里再把刷盘超时凭空写进发送结果.
 * <p>
 * 这条尺断的是"信息来源"而不是"某个 if 分支"：{@code FLUSH_DISK_TIMEOUT} 讲的是"消息进了存储、
 * 只是同步刷盘没落住盘"，这个事实只有 store 侧知道（{@code CommitLog:373} 产出该状态，
 * {@code SendMessageProcessor:98} 把它写进响应体）。client 拿不到任何响应体就没有信息源，
 * 所以 client 的 src/main 里出现这个赋值就意味着有人在猜。
 * <p>
 * <b>负向断言必须自带阳性对照</b>：同一个扫描函数在同一趟里还要
 * <ul>
 *   <li>扫必然有该写法的地方（{@link #POSITIVE_CONTROL_DIR}，期望 &gt;= 1）——证明这把尺看得见非 0；</li>
 *   <li>扫 client 自己树里必然存在的另一种写法（{@link #NEEDLE_KNOWN_PRESENT}）——证明不是路径解析错、
 *       也不是"匹配机制坏掉"造成的假 0；</li>
 *   <li>并断言扫到的 .java 文件数下界 —— 空目录会让"0 命中"看起来像满分。</li>
 * </ul>
 * 两侧解析到的绝对路径与文件数都会打印出来。
 */
public class ClientNeverFabricatesFlushStatusTest {

    /** 被禁的写法（按字符串匹配，注释里写一行同样的字面量也算命中）. */
    private static final String FORBIDDEN_ASSIGNMENT = "setSendStatus(SendStatus.FLUSH_DISK_TIMEOUT)";
    /** 更严的一层：client 的 main 里连这个状态码的名字都不该出现（出现即意味着它在猜结果）. */
    private static final String FORBIDDEN_TOKEN = "FLUSH_DISK_TIMEOUT";
    /** 阳性对照目录：真映射所在模块的 src/main（相对仓根），期望至少 1 命中. */
    private static final String POSITIVE_CONTROL_DIR = "z-mq-broker/src/main";
    private static final int POSITIVE_CONTROL_MIN_HITS = 1;
    /** 针级对照：client 自己树里确实存在的写法，同一个扫描函数必须数到非 0. */
    private static final String NEEDLE_KNOWN_PRESENT = "setSendStatus(SendStatus.SEND_OK)";

    private static final String CLIENT_DIR = "z-mq-client/src/main";
    /** client src/main 的 .java 文件数下界：低于它说明扫错了地方. */
    private static final int CLIENT_MIN_JAVA_FILES = 20;

    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Test
    public void clientMainNeverAssignsTheFlushDiskTimeoutStatus() throws IOException {
        File clientSrc = resolveModuleSrc(CLIENT_DIR);
        Scan scan = scanTree(clientSrc, FORBIDDEN_ASSIGNMENT);
        report("client/" + CLIENT_DIR + " 禁用的赋值", clientSrc, FORBIDDEN_ASSIGNMENT, scan);

        assertTrue(scan.javaFiles >= CLIENT_MIN_JAVA_FILES,
                "scan must actually read the client sources, saw only " + scan.javaFiles
                        + " .java files under " + scan.root.getAbsolutePath());
        assertTrue(scan.hitLines == 0,
                "z-mq-client/src/main must not fabricate a flush verdict (" + FORBIDDEN_ASSIGNMENT
                        + "), but found " + scan.hitLines + " hit line(s): " + scan.hits);
    }

    @Test
    public void clientMainNeverEvenNamesTheFlushDiskTimeoutStatus() throws IOException {
        File clientSrc = resolveModuleSrc(CLIENT_DIR);
        Scan scan = scanTree(clientSrc, FORBIDDEN_TOKEN);
        report("client/" + CLIENT_DIR + " 禁用 token", clientSrc, FORBIDDEN_TOKEN, scan);

        assertTrue(scan.javaFiles >= CLIENT_MIN_JAVA_FILES,
                "scan must actually read the client sources, saw only " + scan.javaFiles
                        + " .java files under " + scan.root.getAbsolutePath());
        assertTrue(scan.hitLines == 0,
                "only the broker knows whether the disk flushed; z-mq-client/src/main must not name "
                        + FORBIDDEN_TOKEN + " at all, but found " + scan.hitLines + " line(s): " + scan.hits);
    }

    /**
     * 阳性对照：同一把尺在同一趟里必须数到非 0 —— 否则上面那两个 0 命中毫无意义.
     */
    @Test
    public void theSameScannerSeesTheRealFlushAssignmentWhereItLegitimatelyLives() throws IOException {
        File controlSrc = resolveModuleSrc(POSITIVE_CONTROL_DIR);
        Scan scan = scanTree(controlSrc, FORBIDDEN_ASSIGNMENT);
        report("阳性对照/" + POSITIVE_CONTROL_DIR, controlSrc, FORBIDDEN_ASSIGNMENT, scan);

        assertTrue(scan.javaFiles > 0,
                "positive-control scan read no .java file at all: " + controlSrc.getAbsolutePath());
        assertTrue(scan.hitLines >= POSITIVE_CONTROL_MIN_HITS,
                "the scanner is blind: expected at least " + POSITIVE_CONTROL_MIN_HITS + " legitimate "
                        + FORBIDDEN_ASSIGNMENT + " under " + controlSrc.getAbsolutePath()
                        + " (it is the real store->broker mapping), found " + scan.hitLines
                        + ". A guard whose reference set is empty proves nothing.");
    }

    /**
     * 针级对照：在<b>被判 0 命中的同一棵树</b>里换一个确实存在的字面量，必须数到非 0.
     * 这条守住的是"路径解析对了、匹配机制也在工作"，与上一条（跨模块）互补。
     */
    @Test
    public void theSameScannerFindsAStringThatIsReallyInClientMain() throws IOException {
        File clientSrc = resolveModuleSrc(CLIENT_DIR);
        Scan scan = scanTree(clientSrc, NEEDLE_KNOWN_PRESENT);
        report("针级对照/client", clientSrc, NEEDLE_KNOWN_PRESENT, scan);

        assertTrue(scan.hitLines >= 1,
                "scanner cannot see " + NEEDLE_KNOWN_PRESENT + " in " + clientSrc.getAbsolutePath()
                        + " after reading " + scan.javaFiles + " files — the 0-hit guards would be empty runs");
    }

    // ==================== 扫描与路径解析 ====================

    private static void report(String label, File root, String needle, Scan scan) {
        System.out.println("[w2a-guard] " + label
                + " | needle=" + needle
                + " | absRoot=" + root.getAbsolutePath()
                + " | javaFiles=" + scan.javaFiles
                + " | hitLines=" + scan.hitLines
                + " | hits=" + scan.hits
                + " | user.dir=" + System.getProperty("user.dir"));
    }

    /**
     * 从 surefire 的工作目录（默认是模块目录 {@code z-mq-client}）往上找仓根，
     * 再拼出 {@code <模块>/src/main} 的绝对路径。解析不到就直接 fail ——
     * 让"扫错地方"表现为红，而不是表现成一个漂亮的 0 命中。
     */
    private static File resolveModuleSrc(String moduleRelPath) {
        File start = new File(System.getProperty("user.dir")).getAbsoluteFile();
        List<String> tried = new ArrayList<String>();
        File dir = start;
        for (int up = 0; up < 5 && dir != null; up++) {
            File candidate = new File(dir, moduleRelPath);
            tried.add(candidate.getAbsolutePath());
            if (candidate.isDirectory()) {
                return candidate.getAbsoluteFile();
            }
            dir = dir.getParentFile();
        }
        fail("cannot resolve " + moduleRelPath + " from user.dir=" + start.getAbsolutePath()
                + "; tried: " + tried);
        return null;
    }

    private static Scan scanTree(File root, String needle) throws IOException {
        Scan scan = new Scan(root);
        if (!root.isDirectory()) {
            throw new IOException("scan root is not a directory: " + root.getAbsolutePath());
        }
        walk(root, needle, scan);
        return scan;
    }

    private static void walk(File file, String needle, Scan scan) throws IOException {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children == null) {
                throw new IOException("cannot list directory: " + file.getAbsolutePath());
            }
            Arrays.sort(children);
            for (int i = 0; i < children.length; i++) {
                File child = children[i];
                if (child.isDirectory() && "target".equals(child.getName())) {
                    continue; // 生成物不算源码
                }
                walk(child, needle, scan);
            }
            return;
        }
        if (!file.getName().endsWith(".java")) {
            return;
        }
        scan.javaFiles++;
        String text = new String(Files.readAllBytes(file.toPath()), UTF8);
        String[] lines = text.split("\r?\n", -1);
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains(needle)) {
                scan.hitLines++;
                scan.hits.add(relativize(file) + ":" + (i + 1));
            }
        }
    }

    private static String relativize(File file) {
        File root = new File(System.getProperty("user.dir")).getAbsoluteFile();
        String base = root.getAbsolutePath();
        String path = file.getAbsolutePath();
        return path.startsWith(base) ? path.substring(base.length() + 1) : path;
    }

    /** 一趟扫描的读数：读到的 .java 文件数 + 命中行数 + 命中位置. */
    private static final class Scan {
        private final File root;
        private int javaFiles;
        private int hitLines;
        private final List<String> hits = new ArrayList<String>();

        Scan(File root) {
            this.root = root;
        }
    }
}
