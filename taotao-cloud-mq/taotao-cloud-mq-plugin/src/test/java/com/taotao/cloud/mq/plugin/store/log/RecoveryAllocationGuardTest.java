package com.taotao.cloud.mq.plugin.store.log;

import com.taotao.cloud.mq.common.message.MessageExt;
import com.taotao.cloud.mq.plugin.store.MessageExtBrokerInner;
import com.taotao.cloud.mq.plugin.store.MessageStoreConfig;
import com.taotao.cloud.mq.plugin.store.PutMessageStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 恢复期堆分配守卫（工单 T1 主修）+ "文件自然结束"守卫（工单 T1 附注的第二个判断）.
 * <p>
 * 这把尺量的是<b>结构量</b>：一次 "写 → shutdown → 重新 load" 期间当前线程到底<b>分配了多少字节</b>
 * （{@code com.sun.management.ThreadMXBean#getThreadAllocatedBytes}），而不是"这台机器死没死"。
 * 默认 {@code mappedFileSizeCommitLog = 1GiB} 时，老实现 {@link MappedFile#markBlankAt} 里的
 * {@code new byte[blankLen]}（{@code blankLen = fileSize - pos}）一次就是 ~1.07GB —— 与 fork 堆多大无关，
 * 跨机器、跨 {@code -Xmx} 都成立。把 {@code markBlankAt} 还原成按尾巴长度分配，<b>用例 1</b> 必红
 * （1GiB 堆的 fork 里直接 {@code OutOfMemoryError}，更大的堆里增量 ~1.07GB 超阈值）；
 * <b>用例 2</b> 钉的是另一处独立的判断（干净的全零尾巴不该被当成坏数据），还原 {@code cleanFileEnd}
 * 那段只有它红 —— 两处分属两笔改动，反证也各跑各的。
 * <p>
 * 为了让守卫不是永真式，每条用例同时钉三层：
 * <ul>
 *   <li>量具自检：同一个计数器必须看得见一次真实的 8MB 分配（防"计数器恒 0 ⇒ 守卫永绿"）；</li>
 *   <li>分支自检：被守卫的代码路径<b>真的走到了</b>（盘上确实有 16 字节 BLANK 头、位点确实停在截断点）；</li>
 *   <li>刻度自检：{@code mappedFileSizeCommitLog} 必须还是 1GiB 默认值（没被调小当尺子）。</li>
 * </ul>
 * 三处改动分别由三支用例钉住：用例 1 = 恢复期封尾的堆分配与尾巴长度无关（老实现 ~1.07GB）；
 * 用例 2 = 干净重启的全零尾巴不再被当成坏数据（既不写 BLANK 也不分配）；
 * 用例 3 = 尾巴不足一个记录头时不得丢弃后面的文件。
 */
public class RecoveryAllocationGuardTest {

    /** 守卫阈值：一次 "写 → shutdown → load" 的当前线程分配增量必须小于它。老实现是 ~1.07GB。 */
    private static final long ALLOCATION_LIMIT = 32L * 1024 * 1024;

    /** 尺子刻度：必须是 MessageStoreConfig 的默认值，不许调小。 */
    private static final int DEFAULT_MAPPED_FILE_SIZE = 1024 * 1024 * 1024;

    private static final int INSTRUMENT_PROBE_SIZE = 8 * 1024 * 1024;

    @TempDir
    Path tempDir;

    private final com.sun.management.ThreadMXBean threadMx;

    /** 量具自检用：真分配一块内存并被计数器看见。 */
    private volatile byte[] sink;

    public RecoveryAllocationGuardTest() {
        java.lang.management.ThreadMXBean raw = ManagementFactory.getThreadMXBean();
        com.sun.management.ThreadMXBean allocated = null;
        if (raw instanceof com.sun.management.ThreadMXBean) {
            allocated = (com.sun.management.ThreadMXBean) raw;
            if (!allocated.isThreadAllocatedMemorySupported()) {
                allocated = null;
            }
        }
        if (allocated == null) {
            // 量具跑不了不许算守卫过（否则这条守卫在别的 JVM 上静默失效）
            fail("需要 com.sun.management.ThreadMXBean.getThreadAllocatedBytes 才能量分配，当前 JVM 是 "
                    + raw.getClass().getName());
        }
        allocated.setThreadAllocatedMemoryEnabled(true);
        this.threadMx = allocated;
    }

    @AfterEach
    public void cleanStoreDir() {
        // 这个仓历史上往 ~/ 里写过 1.0G 的残留：收尾把整棵 TempDir 删掉（@TempDir 也会删，双保险）
        deleteRecursively(tempDir.toFile());
    }

    // ============ 用例 1：坏尾巴（真的走到 markBlankAt）不许按尾巴长度分配堆数组 ============

    /**
     * 用例 1：恢复期遇到 CRC 坏掉的记录 ⇒ 走 {@code markBlankAt}。
     * 默认 1GiB 的 mapped 区下，老实现 {@code new byte[fileSize - pos]} 一次就是 ~1.07GB；
     * 本用例钉住"分配量与尾巴长度无关"。
     */
    @Test
    public void testRecoverOfGarbageTailAllocatesNoGbArray() throws Exception {
        instrumentSeesAnEightMegabyteAllocation();

        MessageStoreConfig config = config("guard-garbage-tail");
        assertEquals(DEFAULT_MAPPED_FILE_SIZE, config.getMappedFileSizeCommitLog(),
                "尺子刻度必须是 1GiB 默认 mappedFileSizeCommitLog，调小它守卫就失去意义");

        // 1) 写一条并干净关闭
        CommitLog first = new CommitLog(config);
        assertTrue(first.load());
        first.start();
        MessageExtBrokerInner msg = build("GuardTopic", 0, 64);
        assertEquals(PutMessageStatus.PUT_OK, first.putMessage(msg).getPutMessageStatus());
        final MappedFile fileBefore = first.getMappedFileQueue().getMappedFiles().get(0);
        final int fileSize = fileBefore.getFileSize();
        final int tailPos = msg.getStoreSize();
        assertEquals(tailPos, fileBefore.getWrotePosition(), "第一条记录从 0 开始");
        first.shutdown();
        assertTrue(tailPos > 0 && tailPos < fileSize - MappedFile.BLANK_HEADER_SIZE,
                "尾巴必须长到一个 BLANK 头放得下，才会走 markBlankAt");

        // 2) 人为制造坏尾巴：把截断点处的 magic 改成非 0、非 BLANK 的值。
        //    恢复扫描读到 magic != 0 && != BLANK ⇒ verifyRecord 判 INVALID ⇒ 走 markBlankAt 封尾。
        File logFile = new File(config.getStorePathCommitLog(), String.format("%020d", 0L));
        writeIntAt(logFile, tailPos + MessageCodec.POS_MAGIC, 0x11223344);

        // 3) 重新 load —— 这一段就是被测窗口
        CommitLog reopened = new CommitLog(config);
        long delta;
        try {
            long before = allocatedBytes();
            boolean loaded;
            try {
                loaded = reopened.load();
            } finally {
                delta = allocatedBytes() - before;
            }
            assertTrue(loaded, "带坏尾巴的恢复应当成功");
        } catch (OutOfMemoryError oom) {
            reopened.shutdown();
            fail("恢复期申请了 GB 级堆数组（markBlankAt 还原成 new byte[blankLen] 的形态）：" + oom);
            return;
        }
        try {
            // 4) 分支自检：这条用例真的走到了 markBlankAt —— 盘上必须有 16 字节 BLANK 头
            assertEquals(tailPos, reopened.getMappedFileQueue().getMappedFiles().get(0).getWrotePosition(),
                    "位点必须停在截断点（守卫不能是空跑）");
            assertBlankHeader(logFile, tailPos, fileSize - tailPos, fileSize);
            assertEquals(1L, reopened.getRecoveredMessages(), "坏尾巴之前那条必须恢复");
            List<MessageExt> back = reopened.pullMessage("GuardTopic", 0, 0, 10);
            assertEquals(1, back.size());
            assertEquals(msg.getMsgId(), back.get(0).getMsgId());

            assertTrue(delta >= 0, "分配计数器出现负增量，量具不可信：" + delta);
            assertTrue(delta < ALLOCATION_LIMIT,
                    "恢复期当前线程分配了 " + delta + " 字节（阈值 " + ALLOCATION_LIMIT + "）。"
                            + "尾巴长度 " + (fileSize - tailPos) + " 字节 —— 恢复期的堆分配必须与尾巴长度无关，"
                            + "老实现 new byte[blankLen] 在这里一次就是 ~1.07GB");
        } finally {
            reopened.shutdown();
        }
    }

    // ============ 用例 2：干净重启的全零尾巴既不该写 BLANK，也不该分配 ============

    @Test
    public void testCleanShutdownZeroTailIsNotTreatedAsCorruption() throws Exception {
        instrumentSeesAnEightMegabyteAllocation();

        MessageStoreConfig config = config("guard-clean-tail");
        assertEquals(DEFAULT_MAPPED_FILE_SIZE, config.getMappedFileSizeCommitLog(),
                "尺子刻度必须是 1GiB 默认值");

        CommitLog first = new CommitLog(config);
        assertTrue(first.load());
        first.start();
        MessageExtBrokerInner a = build("CleanTailTopic", 0, 48);
        MessageExtBrokerInner b = build("CleanTailTopic", 0, 96);
        assertEquals(PutMessageStatus.PUT_OK, first.putMessage(a).getPutMessageStatus());
        assertEquals(PutMessageStatus.PUT_OK, first.putMessage(b).getPutMessageStatus());
        final MappedFile only = first.getMappedFileQueue().getMappedFiles().get(0);
        final int wroteAtShutdown = only.getWrotePosition();
        final int fileSize = only.getFileSize();
        first.shutdown();
        assertTrue(wroteAtShutdown > 0 && wroteAtShutdown < fileSize,
                "本用例要量的就是【写了一半、后面全是零】这种干净重启的正常形态");

        File logFile = new File(config.getStorePathCommitLog(), String.format("%020d", 0L));
        assertEquals(0, readIntAt(logFile, wroteAtShutdown), "关机时截断点处本来就是 0");

        CommitLog reopened = new CommitLog(config);
        long delta;
        try {
            long before = allocatedBytes();
            try {
                assertTrue(reopened.load());
            } finally {
                delta = allocatedBytes() - before;
            }
        } catch (OutOfMemoryError oom) {
            reopened.shutdown();
            fail("干净重启的全零尾巴触发了 GB 级分配（老实现每个非满文件的尾巴都会走一次）：" + oom);
            return;
        }
        try {
            assertEquals(2L, reopened.getRecoveredMessages(), "两条都已确认的消息都要回来");
            assertEquals(wroteAtShutdown,
                    reopened.getMappedFileQueue().getMappedFiles().get(0).getWrotePosition(),
                    "位点必须停在关机时的写入位点，而不是被推到文件末尾");
            // 全零尾巴是干净中断的正常形态：不该在它上面留一个假封尾
            assertEquals(0, readIntAt(logFile, wroteAtShutdown + MessageCodec.POS_MAGIC),
                    "干净的全零尾巴上不该被写 BLANK magic（它不是坏数据）");
            List<MessageExt> back = reopened.pullMessage("CleanTailTopic", 0, 0, 10);
            assertEquals(2, back.size());
            assertEquals(a.getMsgId(), back.get(0).getMsgId());
            assertEquals(b.getMsgId(), back.get(1).getMsgId());

            assertTrue(delta >= 0, "分配计数器出现负增量，量具不可信：" + delta);
            assertTrue(delta < ALLOCATION_LIMIT,
                    "干净重启的恢复期分配了 " + delta + " 字节（阈值 " + ALLOCATION_LIMIT + "）");
        } finally {
            reopened.shutdown();
        }
    }

    // ============ 用例 3：文件只剩不到一个记录头 ⇒ 后面的文件不能被当作坏尾巴丢掉 ============

    @Test
    public void testTailTooShortForBlankHeaderDoesNotDropNextFile() throws Exception {
        // 这条要构造的是【写完最后一条记录后，文件恰好剩 1..15 字节】的自然形态：
        // 写入侧在这种情况下走 "no room even for BLANK header, seal file without marker"（只推位点、不留标记），
        // 于是恢复期看到的是一段"连 16 字节 BLANK 头都放不下"的零尾巴。
        // 它不量分配，只量"已确认的消息读得回来"。
        final int smallFileSize = 8 * 1024;
        MessageStoreConfig config = config("guard-seam");
        config.setMappedFileSizeCommitLog(smallFileSize);

        // 挑一个记录尺寸，使得 smallFileSize 除以它余 1..15 字节
        int recordBodyLen = -1;
        int recordSize = -1;
        int leftover = -1;
        for (int bodyLen = 16; bodyLen < smallFileSize; bodyLen++) {
            int size = MessageCodec.encodedSize(build("SeamTopic", 0, bodyLen));
            if (size <= 0 || size > smallFileSize) {
                continue;
            }
            int rest = smallFileSize % size;
            if (rest >= 1 && rest <= MappedFile.BLANK_HEADER_SIZE - 1) {
                recordBodyLen = bodyLen;
                recordSize = size;
                leftover = rest;
                break;
            }
        }
        assertTrue(recordSize > 0,
                "构造不出【写完记录后恰好剩 1..15 字节】的边界，这条用例的量具失效了");

        CommitLog first = new CommitLog(config);
        assertTrue(first.load());
        first.start();

        // 按尺寸一条不落地写满第一个文件；最后一条写完正好剩 leftover 字节
        int fitCount = smallFileSize / recordSize;
        List<String> ids = new java.util.ArrayList<String>();
        for (int i = 0; i < fitCount; i++) {
            MessageExtBrokerInner msg = build("SeamTopic", 0, recordBodyLen);
            ids.add(msg.getMsgId());
            assertEquals(PutMessageStatus.PUT_OK, first.putMessage(msg).getPutMessageStatus(),
                    "第 " + i + " 条应写进第一个文件");
            assertEquals(1, first.getMappedFileQueue().getMappedFiles().size(),
                    "写满之前不该滚动到新文件");
        }
        assertEquals(leftover,
                smallFileSize - first.getMappedFileQueue().getMappedFiles().get(0).getWrotePosition(),
                "第一个文件必须恰好剩 " + leftover + " 字节（连 BLANK 头都放不下）");

        // 下一条放不下 ⇒ 滚动到第二个文件；第一个文件不带任何封尾标记
        MessageExtBrokerInner rolled = build("SeamTopic", 0, recordBodyLen);
        ids.add(rolled.getMsgId());
        assertEquals(PutMessageStatus.PUT_OK, first.putMessage(rolled).getPutMessageStatus());
        assertEquals(2, first.getMappedFileQueue().getMappedFiles().size(), "应已滚动到第二个文件");
        File seamFile = new File(config.getStorePathCommitLog(), String.format("%020d", 0L));
        for (int off = smallFileSize - leftover; off < smallFileSize; off++) {
            assertEquals(0, readFileByte(seamFile, off),
                    "尾巴 " + off + " 必须是没被写过的零字节（不该有任何封尾标记）");
        }
        first.shutdown();

        CommitLog reopened = new CommitLog(config);
        assertTrue(reopened.load());
        try {
            assertEquals(fitCount + 1L, reopened.getRecoveredMessages(),
                    "第一个文件的尾巴不足一个记录头，是【文件到此为止】而不是坏数据，"
                            + "不许借此把后面的文件整段丢掉");
            List<MessageExt> back = reopened.pullMessage("SeamTopic", 0, 0, fitCount + 2);
            assertEquals(fitCount + 1, back.size());
            for (int i = 0; i < fitCount + 1; i++) {
                assertEquals(ids.get(i), back.get(i).getMsgId(), "第 " + i + " 条 msgId");
                assertEquals((long) i, back.get(i).getQueueOffset(), "queueOffset 必须连续");
            }
            MessageExt fromSecondFile = back.get(back.size() - 1);
            assertTrue(fromSecondFile.getCommitLogOffset() >= smallFileSize,
                    "最后一条确实落在第二个文件里，必须原样读回");
        } finally {
            reopened.shutdown();
        }
    }

    // ==================== 量具与工具 ====================

    private long allocatedBytes() {
        return threadMx.getThreadAllocatedBytes(Thread.currentThread().getId());
    }

    /** 量具自检：计数器必须看得见一次真实分配，否则"增量小于阈值"是永真式。 */
    private void instrumentSeesAnEightMegabyteAllocation() {
        long before = allocatedBytes();
        sink = new byte[INSTRUMENT_PROBE_SIZE];
        long seen = allocatedBytes() - before;
        sink = null;
        assertTrue(seen >= INSTRUMENT_PROBE_SIZE,
                "ThreadMXBean 没能量到一次 " + INSTRUMENT_PROBE_SIZE + " 字节的分配（只量到 " + seen
                        + "），分配守卫失去意义");
    }

    private MessageStoreConfig config(String sub) {
        MessageStoreConfig config = new MessageStoreConfig();
        // rootDir 与 storePathCommitLog 都要显式设进 TempDir，只设 rootDir 会逃逸到 ~/store/commitlog
        config.setStorePathRootDir(tempDir.resolve(sub).toString());
        config.setStorePathCommitLog(tempDir.resolve(sub + "/commitlog").toString());
        return config;
    }

    private MessageExtBrokerInner build(String topic, int queueId, int bodyLen) {
        MessageExtBrokerInner msg = new MessageExtBrokerInner();
        msg.setTopic(topic);
        msg.setMsgId("GUARD-" + UUID.randomUUID());
        msg.setQueueId(queueId);
        msg.setSysFlag(7);
        msg.setFlag(42);
        msg.setBornTimestamp(1700000000000L);
        msg.setBornHost(new InetSocketAddress("10.1.2.3", 4001));
        msg.setStoreHost(new InetSocketAddress("192.168.7.8", 5001));
        msg.setReconsumeTimes(3);
        msg.setPreparedTransactionOffset(12345L);
        msg.setTags("TAG-GUARD");
        msg.setKeys("KEY-GUARD");
        byte[] body = new byte[bodyLen];
        for (int i = 0; i < bodyLen; i++) {
            body[i] = (byte) ('a' + (i % 26));
        }
        msg.setBody(body);
        return msg;
    }

    private static int readIntAt(File file, long pos) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        try {
            return readInt(raf, pos);
        } finally {
            raf.close();
        }
    }

    private static int readFileByte(File file, long pos) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        try {
            raf.seek(pos);
            return raf.readByte() & 0xFF;
        } finally {
            raf.close();
        }
    }

    private static void writeIntAt(File file, long pos, int value) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        try {
            raf.seek(pos);
            raf.writeInt(value);
            raf.getFD().sync();
        } finally {
            raf.close();
        }
    }

    /** BLANK 头布局：[0..3] 尾巴总长 | [4..7] BLANK_MAGIC_CODE | [8..15] 下一文件起始偏移。 */
    private static void assertBlankHeader(File file, long pos, long tailLength, long nextFileOffset)
            throws IOException {
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        try {
            assertEquals((int) tailLength, readInt(raf, pos), "BLANK 头 0..3 应是尾巴总长");
            assertEquals(CommitLog.BLANK_MAGIC_CODE, readInt(raf, pos + 4),
                    "BLANK magic 必须在偏移 4 —— 这个断言证明守卫量到的那条写路径真的走到了");
            byte[] eight = new byte[8];
            raf.seek(pos + 8);
            raf.readFully(eight);
            assertEquals(nextFileOffset, ByteBuffer.wrap(eight).getLong(), "BLANK 头应带下一文件起始偏移");
        } finally {
            raf.close();
        }
    }

    private static int readInt(RandomAccessFile raf, long pos) throws IOException {
        byte[] four = new byte[4];
        raf.seek(pos);
        raf.readFully(four);
        return ByteBuffer.wrap(four).getInt();
    }

    private static void deleteRecursively(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        File[] children = file.listFiles();
        if (children != null) {
            for (File child : children) {
                deleteRecursively(child);
            }
        }
        if (!file.delete()) {
            // 映射还挂在进程上时可能删不掉（fork 退出会释放）；这里只留下事实，不静默放过
            System.err.println("[guard] leftover (still mapped): " + file.getAbsolutePath());
        }
    }
}
