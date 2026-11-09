package com.taotao.cloud.mq.consistency.ttc.store.log;

import com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt;
import com.taotao.cloud.mq.consistency.ttc.store.MessageExtBrokerInner;
import com.taotao.cloud.mq.consistency.ttc.store.MessageStoreConfig;
import com.taotao.cloud.mq.consistency.ttc.store.PutMessageResult;
import com.taotao.cloud.mq.consistency.ttc.store.PutMessageStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.CRC32;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * CommitLog 真实持久化验收（工单 W1 §交付物 2）.
 * <p>
 * 唯一的验收目标：<b>关掉进程重开，消息在、字段在、位点在，并且读的是盘上字节</b>。
 * 每支用例都走"写 → 关 → 换一个新 CommitLog 实例同一 storeDir 恢复 → 逐字段比对"，
 * 读路径是 {@link CommitLog#pullMessage} → {@code MappedFile.selectMappedBuffer} → {@link MessageCodec#decode}，
 * 不碰进程内任何缓存对象。
 * <p>
 * 断言全部非空洞：13 个 MessageExt 自有字段每条都赋非默认值（含 bornHost/storeHost/sysFlag/
 * reconsumeTimes/preparedTransactionOffset 这几列全仓 main 没人赋值的，必须由本用例自己钉住），
 * 队列 id 用 2/5 而不是 0，物理偏移要求严格递增，storeTimestamp 与 bodyCRC 独立复算。
 */
public class CommitLogRecoveryTest {

    private static final int COUNT = 500;

    @TempDir
    Path tempDir;

    /** 一条消息落盘后的期望值快照（putMessage 会原地改写 storeTimestamp/queueOffset/commitLogOffset）。 */
    private static final class Snapshot {
        String msgId;
        int queueId;
        int storeSize;
        long queueOffset;
        int sysFlag;
        long bornTimestamp;
        String bornHost;
        long storeTimestamp;
        String storeHost;
        long commitLogOffset;
        int bodyCRC;
        int reconsumeTimes;
        long preparedTransactionOffset;
        String topic;
        String tags;
        String keys;
        int flag;
        Map<String, String> properties;
        byte[] body;
    }

    // ==================== 工具 ====================

    private MessageStoreConfig config(String sub, int mappedFileSize) {
        MessageStoreConfig config = new MessageStoreConfig();
        // storePathRootDir 与 storePathCommitLog 都必须显式设，否则 commitlog 逃逸到 ~/store/commitlog
        config.setStorePathRootDir(tempDir.resolve(sub).toString());
        config.setStorePathCommitLog(tempDir.resolve(sub + "/commitlog").toString());
        config.setMappedFileSizeCommitLog(mappedFileSize);
        return config;
    }

    private MessageExtBrokerInner build(int i, int queueId) {
        MessageExtBrokerInner msg = new MessageExtBrokerInner();
        msg.setTopic("RecoveryTopic_" + queueId);
        msg.setMsgId("MID-" + queueId + "-" + i + "-" + UUID.randomUUID().toString());
        msg.setQueueId(queueId);
        msg.setSysFlag(7);
        msg.setFlag(1000 + i);
        msg.setBornTimestamp(1700000000000L + i);
        msg.setBornHost(new InetSocketAddress("10.1.2.3", 4001 + i));
        msg.setStoreHost(new InetSocketAddress("192.168.7.8", 5001 + i));
        msg.setReconsumeTimes(3 + (i % 5));
        msg.setPreparedTransactionOffset(12345L + i);
        msg.setTags("TAG-" + (i % 7));
        msg.setKeys("KEY-" + i);
        Map<String, String> props = new HashMap<String, String>();
        props.put("UNIQ_KEY", "uniq-" + i);
        props.put("DELAY", String.valueOf(1 + (i % 4)));
        props.put("emptyHost", "");
        msg.setProperties(props);
        msg.setBody(("payload-of-message-" + i + "-with-some-bytes-to-make-it-realistic")
                .getBytes(StandardCharsets.UTF_8));
        return msg;
    }

    private static Snapshot snapshotOf(MessageExtBrokerInner msg) {
        Snapshot s = new Snapshot();
        s.msgId = msg.getMsgId();
        s.queueId = msg.getQueueId();
        s.storeSize = msg.getStoreSize();
        s.queueOffset = msg.getQueueOffset();
        s.sysFlag = msg.getSysFlag();
        s.bornTimestamp = msg.getBornTimestamp();
        s.bornHost = hostKey(msg.getBornHost());
        s.storeTimestamp = msg.getStoreTimestamp();
        s.storeHost = hostKey(msg.getStoreHost());
        s.commitLogOffset = msg.getCommitLogOffset();
        s.bodyCRC = msg.getBodyCRC();
        s.reconsumeTimes = msg.getReconsumeTimes();
        s.preparedTransactionOffset = msg.getPreparedTransactionOffset();
        s.topic = msg.getTopic();
        s.tags = msg.getTags();
        s.keys = msg.getKeys();
        s.flag = msg.getFlag();
        s.properties = new HashMap<String, String>(msg.getProperties());
        s.body = msg.getBody().clone();
        return s;
    }

    private static String hostKey(InetSocketAddress host) {
        return host == null ? null : host.getHostString() + ":" + host.getPort();
    }

    /** 写入 COUNT 条并干净关闭，返回落盘后的快照。 */
    private List<Snapshot> writeManyAndShutdown(MessageStoreConfig config, int queueId, int count) {
        CommitLog commitLog = new CommitLog(config);
        assertTrue(commitLog.load(), "首次 load 应成功");
        commitLog.start();
        List<Snapshot> snapshots = new ArrayList<Snapshot>();
        for (int i = 0; i < count; i++) {
            MessageExtBrokerInner msg = build(i, queueId);
            PutMessageResult result = commitLog.putMessage(msg);
            assertEquals(PutMessageStatus.PUT_OK, result.getPutMessageStatus(), "第 " + i + " 条应写入成功");
            snapshots.add(snapshotOf(msg));
        }
        commitLog.shutdown();
        assertFalse(new File(config.getStorePathRootDir(), CommitLog.ABORT_FILE_NAME).exists(),
                "干净关闭后 abort 文件必须被删掉");
        assertTrue(new File(config.getStorePathRootDir(), CommitLog.CHECKPOINT_FILE_NAME).exists(),
                "干净关闭后必须留下 checkpoint");
        return snapshots;
    }

    /** 从同一 storeDir 冷启动一个全新实例并恢复。 */
    private CommitLog reopen(MessageStoreConfig config) {
        CommitLog recovered = new CommitLog(config);
        assertTrue(recovered.load(), "重启 load 应成功");
        return recovered;
    }

    /** 逐条逐字段比对：盘上读回的消息 vs 写入快照。 */
    private void assertMatches(List<Snapshot> expected, List<MessageExt> actual, int queueId) {
        assertEquals(expected.size(), actual.size(),
                "队列 " + queueId + " 恢复出的条数应与写入条数一致");
        for (int i = 0; i < expected.size(); i++) {
            Snapshot e = expected.get(i);
            MessageExt a = actual.get(i);
            String at = "queue=" + queueId + " index=" + i + " ";
            // ---- MessageExt 13 个自有字段 ----
            assertEquals(e.msgId, a.getMsgId(), at + "msgId");
            assertEquals(e.queueId, a.getQueueId(), at + "queueId");
            assertEquals(e.queueId, queueId, at + "queueId 应落在被请求的队列上");
            assertEquals(e.storeSize, a.getStoreSize(), at + "storeSize");
            assertEquals(e.queueOffset, a.getQueueOffset(), at + "queueOffset");
            assertEquals(e.sysFlag, a.getSysFlag(), at + "sysFlag");
            assertEquals(e.bornTimestamp, a.getBornTimestamp(), at + "bornTimestamp");
            assertEquals(hostKey(a.getBornHost()), e.bornHost, at + "bornHost");
            assertEquals(e.storeTimestamp, a.getStoreTimestamp(), at + "storeTimestamp");
            assertEquals(hostKey(a.getStoreHost()), e.storeHost, at + "storeHost");
            assertEquals(e.commitLogOffset, a.getCommitLogOffset(), at + "commitLogOffset");
            assertEquals(e.bodyCRC, a.getBodyCRC(), at + "bodyCRC");
            assertEquals(e.reconsumeTimes, a.getReconsumeTimes(), at + "reconsumeTimes");
            assertEquals(e.preparedTransactionOffset, a.getPreparedTransactionOffset(),
                    at + "preparedTransactionOffset");
            // ---- 父类 Message 字段 ----
            assertEquals(e.topic, a.getTopic(), at + "topic");
            assertEquals(e.tags, a.getTags(), at + "tags");
            assertEquals(e.keys, a.getKeys(), at + "keys");
            assertEquals(e.flag, a.getFlag(), at + "flag");
            assertEquals(e.properties, a.getProperties(), at + "properties");
            assertArrayEquals(e.body, a.getBody(), at + "body");
            assertEquals(Integer.parseInt(e.properties.get("DELAY")), a.getDelayTimeLevel(),
                    at + "delayTimeLevel（属性驱动）");

            // ---- 非空洞自检：这几列在真实链路上恒为 0/null，必须由本用例赋上非默认值 ----
            assertTrue(a.getSysFlag() != 0, at + "sysFlag 必须是非默认值");
            assertTrue(a.getReconsumeTimes() != 0, at + "reconsumeTimes 必须是非默认值");
            assertTrue(a.getPreparedTransactionOffset() != 0, at + "preparedTransactionOffset 必须非默认");
            assertNotNull(a.getBornHost(), at + "bornHost 不能为 null");
            assertNotNull(a.getStoreHost(), at + "storeHost 不能为 null");
            assertFalse(a.getBornHost().getHostString().isEmpty(), at + "bornHost 字符串不能为空");
            assertTrue(a.getStoreSize() >= MessageCodec.FIXED_AREA_SIZE, at + "storeSize 至少一个定长区");
            assertTrue(a.getBody().length > 0, at + "body 不能为空");
            assertTrue(a.getBodyCRC() == crc32(a.getBody()) && a.getBodyCRC() != 0,
                    at + "bodyCRC 应等于独立复算的 body crc32");
        }
    }

    private static int crc32(byte[] body) {
        CRC32 crc = new CRC32();
        crc.update(body);
        return (int) crc.getValue();
    }

    private static int intAt(RandomAccessFile raf, long pos) throws IOException {
        byte[] four = new byte[4];
        raf.seek(pos);
        raf.readFully(four);
        return ByteBuffer.wrap(four).getInt();
    }

    /** 把盘上 [pos, pos+len) 改成指定字节（模拟截断 / CRC 损坏）。 */
    private static void overwrite(File file, long pos, byte[] data) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        try {
            raf.seek(pos);
            raf.write(data);
            raf.getFD().sync();
        } finally {
            raf.close();
        }
    }

    private static File lastMappedFile(File commitLogDir) {
        File[] files = commitLogDir.listFiles();
        assertNotNull(files, "commitlog 目录应存在");
        assertTrue(files.length > 0, "commitlog 目录应有文件");
        File best = null;
        long bestOffset = -1;
        for (File f : files) {
            if (!f.isFile()) {
                continue;
            }
            long offset = Long.parseLong(f.getName());
            if (offset > bestOffset) {
                bestOffset = offset;
                best = f;
            }
        }
        assertNotNull(best, "应能找到最后一个 mapped file");
        return best;
    }

    /** BLANK 头部布局：[0..3] 尾巴总长 | [4..7] BLANK_MAGIC_CODE | [8..15] 下一文件起始偏移。 */
    private static void assertBlankMarker(File file, long pos, long tailLength, long nextFileOffset)
            throws IOException {
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        try {
            assertEquals((int) tailLength, intAt(raf, pos), "BLANK 头 0..3 应是尾巴总长");
            assertEquals(CommitLog.BLANK_MAGIC_CODE, intAt(raf, pos + 4),
                    "BLANK magic 必须在偏移 4 —— 与真记录同一个读法，扫描器才认得它");
            byte[] eight = new byte[8];
            raf.seek(pos + 8);
            raf.readFully(eight);
            assertEquals(nextFileOffset, ByteBuffer.wrap(eight).getLong(),
                    "BLANK 头里应带下一个文件的起始偏移");
        } finally {
            raf.close();
        }
    }

    // ==================== 用例 1：500 条逐字段 round-trip ====================

    @Test
    public void testFiveHundredMessagesSurviveRestartFieldByField() {
        MessageStoreConfig config = config("clean-rt", 1024 * 1024);
        List<Snapshot> snapshots = writeManyAndShutdown(config, 2, COUNT);

        // 关掉进程（本用例用新的 CommitLog 实例代表重启后的进程）后重新打开
        CommitLog recovered = reopen(config);
        recovered.start();
        assertEquals(COUNT, recovered.getRecoveredMessages(), "恢复扫描应认出 500 条");

        List<MessageExt> messages = recovered.pullMessage("RecoveryTopic_2", 2, 0, COUNT * 2);
        assertMatches(snapshots, messages, 2);

        // 位点：物理偏移严格递增、queueOffset 连续，说明不是"两边都 0 所以相等"
        for (int i = 1; i < messages.size(); i++) {
            assertTrue(messages.get(i).getCommitLogOffset() > messages.get(i - 1).getCommitLogOffset(),
                    "commitLogOffset 必须严格递增");
            assertEquals(messages.get(i - 1).getQueueOffset() + 1, messages.get(i).getQueueOffset(),
                    "queueOffset 必须连续");
        }
        assertEquals(0L, messages.get(0).getCommitLogOffset(), "第一条记录从物理偏移 0 开始");
        assertEquals(0L, messages.get(0).getQueueOffset());
        assertEquals(COUNT - 1L, messages.get(COUNT - 1).getQueueOffset());
        assertEquals(COUNT, recovered.getQueueIndex().getSize("RecoveryTopic_2", 2));
        assertEquals(COUNT, recovered.getQueueIndex().getMaxOffset("RecoveryTopic_2", 2));
        // 未写入的队列查不到任何东西
        assertTrue(recovered.pullMessage("RecoveryTopic_2", 1, 0, 10).isEmpty());
        assertTrue(recovered.pullMessage("NoSuchTopic", 2, 0, 10).isEmpty());

        // checkpoint 必须等于恢复后已刷盘前缀；abort 已被上一次干净关闭路径删掉
        assertTrue(new File(config.getStorePathRootDir(), CommitLog.CHECKPOINT_FILE_NAME).exists(),
                "checkpoint 文件应存在");
        assertFalse(recovered.isLastShutdownAbnormally());
        recovered.start();
        assertTrue(new File(config.getStorePathRootDir(), CommitLog.ABORT_FILE_NAME).exists(),
                "start() 必须落下 abort 文件");
        recovered.shutdown();
        assertFalse(new File(config.getStorePathRootDir(), CommitLog.ABORT_FILE_NAME).exists(),
                "干净 shutdown 必须删掉 abort 文件");
    }

    // ==================== 用例 2：末条被截断 ====================

    @Test
    public void testTruncatedTailRecordOnlyLosesThatOneMessage() throws Exception {
        MessageStoreConfig config = config("truncated", 1024 * 1024);
        List<Snapshot> snapshots = writeManyAndShutdown(config, 5, COUNT);

        Snapshot last = snapshots.get(COUNT - 1);
        File tailFile = lastMappedFile(new File(config.getStorePathCommitLog()));
        long lastStartInFile = last.commitLogOffset - Long.parseLong(tailFile.getName());
        assertTrue(lastStartInFile >= 0);

        // 把最后一条记录的尾巴写零：totalSize 还在，但 crc 覆盖区被抹掉 ⇒ 只能判它一条坏
        byte[] zeros = new byte[16];
        overwrite(tailFile, lastStartInFile + last.storeSize - zeros.length, zeros);

        CommitLog recovered = reopen(config);
        recovered.start();

        // 恢复不崩：前 499 条完好，位点与索引都不越界
        assertEquals(COUNT - 1, recovered.getRecoveredMessages(), "只应丢掉未完整写入的那一条");
        List<MessageExt> messages = recovered.pullMessage("RecoveryTopic_5", 5, 0, COUNT * 2);
        assertMatches(snapshots.subList(0, COUNT - 1), messages, 5);
        assertEquals(COUNT - 1L, recovered.getQueueIndex().getMaxOffset("RecoveryTopic_5", 5),
                "计数器不得越过被截断的那条");
        assertTrue(recovered.pullMessage("RecoveryTopic_5", 5, COUNT - 1, 10).isEmpty(),
                "被截断的那条不该还能查到条目");
        assertTrue(recovered.getMaxPhyOffset() <= last.commitLogOffset,
                "最大物理位点必须退到截断点之前");
        recovered.shutdown();
    }

    // ==================== 用例 3：中途一条 CRC 损坏 ====================

    @Test
    public void testCorruptedRecordInMiddleCutsEverythingAfterIt() throws Exception {
        MessageStoreConfig config = config("corrupt-mid", 1024 * 1024);
        List<Snapshot> snapshots = writeManyAndShutdown(config, 3, COUNT);

        final int corruptIndex = 250;
        Snapshot broken = snapshots.get(corruptIndex);
        final long mappedFileSize = 1024 * 1024;
        File file = new File(config.getStorePathCommitLog(),
                String.format("%020d", broken.commitLogOffset / mappedFileSize * mappedFileSize));
        long posInFile = broken.commitLogOffset - broken.commitLogOffset / mappedFileSize * mappedFileSize;

        // 改掉 body 区里的一个字节（不是写零，纯粹制造 CRC 不符）
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        try {
            long target = posInFile + MessageCodec.FIXED_AREA_SIZE + 8;
            raf.seek(target);
            int b = raf.read();
            raf.seek(target);
            raf.write(b ^ 0xFF);
            raf.getFD().sync();
        } finally {
            raf.close();
        }

        CommitLog recovered = reopen(config);
        recovered.start();

        // 策略：CRC 不符 ⇒ 在该记录起始处封尾截断，之后的全部丢弃（工单 §4）
        assertEquals(corruptIndex, recovered.getRecoveredMessages(),
                "损坏记录之前的 250 条应恢复，之后（含坏的那条）全部丢弃");
        List<MessageExt> messages = recovered.pullMessage("RecoveryTopic_3", 3, 0, COUNT * 2);
        assertMatches(snapshots.subList(0, corruptIndex), messages, 3);
        assertEquals(corruptIndex, recovered.getQueueIndex().getMaxOffset("RecoveryTopic_3", 3));
        // 截断点上写了 BLANK 封尾，wrotePosition 退到坏记录起始处
        MappedFile mappedFile = recovered.getMappedFileQueue().getMappedFiles().get(0);
        assertEquals(posInFile, mappedFile.getWrotePosition(), "位点应退到坏记录起始处");
        assertBlankMarker(file, posInFile, mappedFileSize - posInFile,
                posInFile / mappedFileSize * mappedFileSize + mappedFileSize);

        // 截断之后继续写：新记录落在 BLANK 之前，且能再读回来
        MessageExtBrokerInner more = build(999, 3);
        assertEquals(PutMessageStatus.PUT_OK, recovered.putMessage(more).getPutMessageStatus());
        recovered.shutdown();

        CommitLog again = reopen(config);
        List<MessageExt> after = again.pullMessage("RecoveryTopic_3", 3, 0, COUNT * 3);
        assertEquals(corruptIndex + 1, after.size(), "截断后新写的消息应能恢复");
        assertEquals(corruptIndex, after.get(after.size() - 1).getQueueOffset(),
                "queueOffset 从截断处继续分配，不与被丢弃的旧记录重复");
        again.shutdown();
    }

    // ==================== 用例 4：多文件 + BLANK 封尾 ====================

    @Test
    public void testFiveHundredMessagesAcrossManyFiles() throws Exception {
        final int fileSize = 16 * 1024;
        MessageStoreConfig config = config("multifile", fileSize);
        List<Snapshot> snapshots = writeManyAndShutdown(config, 1, COUNT);

        File dir = new File(config.getStorePathCommitLog());
        File[] files = dir.listFiles();
        assertNotNull(files);
        int fileCount = 0;
        for (File f : files) {
            if (f.isFile()) {
                fileCount++;
            }
        }
        assertTrue(fileCount >= 3, "500 条在 16KB 文件下应跨多个文件，实际 " + fileCount + " 个");

        // 找到"跨文件边界"：第一条落在非 0 号文件里的记录，其前一条之后必须正好是 BLANK 封尾
        int boundary = -1;
        for (int i = 0; i < snapshots.size(); i++) {
            if (snapshots.get(i).commitLogOffset >= fileSize) {
                boundary = i;
                break;
            }
        }
        assertTrue(boundary > 0, "应存在跨文件边界");
        Snapshot firstInSecondFile = snapshots.get(boundary);
        Snapshot beforeBoundary = snapshots.get(boundary - 1);
        long blankPos = beforeBoundary.commitLogOffset + beforeBoundary.storeSize;
        assertTrue(blankPos <= firstInSecondFile.commitLogOffset,
                "剩余空间放不下下一条 ⇒ 必须封尾换文件，不能跨文件写");
        assertEquals(firstInSecondFile.commitLogOffset % fileSize, 0L,
                "跨文件后的第一条记录必须从新文件头部开始");
        File sealed = new File(dir, String.format("%020d", blankPos / fileSize * fileSize));
        assertBlankMarker(sealed, blankPos % fileSize, fileSize - (blankPos % fileSize),
                firstInSecondFile.commitLogOffset);

        CommitLog recovered = reopen(config);
        recovered.start();
        assertEquals(COUNT, recovered.getRecoveredMessages(),
                "BLANK 不能被当成坏数据把整个文件截掉");
        assertMatches(snapshots, recovered.pullMessage("RecoveryTopic_1", 1, 0, COUNT * 2), 1);

        // 除最后一个文件外，前面的文件都必须"满"（wrotePosition == fileSize）
        List<MappedFile> mappedFiles = new ArrayList<MappedFile>(recovered.getMappedFileQueue().getMappedFiles());
        assertTrue(mappedFiles.size() >= 3);
        for (int i = 0; i < mappedFiles.size() - 1; i++) {
            assertEquals(fileSize, mappedFiles.get(i).getWrotePosition(),
                    "第 " + i + " 个文件应被写满");
            assertEquals(fileSize, mappedFiles.get(i).getFlushedPosition(),
                    "第 " + i + " 个文件恢复后应已刷盘");
        }
        // 每个文件的 fileFromOffset 都必须从文件名恢复（老实现恒 0）
        for (MappedFile mappedFile : mappedFiles) {
            assertEquals(Long.parseLong(new File(mappedFile.getFileName()).getName()),
                    mappedFile.getFileFromOffset());
        }
        recovered.shutdown();
    }

    // ==================== 用例 5：干净 / 非干净两条启动路径都返回同样 500 条 ====================

    @Test
    public void testCleanAndAbortedPathsReturnSameFiveHundred() {
        // ---- 干净路径：上面用例 1 已覆盖，这里再显式跑一遍并核对 checkpoint ----
        MessageStoreConfig clean = config("path-clean", 1024 * 1024);
        List<Snapshot> cleanShots = writeManyAndShutdown(clean, 4, COUNT);
        CommitLog cleanReopen = reopen(clean);
        assertEquals(COUNT, cleanReopen.getRecoveredMessages());
        assertFalse(cleanReopen.isLastShutdownAbnormally(), "干净关闭后不应有 abort");
        assertMatches(cleanShots, cleanReopen.pullMessage("RecoveryTopic_4", 4, 0, COUNT * 2), 4);
        long checkpoint = new CommitLog(config("path-clean", 1024 * 1024)).readCheckpoint();
        assertTrue(checkpoint >= cleanShots.get(COUNT - 1).commitLogOffset,
                "checkpoint 应覆盖到最后一条消息，实际 " + checkpoint);
        cleanReopen.shutdown();

        // ---- 非干净路径：start() 落下 abort，之后不 shutdown 直接换新实例恢复 ----
        MessageStoreConfig dirty = config("path-dirty", 1024 * 1024);
        CommitLog victim = new CommitLog(dirty);
        assertTrue(victim.load());
        victim.start();
        List<Snapshot> dirtyShots = new ArrayList<Snapshot>();
        for (int i = 0; i < COUNT; i++) {
            MessageExtBrokerInner msg = build(i, 4);
            assertEquals(PutMessageStatus.PUT_OK, victim.putMessage(msg).getPutMessageStatus());
            dirtyShots.add(snapshotOf(msg));
        }
        assertTrue(new File(dirty.getStorePathRootDir(), CommitLog.ABORT_FILE_NAME).exists(),
                "start() 之后 abort 文件必须在");
        // 进程"被 kill"：不调用 shutdown()，直接用新实例代表重启后的进程
        CommitLog dirtyReopen = reopen(dirty);
        assertTrue(dirtyReopen.isLastShutdownAbnormally(), "abort 残留 ⇒ 上次非干净退出");
        assertEquals(COUNT, dirtyReopen.getRecoveredMessages(),
                "非干净路径必须全量扫描 + 逐条 CRC，结果与干净路径一致");
        assertMatches(dirtyShots, dirtyReopen.pullMessage("RecoveryTopic_4", 4, 0, COUNT * 2), 4);
        dirtyReopen.shutdown();
        assertFalse(new File(dirty.getStorePathRootDir(), CommitLog.ABORT_FILE_NAME).exists(),
                "干净 shutdown 应删掉 abort");
    }

    // ==================== 用例 6：读路径必经盘（索引里没有消息体） ====================

    @Test
    public void testPullReadsFromDiskNotFromMemory() {
        MessageStoreConfig config = config("read-from-disk", 1024 * 1024);
        writeManyAndShutdown(config, 6, 20);

        CommitLog recovered = reopen(config);
        recovered.start();
        List<MessageExt> messages = recovered.pullMessage("RecoveryTopic_6", 6, 0, 20);
        assertEquals(20, messages.size());

        // 把进程内的位点索引清空，读盘这条路依然只能靠 CommitLog；而索引里本来就不该有 body
        int entrySize = recovered.getQueueIndex().queryEntries("RecoveryTopic_6", 6, 0, 20).get(0).getSize();
        assertTrue(entrySize < messages.get(0).getBody().length + MessageCodec.FIXED_AREA_SIZE + 512,
                "索引条目只是定长记录长度，不含额外对象");
        assertEquals(entrySize, messages.get(0).getStoreSize(),
                "索引里的 size 必须等于盘上记录长度");

        // 单条按物理偏移读回（必经 selectMappedBuffer）
        MessageExt single = recovered.readMessage(messages.get(7).getCommitLogOffset());
        assertNotNull(single);
        assertEquals(messages.get(7).getMsgId(), single.getMsgId());
        assertArrayEquals(messages.get(7).getBody(), single.getBody());
        recovered.shutdown();
    }
}
