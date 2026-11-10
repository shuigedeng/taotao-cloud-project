package com.taotao.cloud.mq.broker.integration;

import com.taotao.cloud.mq.broker.BrokerConfig;
import com.taotao.cloud.mq.broker.BrokerController;
import com.taotao.cloud.mq.common.message.MessageExt;
import com.taotao.cloud.mq.common.protocol.PullResultPayload;
import com.taotao.cloud.mq.common.protocol.SendResult;
import com.taotao.cloud.mq.common.protocol.SendStatus;
import com.taotao.cloud.mq.common.util.JsonCodec;
import com.taotao.cloud.mq.remoting.netty.NettyClientConfig;
import com.taotao.cloud.mq.remoting.netty.NettyRemotingClient;
import com.taotao.cloud.mq.remoting.netty.NettyServerConfig;
import com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.remoting.protocol.RequestCode;
import com.taotao.cloud.mq.plugin.store.MessageStoreConfig;
import com.taotao.cloud.mq.plugin.store.log.FlushDiskType;
import io.netty.channel.Channel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * W2b 的验收用例（工单 §2.4）：<b>提交上去的位点真的跨过了重启，并且真的决定了下一次拉取的起点</b>。
 * <p>
 * {@code BrokerController} 的 javadoc 广告"consumerOffsetManager: 消费位点持久化, 跨重启恢复"。
 * 后端（{@code ConsumerOffsetManager} 的 commitOffset/queryOffset/flush/load）一直是真的，
 * 缺的是两条边：①没有人往它写（{@code UPDATE_CONSUMER_OFFSET}(220) 零读者），
 * ②pull 从不问它（写死从 0 开始）。这一支把两条边接上，这个用例就是把<b>这两条边同时钉住</b>：
 * <ol>
 *   <li>{@link #committedOffsetSurvivesRestartAndDrivesTheNextPull}：发 10 条 → 只消费前 4 条 →
 *       把位点 4 <b>通过真 Netty 提交</b>给真 broker → 真 {@code shutdown()}（位点落盘）→
 *       <b>换一个全新的 BrokerController 实例</b>（同一存储目录，代表重启后的进程）→
 *       <b>不带 offset</b> 再拉 → 拿到的第一条必须是第 5 条（queueOffset == 4），一条不多一条不少。</li>
 *   <li>{@link #restoreIsScopedToConsumerGroupAndFallsBackToZeroWithoutOne}：位点是按
 *       {@code (topic, queueId, group)} 三元组存的 ⇒ 换个消费组必须看不到别人的位点（从 0 起）；
 *       请求里压根没带消费组时也必须回落到 0。这条专门杀"把 group 写成常量"这种为了让测试绿的糊法。</li>
 *   <li>{@link #explicitOffsetOnRequestWinsOverCommittedOffset}：请求带了 offset 就以请求为准，
 *       已提交位点不许覆盖调用方的显式意图。</li>
 *   <li>{@link #updateConsumerOffsetRejectsIncompleteRequests}：提交口不许把缺 group/缺 offset/负位点
 *       悄悄当成 0 收下 —— 那会把"没提交"伪装成"提交到了 0"。</li>
 * </ol>
 * <b>三条口径说明</b>（免得读数被读歪）：
 * <ul>
 *   <li>"重启"用的是本仓 W1 {@code RestartDurabilityTest} 已经认下来的形状：同一存储目录上换一个
 *       全新的 {@link BrokerController} 实例并真 {@code initialize()+start()}。它量的是
 *       <b>进程内内存态清零 + 必经盘 load</b>，正是那句广告的内容；它不量"进程被 kill -9"。</li>
 *   <li>发/提交/拉三个码<b>都走真 Netty 客户端</b>（{@link NettyRemotingClient}），所以
 *       {@code registerProcessor} 的分派本身也在承重范围内，不只是结构上"注册过"。</li>
 *   <li>全程没有 sleep、也没有挂钟阈值：位点落盘的因果点是
 *       {@code BrokerController.shutdown() → consumerOffsetManager.shutdown() → flush()}，
 *       所以"关机之后再断言"天然是 happens-after；周期 flush（5s）不参与任何判定。
 *       判据是一个个 msgId / queueOffset，不是时间。</li>
 * </ul>
 * 存储路径：{@code storePathRootDir} 与 {@code storePathCommitLog} <b>都</b>显式设进 {@code @TempDir}
 * （只设一个会让 commitlog 逃逸到 {@code ~/store}，本仓踩过 2.0 GB 的账）。
 */
public class ConsumerOffsetRestartE2ETest {

    private static final String TOPIC = "W2bOffsetTopic";
    private static final int QUEUE_ID = 2;
    private static final String GROUP = "W2bOffsetGroup";
    private static final String OTHER_GROUP = "W2bOtherOffsetGroup";

    /** 发 10 条，只消费并提交前 4 条的位点 ⇒ 重启后第一次不带 offset 的拉取必须从第 5 条开始. */
    private static final int TOTAL = 10;
    private static final int CONSUMED = 4;

    @TempDir
    Path tempDir;

    private final List<BrokerController> launched = new ArrayList<>();
    private NettyRemotingClient client;

    @AfterEach
    public void tearDown() {
        if (client != null) {
            client.shutdown();
            client = null;
        }
        for (BrokerController each : launched) {
            try {
                each.shutdown();
            } catch (Exception ignore) {
                // 用例里已经关过的，收尸时忽略
            }
        }
        launched.clear();
        deleteRecursively(tempDir.toFile());
    }

    // ==================== 1. 主用例：提交 → 重启 → 不带 offset 的拉取从第 N+1 条开始 ====================

    @Test
    @DisplayName("E2E: 提交位点 → 真 shutdown → 全新 BrokerController 起在同一存储目录 → 不带 offset 再拉，第一条是第 5 条")
    public void committedOffsetSurvivesRestartAndDrivesTheNextPull() throws Exception {
        String sub = "commit-restart";
        Held first = startBroker(sub);

        List<String> msgIds = sendOverWire(first.addr, TOTAL);
        assertEquals(TOTAL, msgIds.size());

        // ---- 消费掉前 CONSUMED 条（显式带 offset=0，与接线前的用法逐字相同）----
        PullResultPayload consumed = pullOverWire(first.addr, 0L, GROUP, CONSUMED);
        assertEquals(CONSUMED, consumed.getMessages().size(), "第一步就该读到 4 条");
        assertEquals(msgIds.get(0), consumed.getMessages().get(0).getMsgId(), "带 offset=0 就该从第 1 条读起");
        assertEquals((long) CONSUMED, consumed.getNextOffset(), "读完 4 条之后该提交的位置");

        // ---- 前置对照：还没提交时，不带 offset 的拉取只能从 0 开始（证明下面那条红不是白捡的）----
        PullResultPayload beforeCommit = pullOverWire(first.addr, null, GROUP, TOTAL);
        assertEquals(0L, beforeCommit.getMessages().get(0).getQueueOffset(),
                "本组还没提交过位点 ⇒ queryOffset 返回 -1 ⇒ 起点必须回落到 0");

        // ---- 提交侧：UPDATE_CONSUMER_OFFSET(220) 真走一次 Netty ----
        long stored = commitOverWire(first.addr, GROUP, CONSUMED);
        assertEquals(CONSUMED, stored, "broker 回读上来的位点必须就是提交进去的那个");
        assertEquals(CONSUMED, first.broker.getConsumerOffsetManager().queryOffset(TOPIC, QUEUE_ID, GROUP),
                "写进的必须是 broker 真在用的那个 manager");

        // ---- 真关机：ConsumerOffsetManager.shutdown() → flush() ⇒ 位点必须离开进程落到盘上 ----
        first.broker.shutdown();
        launched.remove(first.broker);
        File offsetFile = new File(storeRoot(sub), "consumer_offset.json");
        assertTrue(offsetFile.isFile() && offsetFile.length() > 0,
                "干净关机之后 consumer_offset.json 必须在（不在就说明位点从没离开过进程）: "
                        + offsetFile.getAbsolutePath());

        // ---- 重启：全新 BrokerController 实例、同一存储目录，内存表由 load() 重建 ----
        Held second = startBroker(sub);
        assertEquals(CONSUMED, second.broker.getConsumerOffsetManager().queryOffset(TOPIC, QUEUE_ID, GROUP),
                "重启后、任何一次拉取之前，位点就必须已经从盘上读回来了（这才是「持久化」那一半）");

        // ---- 读取侧：不带 offset ⇒ broker 按 (topic, queueId, group) 决定起点 ----
        PullResultPayload afterRestart = pullOverWire(second.addr, null, GROUP, TOTAL);
        List<MessageExt> tail = afterRestart.getMessages();
        assertFalse(tail.isEmpty(), "重启后不带 offset 拉, 至少得读到尾巴（一条都没有 = 起点跑到队尾之后了）");
        assertEquals(CONSUMED, tail.get(0).getQueueOffset(),
                "★ 验收判据: 重启后不带 offset 拉到的第一条必须是第 " + (CONSUMED + 1) + " 条（queueOffset=="
                        + CONSUMED + "），实测=" + tail.get(0).getQueueOffset() + " —— 还是 0 就说明位点没跨过来");
        assertEquals(msgIds.get(CONSUMED), tail.get(0).getMsgId(),
                "★ 而且第一条的 msgId 必须逐字对上第 5 条那个");
        assertEquals(TOTAL - CONSUMED, tail.size(),
                "从已提交位点起读，剩下的必须是 6 条（读到 10 条 = 起点还是 0，位点没被用上）");
        for (int i = 0; i < tail.size(); i++) {
            assertEquals((long) CONSUMED + i, tail.get(i).getQueueOffset(), "尾巴必须连续, index=" + i);
            assertEquals(msgIds.get(CONSUMED + i), tail.get(i).getMsgId(), "msgId 逐条对上, index=" + i);
        }
        assertEquals(TOTAL, afterRestart.getNextOffset(), "读完尾巴之后的位点");

        // ---- 尾巴也能提交：说明这条边可以反复走，不是一次性摆设 ----
        assertEquals(TOTAL, commitOverWire(second.addr, GROUP, afterRestart.getNextOffset()),
                "第二次提交必须被记下");
        assertEquals(0, pullOverWire(second.addr, null, GROUP, TOTAL).getMessages().size(),
                "提交到队尾之后再不带 offset 拉, 应当读不到新消息（起点真的是那位, 不是 0）");

        second.broker.shutdown();
        launched.remove(second.broker);
    }

    // ==================== 2. 按组恢复：group 是 key 的一部分，不是常量 ====================

    @Test
    @DisplayName("§2.2: 位点按 (topic, queueId, group) 生效——别的消费组看不见, 请求不带 group 时回落 0")
    public void restoreIsScopedToConsumerGroupAndFallsBackToZeroWithoutOne() throws Exception {
        String sub = "group-scoped";
        Held broker = startBroker(sub);
        List<String> msgIds = sendOverWire(broker.addr, TOTAL);

        assertEquals(CONSUMED, commitOverWire(broker.addr, GROUP, CONSUMED), "给 GROUP 提交 4");

        // 同一台 broker 上换一个消费组：它没提交过 ⇒ 必须从 0 起（"把 group 写成常量"在这条上必红）
        PullResultPayload otherGroup = pullOverWire(broker.addr, null, OTHER_GROUP, TOTAL);
        assertEquals(TOTAL, otherGroup.getMessages().size(),
                "别的组看不见 GROUP 的位点 ⇒ 必须还是 10 条，读到 6 条就说明 group 被写死了");
        assertEquals(msgIds.get(0), otherGroup.getMessages().get(0).getMsgId(), "另一个组的第一条仍是第 1 条");
        assertEquals(0L, otherGroup.getMessages().get(0).getQueueOffset());

        // 请求里压根没有 group 这个 key ⇒ 结构上无从恢复, 回落到 0（不许抛, 老请求不能因此全挂）
        PullResultPayload groupless = pullOverWire(broker.addr, null, null, TOTAL);
        assertEquals(TOTAL, groupless.getMessages().size(),
                "不带 group 的 pull 必须仍从 0 开始（这是 §2.2 那条真缺陷的显式边界）");
        assertEquals(0L, groupless.getMessages().get(0).getQueueOffset());

        // 同一个组再提交一次更靠后的位置：后写覆盖先写
        assertEquals(TOTAL, commitOverWire(broker.addr, GROUP, TOTAL), "位点可以前移");
        PullResultPayload moved = pullOverWire(broker.addr, null, GROUP, TOTAL);
        assertEquals(0, moved.getMessages().size(), "提交到 10 之后 GROUP 读不到新消息");

        broker.broker.shutdown();
        launched.remove(broker.broker);
    }

    // ==================== 3. 显式 offset 优先于已提交位点 ====================

    @Test
    @DisplayName("请求带了 offset 就以请求为准：已提交位点不许覆盖调用方的显式意图")
    public void explicitOffsetOnRequestWinsOverCommittedOffset() throws Exception {
        String sub = "explicit-offset-wins";
        Held broker = startBroker(sub);
        List<String> msgIds = sendOverWire(broker.addr, TOTAL);
        assertEquals(CONSUMED, commitOverWire(broker.addr, GROUP, CONSUMED));

        // 显式回到 0：已提交位点不得插手
        PullResultPayload fromZero = pullOverWire(broker.addr, 0L, GROUP, TOTAL);
        assertEquals(TOTAL, fromZero.getMessages().size(), "带 offset=0 就必须从第 1 条给起");
        assertEquals(msgIds.get(0), fromZero.getMessages().get(0).getMsgId());

        // 显式指到中间：也不得被已提交位点改写
        PullResultPayload fromSix = pullOverWire(broker.addr, 6L, GROUP, TOTAL);
        assertEquals(6L, fromSix.getMessages().get(0).getQueueOffset(), "offset=6 就该从第 7 条起");
        assertEquals(msgIds.get(6), fromSix.getMessages().get(0).getMsgId());

        broker.broker.shutdown();
        launched.remove(broker.broker);
    }

    // ==================== 4. 提交口不许把坏请求伪装成"提交到了 0" ====================

    @Test
    @DisplayName("提交侧校验: 缺 group / 缺 offset / 位点为负一律 SYSTEM_ERROR, 且不得改动已记下的位点")
    public void updateConsumerOffsetRejectsIncompleteRequests() throws Exception {
        Held broker = startBroker("commit-validation");

        assertEquals(CONSUMED, commitOverWire(broker.addr, GROUP, CONSUMED), "先立一个已知的好值");

        assertCommitRejected(broker.addr, null, CONSUMED, "consumerGroup missing");
        assertCommitRejected(broker.addr, "", CONSUMED, "consumerGroup missing");
        assertCommitRejected(broker.addr, GROUP, -1L, "offset must be >= 0");

        RemotingCommand noOffset = RemotingCommand.createRequestCommand(RequestCode.UPDATE_CONSUMER_OFFSET);
        noOffset.addExtField("topic", TOPIC);
        noOffset.addExtField("queueId", String.valueOf(QUEUE_ID));
        noOffset.addExtField("consumerGroup", GROUP);
        RemotingCommand resp = ask(broker.addr, noOffset);
        assertEquals(RemotingSysResponseCode.SYSTEM_ERROR, resp.getCode(), "缺 offset 必须显式回错");
        assertTrue(resp.getRemark() != null && resp.getRemark().contains("offset"),
                "remark 要说出缺的是哪个字段, 实际=" + resp.getRemark());

        // 四条坏请求都没改动那位：仍然是第 1 步提交的好值
        assertEquals(CONSUMED, broker.broker.getConsumerOffsetManager().queryOffset(TOPIC, QUEUE_ID, GROUP),
                "被拒的请求不许动已经记下的位点（否则「提交失败」会变成「提交到 0」）");

        // 未知码仍走既有的 REQUEST_CODE_NOT_SUPPORTED 口径（RESET_CONSUMER_OFFSET 本支不做）
        RemotingCommand reset = RemotingCommand.createRequestCommand(RequestCode.RESET_CONSUMER_OFFSET);
        reset.addExtField("topic", TOPIC);
        RemotingCommand resetResp = ask(broker.addr, reset);
        assertEquals(RemotingSysResponseCode.REQUEST_CODE_NOT_SUPPORTED, resetResp.getCode(),
                "§3: RESET_CONSUMER_OFFSET(410) 本支不做, 收到了必须显式说不支持, 不许猜语义");

        broker.broker.shutdown();
        launched.remove(broker.broker);
    }

    // ==================== 通过真 Netty 走三个请求码 ====================

    /** 一个真起起来的 broker 连同它的 {@code host:port}（{@code BrokerConfig} 里没有端口字段，
     *  端口只活在 {@code NettyServerConfig} 上，所以这里自己带一份）。 */
    private static final class Held {
        private final BrokerController broker;
        private final String addr;

        Held(BrokerController broker, String addr) {
            this.broker = broker;
            this.addr = addr;
        }
    }

    /** 发 N 条（{@code SEND_MESSAGE} 走真链路），返回按写入顺序排好的 msgId. */
    private List<String> sendOverWire(String addr, int n) throws Exception {
        List<String> msgIds = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String msgId = "W2B-MSG-" + i;
            MessageExt inbound = new MessageExt();
            inbound.setTopic(TOPIC);
            inbound.setMsgId(msgId);
            inbound.setTags("W2bTag");
            inbound.setKeys("W2bKey-" + i);
            inbound.setBody(("w2b-payload-" + i).getBytes(StandardCharsets.UTF_8));
            inbound.setBornTimestamp(1700000000000L + i);

            RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.SEND_MESSAGE);
            req.addExtField("topic", TOPIC);
            req.addExtField("queueId", String.valueOf(QUEUE_ID));
            req.addExtField("brokerName", "W2bBroker");
            req.setBody(JsonCodec.encode(inbound));

            RemotingCommand resp = ask(addr, req);
            assertEquals(RemotingSysResponseCode.SUCCESS, resp.getCode(), "第 " + i + " 条必须被应答");
            SendResult sr = JsonCodec.decode(resp.getBody(), SendResult.class);
            assertNotNull(sr, "发送响应体必须能解出 SendResult");
            assertEquals(SendStatus.SEND_OK, sr.getSendStatus(), "第 " + i + " 条必须 SEND_OK");
            assertEquals((long) i, sr.getQueueOffset(), "第 " + i + " 条的队列位点");
            msgIds.add(msgId);
        }
        return msgIds;
    }

    /**
     * 提交位点（{@code UPDATE_CONSUMER_OFFSET}(220) 走真链路 ⇒ {@code registerProcessor} 的分派也在承重范围内）。
     *
     * @return broker 从 manager 里读回来并回写在那个 ext field 上的位点
     */
    private long commitOverWire(String addr, String group, long offset) throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.UPDATE_CONSUMER_OFFSET);
        req.addExtField("topic", TOPIC);
        req.addExtField("queueId", String.valueOf(QUEUE_ID));
        req.addExtField("consumerGroup", group);
        req.addExtField("offset", String.valueOf(offset));
        RemotingCommand resp = ask(addr, req);
        assertEquals(RemotingSysResponseCode.SUCCESS, resp.getCode(),
                "提交必须被 broker 接受, remark=" + resp.getRemark());
        String stored = resp.getExtField("offset");
        assertNotNull(stored, "响应里必须带回读上来的位点（只回 SUCCESS 就等于没验到「写进去了」）");
        return Long.parseLong(stored.trim());
    }

    /** 一次被拒的提交：必须回 SYSTEM_ERROR，且 remark 点名缺哪个字段. */
    private void assertCommitRejected(String addr, String group, long offset, String expectedRemark)
            throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.UPDATE_CONSUMER_OFFSET);
        req.addExtField("topic", TOPIC);
        req.addExtField("queueId", String.valueOf(QUEUE_ID));
        if (group != null) {
            req.addExtField("consumerGroup", group);
        }
        req.addExtField("offset", String.valueOf(offset));
        RemotingCommand resp = ask(addr, req);
        assertEquals(RemotingSysResponseCode.SYSTEM_ERROR, resp.getCode(),
                "坏请求（" + expectedRemark + "）必须回 SYSTEM_ERROR, 不许悄悄收下");
        assertTrue(resp.getRemark() != null && resp.getRemark().contains(expectedRemark),
                "remark 该点名 " + expectedRemark + ", 实际=" + resp.getRemark());
    }

    /**
     * 走真链路拉一次。
     *
     * @param offset {@code null} = 请求里<b>不发</b> offset 字段（本支要兑现的那条路）；
     *               非 null 则照旧带上，作为"显式优先"的对照
     * @param group  消费组; {@code null} 表示请求里没有 group 这个 key
     */
    private PullResultPayload pullOverWire(String addr, Long offset, String group, int maxNum) throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.PULL_MESSAGE);
        req.addExtField("topic", TOPIC);
        req.addExtField("queueId", String.valueOf(QUEUE_ID));
        if (offset != null) {
            req.addExtField("offset", String.valueOf(offset.longValue()));
        }
        if (group != null) {
            req.addExtField("consumerGroup", group);
        }
        req.addExtField("maxNum", String.valueOf(maxNum));

        RemotingCommand resp = ask(addr, req);
        assertEquals(RemotingSysResponseCode.SUCCESS, resp.getCode(), "pull 必须被应答");
        PullResultPayload payload = JsonCodec.decode(resp.getBody(), PullResultPayload.class);
        assertNotNull(payload, "pull 响应体必须能解出 PullResultPayload");
        assertEquals(TOPIC, payload.getTopic());
        assertEquals(QUEUE_ID, payload.getQueueId());
        if (payload.getMessages() == null) {
            payload.setMessages(new ArrayList<MessageExt>());
        }
        return payload;
    }

    private RemotingCommand ask(String addr, RemotingCommand request) throws Exception {
        if (client == null) {
            client = new NettyRemotingClient(new NettyClientConfig());
            client.start();
        }
        Channel channel = client.getOrCreateChannel(addr);
        assertNotNull(channel, "连不上 broker: " + addr);
        RemotingCommand response = client.invokeSync(channel, request, 5000L);
        assertNotNull(response, "RPC 没有回应: code=" + request.getCode() + " addr=" + addr);
        return response;
    }

    // ==================== broker 生命周期 ====================

    /**
     * 起一个真 broker（真 load、真绑端口、真起刷盘与周期 flush 线程）。不配 nameserver ——
     * 本用例量的是"位点出不出得了进程"，路由不在判定里。
     */
    private Held startBroker(String sub) throws Exception {
        MessageStoreConfig msc = new MessageStoreConfig();
        msc.setStorePathRootDir(storeRoot(sub));
        msc.setStorePathCommitLog(storeRoot(sub) + File.separator + "commitlog");
        msc.setMappedFileSizeCommitLog(1024 * 1024);
        msc.setFlushDiskType(FlushDiskType.SYNC_FLUSH);

        int port = freePort();

        BrokerConfig bc = new BrokerConfig();
        bc.setBrokerName("W2bBroker");
        bc.setBrokerClusterName("W2bCluster");
        bc.setNamesrvAddr("");

        NettyServerConfig nsc = new NettyServerConfig();
        nsc.setListenPort(port);

        BrokerController broker = new BrokerController(bc, msc, nsc);
        assertTrue(broker.initialize(), "BrokerController.initialize() 必须成功（真 load）");
        broker.start();
        launched.add(broker);
        return new Held(broker, "127.0.0.1:" + port);
    }

    private String storeRoot(String sub) {
        return tempDir.resolve(sub).toString();
    }

    /** 现问一个当前空闲的端口，不占固定端口（250 上临时端口段会撞, 见工单 §3 环境的账）。 */
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
            System.err.println("[w2b-e2e] leftover: " + file.getAbsolutePath());
        }
    }
}
