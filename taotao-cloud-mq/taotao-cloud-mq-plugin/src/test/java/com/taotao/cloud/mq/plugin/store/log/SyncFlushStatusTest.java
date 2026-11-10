package com.taotao.cloud.mq.plugin.store.log;

import com.taotao.cloud.mq.common.message.MessageExt;
import com.taotao.cloud.mq.plugin.store.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 同步刷盘超时必须产生 {@link PutMessageStatus#FLUSH_DISK_TIMEOUT}（工单 T2）.
 * <p>
 * 这条守卫不许是"状态码存在"的断言 —— 那正是本仓历史上"广告了但没接线"的形状。它量的是因果：
 * <ol>
 *   <li>刷盘<b>真的</b>失败（force 之前把 FileChannel 关掉 ⇒ {@code MappedFile.flush} 走
 *       {@code channel not available} 分支返回 false，{@code flushedPosition} 不推进，
 *       {@code GroupCommitService} 因此不会 {@code wakeupCustomer}）；</li>
 *   <li>在这样的前提下 {@code putMessage} 返回的状态<b>就是</b> FLUSH_DISK_TIMEOUT 且<b>不是</b> PUT_OK；</li>
 *   <li>反向对照：刷盘健康时同一条路必须还是 PUT_OK（否则这条守卫等于"把一切报成超时"）。</li>
 * </ol>
 * {@code syncFlushTimeout} 用 {@code MessageStoreConfig} 的默认 5000ms，不调大也不调小 ——
 * 失败是结构性的（永远不会有人唤醒这个请求），不是靠超时窗口蒙出来的。
 */
public class SyncFlushStatusTest {

    @TempDir
    Path tempDir;

    private CommitLog commitLog;

    @AfterEach
    public void tearDown() {
        if (commitLog != null) {
            commitLog.shutdown();
            commitLog = null;
        }
        deleteRecursively(tempDir.toFile());
    }

    // ==================== 对照：同步刷盘成功 ⇒ PUT_OK，且 flushedPosition 真的推进 ====================

    @Test
    public void testSyncFlushSuccessStillReportsPutOk() {
        MessageStoreConfig config = config("sync-flush-ok");
        config.setFlushDiskType(FlushDiskType.SYNC_FLUSH);
        assertEquals(5000, config.getSyncFlushTimeout(), "不许改 syncFlushTimeout 当尺子");

        commitLog = new CommitLog(config);
        assertTrue(commitLog.load());
        commitLog.start();

        MessageExtBrokerInner msg = build("SyncOkTopic");
        PutMessageResult result = commitLog.putMessage(msg);
        assertEquals(PutMessageStatus.PUT_OK, result.getPutMessageStatus(),
                "刷盘成功时必须还是 PUT_OK —— 否则这条守卫只是把所有写入报成超时");
        assertTrue(result.isOk());

        MappedFile last = commitLog.getMappedFileQueue().peekLastMappedFile();
        assertEquals(last.getWrotePosition(), last.getFlushedPosition(),
                "同步刷盘成功后 flushedPosition 必须推到 wrotePosition（证明上面的 PUT_OK 不是白给的）");
        assertTrue(last.getFlushedPosition() > 0, "刷盘位点必须真的动过");
    }

    // ==================== 主证：刷盘失败 ⇒ FLUSH_DISK_TIMEOUT，且不是 PUT_OK ====================

    @Test
    public void testFlushFailureIsReportedAsFlushDiskTimeout() throws Exception {
        MessageStoreConfig config = config("sync-flush-timeout");
        config.setFlushDiskType(FlushDiskType.SYNC_FLUSH);
        assertEquals(5000, config.getSyncFlushTimeout(), "不许调大 syncFlushTimeout 过关");

        commitLog = new CommitLog(config);
        assertTrue(commitLog.load());
        commitLog.start();

        // 先写一条健康的，确认 flushedPosition 已经在动
        assertEquals(PutMessageStatus.PUT_OK, commitLog.putMessage(build("SyncTimeoutTopic"))
                .getPutMessageStatus());

        // 毁掉刷盘能力：关掉 mapped 区的文件通道 ⇒ force 这条路再也走不通
        MappedFile victim = commitLog.getMappedFileQueue().peekLastMappedFile();
        final int flushedBefore = victim.getFlushedPosition();
        victim.getFileChannel().close();
        assertFalse(victim.getFileChannel().isOpen(), "前置条件：通道必须真的关掉了，否则这条用例什么都没测");

        // 再写一条：记录进得了存储（mmap 还在），但落不了盘
        MessageExtBrokerInner doomed = build("SyncTimeoutTopic");
        PutMessageResult result = commitLog.putMessage(doomed);

        // (a) 状态码真的产生了，(b) 而且不再是 PUT_OK
        assertEquals(PutMessageStatus.FLUSH_DISK_TIMEOUT, result.getPutMessageStatus(),
                "刷盘没成却报 PUT_OK = 盘没落住而客户端显示成功（T2 要修的就是这个）");
        assertFalse(result.isOk(), "FLUSH_DISK_TIMEOUT 绝不能被 isOk() 认成成功");
        assertFalse(result.getPutMessageStatus() == PutMessageStatus.PUT_OK);

        // 因果自检：刷盘确实没成（flushedPosition 一步没动），而记录确实进了存储
        assertEquals(flushedBefore, victim.getFlushedPosition(),
                "前置条件：这条消息确实没落住盘，FLUSH_DISK_TIMEOUT 不是凭空报的");
        assertNotNull(result.getAppendMessageResult(), "响应里要带着写入结果，客户端才知道落在哪");
        assertEquals(AppendMessageResult.AppendMessageStatus.PUT_OK,
                result.getAppendMessageResult().getStatus(), "记录本身是写进存储了的，失败的只是落盘");

        // 反向对照：健康的写入路径不受影响（前一条已落盘的记录仍读得回）
        List<MessageExt> back = commitLog.pullMessage("SyncTimeoutTopic", 0, 0, 10);
        assertEquals(2, back.size(), "两条都在存储里（一条落住盘、一条没落住），读路径不受状态码影响");
    }

    // ==================== 对照：异步刷盘不等待，不该冒出 FLUSH_DISK_TIMEOUT ====================

    @Test
    public void testAsyncFlushPathUnaffected() {
        MessageStoreConfig config = config("async-flush");
        assertEquals(FlushDiskType.ASYNC_FLUSH, config.getFlushDiskType(), "默认就是异步刷盘");

        commitLog = new CommitLog(config);
        assertTrue(commitLog.load());
        commitLog.start();
        PutMessageResult result = commitLog.putMessage(build("AsyncTopic"));
        assertEquals(PutMessageStatus.PUT_OK, result.getPutMessageStatus(),
                "异步刷盘不等待落盘结果，状态码不许被这条改动带偏");
    }

    // ==================== 工具 ====================

    private MessageStoreConfig config(String sub) {
        MessageStoreConfig config = new MessageStoreConfig();
        // rootDir 与 storePathCommitLog 都要显式设进 TempDir，只设 rootDir 会逃逸到 ~/store/commitlog
        config.setStorePathRootDir(tempDir.resolve(sub).toString());
        config.setStorePathCommitLog(tempDir.resolve(sub + "/commitlog").toString());
        config.setMappedFileSizeCommitLog(1024 * 1024);
        return config;
    }

    private MessageExtBrokerInner build(String topic) {
        MessageExtBrokerInner msg = new MessageExtBrokerInner();
        msg.setTopic(topic);
        msg.setMsgId("SYNC-" + UUID.randomUUID());
        msg.setQueueId(0);
        msg.setSysFlag(7);
        msg.setFlag(11);
        msg.setBornTimestamp(1700000000000L);
        msg.setBornHost(new InetSocketAddress("10.1.2.3", 4001));
        msg.setStoreHost(new InetSocketAddress("192.168.7.8", 5001));
        msg.setReconsumeTimes(2);
        msg.setPreparedTransactionOffset(999L);
        msg.setTags("TAG-SYNC");
        msg.setKeys("KEY-SYNC");
        msg.setBody("sync-flush-payload".getBytes(StandardCharsets.UTF_8));
        return msg;
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
            System.err.println("[sync-flush] leftover (still mapped): " + file.getAbsolutePath());
        }
    }
}
