package com.taotao.cloud.mq.consistency.ttc.broker.integration;

import com.taotao.cloud.mq.consistency.ttc.broker.BrokerConfig;
import com.taotao.cloud.mq.consistency.ttc.broker.BrokerController;
import com.taotao.cloud.mq.consistency.ttc.broker.processor.PullMessageProcessor;
import com.taotao.cloud.mq.consistency.ttc.broker.processor.SendMessageProcessor;
import com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt;
import com.taotao.cloud.mq.consistency.ttc.common.protocol.PullResultPayload;
import com.taotao.cloud.mq.consistency.ttc.common.protocol.SendResult;
import com.taotao.cloud.mq.consistency.ttc.common.protocol.SendStatus;
import com.taotao.cloud.mq.consistency.ttc.common.util.JsonCodec;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyServerConfig;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RequestCode;
import com.taotao.cloud.mq.consistency.ttc.store.MessageStoreConfig;
import com.taotao.cloud.mq.consistency.ttc.store.log.CommitLog;
import com.taotao.cloud.mq.consistency.ttc.store.log.FlushDiskType;
import com.taotao.cloud.mq.consistency.ttc.store.log.MappedFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 交付物第 2 项：<b>真 shutdown、真重新 load，已确认的消息读得回来</b>（工单 T3）。
 * <p>
 * 与 {@code CommitLogRecoveryTest}（存储层单测）的分工不同，这里走的是 broker 的真实链路：
 * <ol>
 *   <li>{@link BrokerController#initialize()} + {@link BrokerController#start()} 起一个真 broker
 *       （真绑端口、真起刷盘线程、真落 abort 文件）；</li>
 *   <li>用真 {@link SendMessageProcessor} 发 N 条并<b>逐条收下 broker 的确认</b>
 *       （响应体 {@code SendStatus.SEND_OK}）；策略钉在 {@code SYNC_FLUSH} ——
 *       在该策略下确认意味着 {@code force()} 真的成功过，"已确认"才是有分量的词；</li>
 *   <li>两种终止方式各测一遍：
 *       (a) {@link BrokerController#shutdown()} 干净退出（abort 被删、checkpoint 落住）；
 *       (b) <b>掉电</b>：不调用任何 shutdown，直接把这个进程内实例丢掉 ——
 *       abort 文件原样留在盘上，checkpoint 停在上一次落住的位置；</li>
 *   <li>换一个<b>全新的 BrokerController 实例</b>（同一路径、代表重启后的进程）重新
 *       {@code initialize()}，走真 {@link PullMessageProcessor} 把消息读回来，
 *       并按 {@code msgId / topic / tags / keys / flag / body / queueOffset / queueId /
 *       bornTimestamp / reconsumeTimes} 逐字段比对。</li>
 * </ol>
 * 位点单独自证：queueOffset 必须从 0 连续到 N-1、commitLogOffset 严格递增、
 * {@code getRecoveredMessages()} 必须正好是 N —— 否则"读得回来"可能只是读了个前缀。
 * <p>
 * 存储路径：{@code storePathRootDir} 与 {@code storePathCommitLog} <b>都</b>显式设进 {@code @TempDir}，
 * 收尾递归删掉（只设 rootDir 会让 commitlog 逃逸到 {@code ~/store/commitlog}，本仓历史上留过 1.0G 残渣）。
 * 端口用 {@code new ServerSocket(0)} 现问现用，不占固定端口。
 */
public class RestartDurabilityTest {

    private static final int MESSAGE_COUNT = 100;
    private static final String TOPIC = "RestartDurabilityTopic";
    private static final int QUEUE_ID = 3;

    @TempDir
    Path tempDir;

    private final List<BrokerController> launched = new ArrayList<>();

    @AfterEach
    public void tearDown() {
        // 掉电用例故意不把第一个 broker 关干净；收尾时统一收尸，只清线程与映射，不再影响断言
        for (BrokerController broker : launched) {
            try {
                broker.shutdown();
            } catch (Exception ignore) {
                // 掉电用例里通道可能已被自己的 shutdown 关掉，忽略
            }
        }
        launched.clear();
        deleteRecursively(tempDir.toFile());
    }

    // ==================== 用例 A：干净 shutdown ⇒ 重启后已确认的消息一条不少 ====================

    @Test
    public void acknowledgedMessagesSurviveCleanShutdownAndRealReload() throws Exception {
        String sub = "clean-restart";
        BrokerController first = startBroker(sub, FlushDiskType.SYNC_FLUSH);
        File rootDir = new File(storeRoot(sub));
        List<Written> written;

        try {
            written = sendAndAcknowledge(first, "ack-clean");
            assertEquals(MESSAGE_COUNT, written.size());

            // 第一步写消息之后才该问"有没有 mapped file"——写路径懒建首文件（见 startBroker 注释）。
            assertWritePathHasCreatedMappedFile(first, "干净用例首批 SEND_OK 之后");

            CommitLog log1 = first.getCommitLog();
            MappedFile last1 = log1.getMappedFileQueue().peekLastMappedFile();
            assertEquals(last1.getWrotePosition(), last1.getFlushedPosition(),
                    "SYNC_FLUSH 下确认过的记录必须已经 force 到盘（否则这条用例什么都没保证）");
            assertTrue(new File(rootDir, CommitLog.ABORT_FILE_NAME).exists(),
                    "start() 之后 abort 文件必须在");
            assertTrue(log1.getMappedFileQueue().getFlushedOffset() > 0,
                    "已刷位点必须真的动过");
        } finally {
            first.shutdown();
            launched.remove(first);
        }

        // 干净关闭的盘上形状：abort 被删掉，checkpoint 留在最后刷盘位点
        assertFalse(new File(rootDir, CommitLog.ABORT_FILE_NAME).exists(),
                "BrokerController.shutdown() 必须走 CommitLog.shutdown()：flush + checkpoint + 删 abort");
        assertTrue(new File(rootDir, CommitLog.CHECKPOINT_FILE_NAME).exists(),
                "干净关闭必须留下 checkpoint 文件");

        // ---- 真重新 load：全新实例（代表重启后的进程）----
        BrokerController second = startBroker(sub, FlushDiskType.SYNC_FLUSH);
        assertFalse(second.getCommitLog().isLastShutdownAbnormally(),
                "干净关闭后重启不应被判成异常退出");
        assertEquals(MESSAGE_COUNT, second.getCommitLog().getRecoveredMessages(),
                "恢复扫描必须认出全部已确认记录");

        List<MessageExt> reread = pullThroughProcessor(second, MESSAGE_COUNT);
        assertSameFields(written, reread);

        // 队列索引也是重启后重建的，不是进程内残留
        CommitLog log2 = second.getCommitLog();
        assertEquals(MESSAGE_COUNT, log2.getQueueIndex().getSize(TOPIC, QUEUE_ID),
                "重启后队列索引条数");
        assertEquals(MESSAGE_COUNT, log2.getQueueIndex().getMaxOffset(TOPIC, QUEUE_ID),
                "重启后队列最大位点必须正好是 N（下一条该写 100）");

        // 重启后再发一条并确认：说明恢复出来的位点是"能继续写"的活位点，不是只读快照
        sendOneAndAck(second, "post-restart", MESSAGE_COUNT);
        assertEquals(MESSAGE_COUNT + 1L, log2.getQueueIndex().getMaxOffset(TOPIC, QUEUE_ID),
                "队列位点必须从恢复出来的位置继续递增");
        assertEquals(MESSAGE_COUNT + 1,
                log2.pullMessage(TOPIC, QUEUE_ID, 0, MESSAGE_COUNT + 2).size(),
                "重启后的新确认记录与恢复出来的记录一起读得回来");
        assertEquals(MESSAGE_COUNT, log2.getRecoveredMessages(),
                "getRecoveredMessages 只反映 load 那一刻的条数，不许被后续写入污染");

        second.shutdown();
        launched.remove(second);
        assertFalse(new File(rootDir, CommitLog.ABORT_FILE_NAME).exists());
    }

    // ==================== 用例 B：掉电（无 shutdown）⇒ 已确认的消息仍读得回来 ====================

    @Test
    public void acknowledgedMessagesSurvivePowerLossAndRealReload() throws Exception {
        String sub = "power-loss";
        BrokerController first = startBroker(sub, FlushDiskType.SYNC_FLUSH);
        File rootDir = new File(storeRoot(sub));

        List<Written> written = sendAndAcknowledge(first, "ack-crash");

        // 掉电前置条件之一：写路径已经把首文件建出来并写进了字节（见 startBroker 注释）。
        assertWritePathHasCreatedMappedFile(first, "掉电用例首批 SEND_OK 之后");

        CommitLog log1 = first.getCommitLog();
        MappedFile last1 = log1.getMappedFileQueue().peekLastMappedFile();
        assertEquals(last1.getWrotePosition(), last1.getFlushedPosition(),
                "掉电前置条件：确认过的记录都已 force，掉电掉的是未确认的部分");
        assertTrue(new File(rootDir, CommitLog.ABORT_FILE_NAME).exists(),
                "掉电前置条件：还在运行中的 abort 文件");

        // ---- 掉电：不调用 shutdown()，直接把实例丢掉（abort 留在盘上、checkpoint 停在旧值）----
        // 之后由 tearDown 收尸，确保测试结束后不遗留线程与映射。
        final long flushedBeforeCrash = log1.getMappedFileQueue().getFlushedOffset();

        BrokerController second = startBroker(sub, FlushDiskType.SYNC_FLUSH);
        CommitLog log2 = second.getCommitLog();
        assertTrue(log2.isLastShutdownAbnormally(),
                "abort 残留 ⇒ 必须被判成上次非干净退出（掉电的判定依据）");
        assertEquals(MESSAGE_COUNT, log2.getRecoveredMessages(),
                "即便被判为掉电，恢复扫描也要把已确认的记录全认回来");

        List<MessageExt> reread = pullThroughProcessor(second, MESSAGE_COUNT);
        assertSameFields(written, reread);

        // 掉电重启后 checkpoint 文件仍在，且没有把已落盘前缀说小
        assertTrue(new File(rootDir, CommitLog.CHECKPOINT_FILE_NAME).exists(),
                "第一次生命周期的位点必须已进 checkpoint（SYNC_FLUSH 的确认就是 force 成功）");
        assertTrue(log2.getMappedFileQueue().getFlushedOffset() >= flushedBeforeCrash,
                "重启后已刷位点不许倒退");

        // 掉电重启后再确认一条，然后干净退出 —— 证明恢复出来的实例是可用的
        sendOneAndAck(second, "post-crash", MESSAGE_COUNT);
        assertEquals(MESSAGE_COUNT + 1L, log2.getQueueIndex().getMaxOffset(TOPIC, QUEUE_ID));
        second.shutdown();
        launched.remove(second);
        assertFalse(new File(rootDir, CommitLog.ABORT_FILE_NAME).exists(),
                "重启后这一次退出走干净路径，abort 必须被删掉");
    }

    // ==================== 写入 / 确认 / 读回 ====================

    /** 一条"已被 broker 确认"的记录，连同它在客户端侧看到的字段。 */
    private static final class Written {
        final String msgId;
        final String tags;
        final String keys;
        final int flag;
        final long bornTimestamp;
        final byte[] body;
        final long queueOffset;

        Written(String msgId, String tags, String keys, int flag, long bornTimestamp,
                byte[] body, long queueOffset) {
            this.msgId = msgId;
            this.tags = tags;
            this.keys = keys;
            this.flag = flag;
            this.bornTimestamp = bornTimestamp;
            this.body = body;
            this.queueOffset = queueOffset;
        }
    }

    /** 逐条发送并<b>要求 broker 逐条确认</b>；任何一条没拿到 SEND_OK 就直接红。 */
    private List<Written> sendAndAcknowledge(BrokerController broker, String idPrefix) throws Exception {
        List<Written> written = new ArrayList<>();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            written.add(sendOneAndAck(broker, idPrefix, i));
        }
        return written;
    }

    /**
     * 经过真实的 {@link SendMessageProcessor} 发一条，并把响应体里的 {@code SendStatus} 当作"确认"来看。
     * 传输层码 {@code SUCCESS} + 响应体 {@code SEND_OK} 才是已确认；{@code FLUSH_DISK_TIMEOUT} 一律算失败
     * （见 T2：那条状态码不再被当成成功）。
     */
    private Written sendOneAndAck(BrokerController broker, String idPrefix, int i) throws Exception {
        String msgId = "RT-" + idPrefix + "-" + i;
        String tags = "TAG-" + (i % 7);
        String keys = "KEY-" + i;
        int flag = 11 + i;
        long bornTimestamp = 1700000000000L + i;
        byte[] body = ("restart-payload-" + i + "-" + idPrefix + "-some-realistic-bytes")
                .getBytes(StandardCharsets.UTF_8);

        MessageExt inbound = new MessageExt();
        inbound.setTopic(TOPIC);
        inbound.setMsgId(msgId);
        inbound.setTags(tags);
        inbound.setKeys(keys);
        inbound.setFlag(flag);
        inbound.setBornTimestamp(bornTimestamp);
        inbound.setBody(body);

        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE);
        req.addExtField("topic", TOPIC);
        req.addExtField("queueId", String.valueOf(QUEUE_ID));
        req.addExtField("brokerName", broker.getBrokerConfig().getBrokerName());
        req.setBody(JsonCodec.encode(inbound));

        SendMessageProcessor processor = new SendMessageProcessor(broker);
        RemotingCommand resp = processor.processRequest(null, req);
        assertEquals(RemotingSysResponseCode.SUCCESS, resp.getCode(), "传输层必须应答");
        SendResult sr = JsonCodec.decode(resp.getBody(), SendResult.class);
        assertNotNull(sr, "响应体必须能解出 SendResult");
        assertEquals(SendStatus.SEND_OK, sr.getSendStatus(),
                "第 " + i + " 条必须被 broker 确认为 SEND_OK（FLUSH_DISK_TIMEOUT/SEND_FAILED 都算这条用例失败）");

        return new Written(msgId, tags, keys, flag, bornTimestamp, body, sr.getQueueOffset());
    }

    /** 经过真实的 {@link PullMessageProcessor} 读回（响应体是 {@link PullResultPayload}）。 */
    private List<MessageExt> pullThroughProcessor(BrokerController broker, int maxNum) throws Exception {
        PullMessageProcessor processor = new PullMessageProcessor(broker);
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.PULL_MESSAGE);
        req.addExtField("topic", TOPIC);
        req.addExtField("queueId", String.valueOf(QUEUE_ID));
        req.addExtField("offset", "0");
        req.addExtField("maxNum", String.valueOf(maxNum));

        RemotingCommand resp = processor.processRequest(null, req);
        assertEquals(RemotingSysResponseCode.SUCCESS, resp.getCode());
        PullResultPayload payload = JsonCodec.decode(resp.getBody(), PullResultPayload.class);
        assertNotNull(payload, "拉取响应体必须能解出 PullResultPayload");
        assertEquals(TOPIC, payload.getTopic());
        assertEquals(QUEUE_ID, payload.getQueueId());
        return payload.getMessages() == null ? new ArrayList<MessageExt>() : payload.getMessages();
    }

    // ==================== 断言：逐字段 ====================

    private void assertSameFields(List<Written> expected, List<MessageExt> actual) {
        assertEquals(expected.size(), actual.size(),
                "重启后读回的条数必须等于已确认条数（少了就是掉电丢数据）");
        for (int i = 0; i < expected.size(); i++) {
            Written e = expected.get(i);
            MessageExt a = actual.get(i);
            String at = "index=" + i + " ";
            assertEquals(e.msgId, a.getMsgId(), at + "msgId");
            assertEquals(TOPIC, a.getTopic(), at + "topic");
            assertEquals(QUEUE_ID, a.getQueueId(), at + "queueId");
            assertEquals(e.tags, a.getTags(), at + "tags");
            assertEquals(e.keys, a.getKeys(), at + "keys");
            assertEquals(e.flag, a.getFlag(), at + "flag");
            assertArrayEquals(e.body, a.getBody(), at + "body");
            assertEquals(e.bornTimestamp, a.getBornTimestamp(), at + "bornTimestamp");
            // 确认时给客户的位点必须就是重启后读回来的队列位点。这条钉的是 src/main 的一处真缺陷
            // （本测试抓到、09-26 已修）：broker 一度把 commitLog 的【物理】位点当成队列位点回给客户 ——
            // 站点是 SendMessageProcessor / TransactionMessageProcessor 里的
            // `sendResult.setQueueOffset(amr.getWroteOffset())`，而 wroteOffset 就是 MappedFile:214-215
            // 的 `fileFromOffset + expectedPos`；真队列位点在 CommitLog:309-310 分配、写在同一条消息对象上。
            // 下面那条 "== i 连续递增" 是绿的，所以本条红的时候可以直接断定回错了字段。
            // 反证：把 :90 还原成 `amr.getWroteOffset()` ⇒ 两例各自点名红（acked == rereadCommitLogOffset）。
            assertEquals(e.queueOffset, a.getQueueOffset(), at + "queueOffset（确认时给客户的位点）"
                    + " acked=" + e.queueOffset + " rereadQueueOffset=" + a.getQueueOffset()
                    + " rereadCommitLogOffset=" + a.getCommitLogOffset()
                    + "；acked == commitLogOffset 即 broker 回错了字段");
            assertTrue(a.getCommitLogOffset() >= 0, at + "commitLogOffset 必须由重启后的扫描给出");
            assertTrue(a.getStoreSize() > 0, at + "storeSize 必须是非默认值");
            // bodyCRC 在本仓是【有符号 int】：UtilAll.crc32 在 CommitLog.java:780-786（包私有类）
            // 返回 `(int) java.util.zip.CRC32#getValue()`，所以一条合法算出的 CRC 完全可能 < 0。
            // 原来这条 `getBodyCRC() > 0` 把 CRC 当成了无符号数，实测是假红：
            // w1t3_after_fix.log —— 干净用例 index=0 红（该 body 的 CRC 最高位为 1）、
            // 掉电用例 index=0 反而绿（它那个 body 字符串的 CRC 恰好为正），差的只是符号位。
            // 判据换成与姊妹测试 CommitLogRecoveryTest:206 同一口径、且严格更强的两条：
            // 独立复算的 CRC 必须逐位等于重启后读回的值（这比"大于 0"强得多），且不许是没算过的 0。
            assertEquals(crc32Of(e.body), a.getBodyCRC(),
                    at + "bodyCRC 必须等于独立复算的 body crc32（重启后的逐条校验给的就是这个值）");
            assertTrue(a.getBodyCRC() != 0, at + "bodyCRC 不许是没算过的默认值 0");
            assertTrue(a.getStoreTimestamp() > 0, at + "storeTimestamp 由 broker 侧写入，必须非默认");

            // 非空洞自检：位点必须真的连续，否则"读回 100 条"可能是同一批被数了两遍
            assertEquals((long) i, a.getQueueOffset(), at + "queueOffset 必须从 0 连续递增");
            if (i > 0) {
                assertTrue(a.getCommitLogOffset() > actual.get(i - 1).getCommitLogOffset(),
                        at + "commitLogOffset 必须严格递增");
            }
        }
        assertEquals(MESSAGE_COUNT - 1L, actual.get(actual.size() - 1).getQueueOffset(),
                "最后一条的队列位点必须是 N-1");
    }

    // ==================== broker 生命周期 ====================

    /**
     * 起一个真 broker：{@code initialize()}（真 load/恢复）+ {@code start()}（真绑端口、真起刷盘线程、
     * 真落 abort）。不配 namesrv —— 本用例测的是存储生命周期，不是路由。
     */
    private BrokerController startBroker(String sub, FlushDiskType flushDiskType) throws Exception {
        MessageStoreConfig msc = new MessageStoreConfig();
        msc.setStorePathRootDir(storeRoot(sub));
        msc.setStorePathCommitLog(storeRoot(sub) + File.separator + "commitlog");
        msc.setMappedFileSizeCommitLog(1024 * 1024);
        msc.setFlushDiskType(flushDiskType);

        BrokerConfig bc = new BrokerConfig();
        bc.setBrokerName("RestartBroker");
        bc.setBrokerClusterName("RestartCluster");
        bc.setNamesrvAddr("");

        NettyServerConfig nsc = new NettyServerConfig();
        nsc.setListenPort(freePort());

        // 起 broker <b>之前</b>先量一次盘上现状：这决定下面那条守卫朝哪个方向断言。
        boolean firstLaunch = isStoreDirEmpty(msc.getStorePathCommitLog());

        BrokerController broker = new BrokerController(bc, msc, nsc);
        assertTrue(broker.initialize(), "BrokerController.initialize() 必须成功（真 load）");
        broker.start();
        launched.add(broker);

        // 守卫是【双向】的，方向由"起 broker 之前盘上有没有文件"决定 —— 本方法被两种生命周期各调一次：
        // 首次启动（空存储）与重启（盘上已有消息）。原来那一条单向的 size() > 0 在两种形状里都只
        // 可能在其中一种成立，所以它对空存储必然是假红（实测：w1t3_repro_before2.log 两例全死在本行前身）。
        //
        // 本仓口径（把设计钉住，不是迁就现状）：空存储刚 load()+start() 之后 mapped file 数就是 0，
        // 首文件由【写路径】懒建，理由三条，都在 src/main 里逐条实测过：
        //   1) 建文件的唯一站点是 MappedFileQueue.getLastMappedFile(startOffset, createIfNotExists)
        //      的 :186 分支（:198 new MappedFile(...)），而 src/main 里唯一走到它的调用者是
        //      CommitLog 的写入路径 :315 `getLastMappedFile()` ⇒ :214-215 `getLastMappedFile(0, true)`；
        //   2) 启动路径上没人调它：CommitLog.load():146 只调 mappedFileQueue.load()，而
        //      MappedFileQueue.load():58-64 目录不存在时只 mkdirs() 就返回，盘上无文件时一个都不建；
        //      CommitLog.start():115-119 只做 createAbortFile() + 起刷盘线程；
        //   3) MappedFileQueue:329 的注释明说"不创建文件：getMaxOffset 老实现走 getLastMappedFile()，
        //      会在读的时候顺手建文件"—— 也就是说"不在读路径顺手建文件"是刻意维持的不变式。
        // ⇒ 哪天有人让 load()/start() 顺手建首文件，下面这条 assertEquals 会红，逼他表态。
        int files = broker.getCommitLog().getMappedFileQueue().getMappedFiles().size();
        if (firstLaunch) {
            assertEquals(0, files,
                    "空存储 load()+start() 后必须 0 个 mapped file（首文件是写路径懒建的，见本行上方注释）");
        } else {
            assertTrue(files > 0,
                    "重启必须把盘上已有的 mapped file 加载回来（load() 只扫盘，实测 files=" + files + "）");
        }
        return broker;
    }

    /**
     * 第一条消息被确认写下去之后，检查"写路径真的把首文件建出来并写进了字节"。
     * <p>
     * 这才是原守卫（{@code start() 后必须真有 mapped file}）想说而说不出来的意思：它落在一个可证的
     * 事实（写路径建文件）上，而不是落在一个本仓根本不成立的启动不变式上。两条都要：
     * 只建文件没写字节、或写了字节却没进队列，都是坏。
     */
    private void assertWritePathHasCreatedMappedFile(BrokerController broker, String at) {
        int files = broker.getCommitLog().getMappedFileQueue().getMappedFiles().size();
        assertTrue(files > 0, at + "：成功发送之后写路径必须已懒建出 mapped file（files=" + files + "）");
        MappedFile last = broker.getCommitLog().getMappedFileQueue().peekLastMappedFile();
        assertNotNull(last, at + "：getMappedFiles() 非空而 peekLastMappedFile() 为 null，队列自相矛盾");
        assertTrue(last.getWrotePosition() > 0,
                at + "：首文件必须真的写进了字节，wrotePosition=" + last.getWrotePosition());
    }

    /** 目录不存在或里面没有任何文件 ⇒ 本仓这次是"空存储首次启动"，而不是"重启后加载"。 */
    private static boolean isStoreDirEmpty(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            return true;
        }
        File[] children = dir.listFiles();
        return children == null || children.length == 0;
    }

    /**
     * 独立复算 body 的 CRC，算法与存储层写入时用的那个（{@code UtilAll.crc32}，在 CommitLog.java:780，
     * 包私有、本测试够不着）逐字一致：{@code java.util.zip.CRC32} 的值截成【有符号 int】。
     * 刻意不复用生产的 CRC 实现类（MessageCodec 里的记录级 crc32 是另一套），这样"复算"才算独立。
     */
    private static int crc32Of(byte[] body) {
        java.util.zip.CRC32 crc = new java.util.zip.CRC32();
        crc.update(body);
        return (int) crc.getValue();
    }

    private String storeRoot(String sub) {
        return tempDir.resolve(sub).toString();
    }

    /** 现问一个当前空闲的端口，不占固定端口。 */
    private static int freePort() throws Exception {
        ServerSocket probe = new ServerSocket(0);
        try {
            return probe.getLocalPort();
        } finally {
            probe.close();
        }
    }

    private static void deleteRecursively(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteRecursively(child);
                }
            }
        }
        if (!file.delete()) {
            System.err.println("[restart-durability] leftover: " + file.getAbsolutePath());
        }
    }
}
