package com.taotao.cloud.mq.broker.integration;

import com.taotao.cloud.mq.broker.BrokerConfig;
import com.taotao.cloud.mq.broker.BrokerController;
import com.taotao.cloud.mq.common.TopicConfig;
import com.taotao.cloud.mq.common.ha.TopicConfigSerializeWrapper;
import com.taotao.cloud.mq.common.util.JsonCodec;
import com.taotao.cloud.mq.remoting.netty.NettyClientConfig;
import com.taotao.cloud.mq.remoting.netty.NettyRemotingClient;
import com.taotao.cloud.mq.remoting.netty.NettyServerConfig;
import com.taotao.cloud.mq.remoting.netty.RemotingSysResponseCode;
import com.taotao.cloud.mq.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.remoting.protocol.RequestCode;
import com.taotao.cloud.mq.plugin.store.MessageStoreConfig;
import com.taotao.cloud.mq.plugin.store.config.TopicConfigManager;
import com.taotao.cloud.mq.plugin.store.log.FlushDiskType;
import io.netty.channel.Channel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 验收用例：「Topic 配置跨重启保留」这句广告兑现到什么程度。
 * <p>
 * 判据形状是"建一条非默认队列数的 topic → 真关机 → 同一存储目录换一个全新实例 →
 * <b>不重发那条 CREATE 请求</b>，配置还在、队列数还是那个值"。所有断言都落在<b>新实例</b>上，
 * 而且要落在新实例<b>对外服务的那张嘴</b>上（{@code GET_ALL_TOPIC_CONFIG} 走真 Netty），
 * 不是只落在进程内的一个 getter 上 —— 后者连"重启"都没经过。
 * <ol>
 *   <li>{@link #topicConfigSurvivesBrokerRestartWithoutReissuingCreate}：主用例。
 *       附带一条前置对照：CREATE 请求返回<b>之后</b>盘上就必须已经有那份文件
 *       （落盘是写入口自己做的，不是等周期任务），否则重启那一半无从谈起。</li>
 *   <li>{@link #fullOverwriteFromPeerIsDurableOnThisBrokerToo}：全量替换（从主覆盖进来那条路）
 *       同样要留下盘上痕迹，且"incoming 里没有的条目"必须在内存和盘上<b>一起</b>消失。
 *       这条就是"落盘只落主"那种做法的反例：只要有一条写边不落盘，那张表与那个文件就分叉了。</li>
 *   <li>{@link #restoredTopicsAreVisibleThroughTheAdvertisedReadPaths}：重启后的表必须能被
 *       对外接口原样读到（含 perm/order/unit 这些不常看但会丢的字段）。</li>
 *   <li>{@link #corruptedRecordDoesNotTakeDownTheRest}：一行坏了不许牵连其它行 ——
 *       半截文件的最坏结果应当是少一条 topic，而不是整张表空掉。</li>
 * </ol>
 * <b>口径</b>：
 * <ul>
 *   <li>"重启"沿用本仓 {@code RestartDurabilityTest} / {@code ConsumerOffsetRestartE2ETest}
 *       已经认下来的形状：同一存储目录上换一个全新 {@link BrokerController} 实例并真
 *       {@code initialize()+start()}。它量的是"进程内内存态清零 + 必经盘 load"。</li>
 *   <li>存储路径 {@code storePathRootDir} 与 {@code storePathCommitLog} <b>都</b>显式设进
 *       {@code @TempDir}（只设一个会让 commitlog 逃逸到 {@code ~/store}）。</li>
 *   <li>全程没有 sleep、没有挂钟阈值：落盘的因果点在写入口自己（同一次写锁里重写文件），
 *       读回的因果点在 {@code initialize()}，所以"initialize 返回之后"天然是 happens-after。</li>
 *   <li>{@code load()} 早于对外服务这一点由第 1 条用例里那句
 *       "initialize() 之后、start() 之前就能读到"直接量出来，不靠注释。</li>
 * </ul>
 */
public class TopicConfigRestartE2ETest {

    private static final String TOPIC = "W2dPersistedTopic";
    private static final String SECOND_TOPIC = "W2dSecondPersistedTopic";
    private static final int READ_QUEUE_NUMS = 7;
    private static final int WRITE_QUEUE_NUMS = 3;

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

    // ==================== 1. 主用例：建 → 关机 → 同一目录再起 → 不重发 CREATE ====================

    @Test
    @DisplayName("E2E: 建 topic → 真 shutdown → 全新 BrokerController 起在同一存储目录 → 不重发 CREATE, 队列数还在")
    public void topicConfigSurvivesBrokerRestartWithoutReissuingCreate() throws Exception {
        String sub = "topic-restart";
        Held first = startBroker(sub);

        createTopicOverWire(first.addr, TOPIC, READ_QUEUE_NUMS, WRITE_QUEUE_NUMS);
        TopicConfig inMemory = first.broker.getAdminBrokerProcessor().getTopicConfig(TOPIC);
        assertNotNull(inMemory, "写进去之后当场就该读到");
        assertEquals(READ_QUEUE_NUMS, inMemory.getReadQueueNums());

        // ---- 前置对照：CREATE 的响应回来时，那份文件已经在盘上了（不靠周期任务、不靠关机）----
        File configFile = new File(storeRoot(sub), TopicConfigManager.TOPIC_CONFIG_FILE_NAME);
        assertTrue(configFile.isFile() && configFile.length() > 0,
                "★ 建 topic 的响应回来之后 topic_config.json 就必须已经在（不在 = 这条配置从没离开过进程）: "
                        + configFile.getAbsolutePath());
        assertTrue(new String(Files.readAllBytes(configFile.toPath()), StandardCharsets.UTF_8)
                        .contains(TOPIC),
                "文件里得看得见这条 topic 的名字");

        // ---- 真关机 ----
        first.broker.shutdown();
        launched.remove(first.broker);

        // ---- 重启：全新实例，同一存储目录。注意 start() 之前就先断言一次 ----
        Held second = buildBroker(sub);
        TopicConfig restoredBeforeServing = second.broker.getAdminBrokerProcessor().getTopicConfig(TOPIC);
        assertNotNull(restoredBeforeServing,
                "★ initialize() 返回、还没 start()（Netty 尚未绑端口）时表里就该有这条 topic"
                        + " ⇒ 说明读盘发生在启动路径上，而不是等第一条请求去顺手补");
        assertEquals(READ_QUEUE_NUMS, restoredBeforeServing.getReadQueueNums(),
                "★ 验收判据: 重启后读回来的读队列数必须是 " + READ_QUEUE_NUMS + "，实测="
                        + restoredBeforeServing.getReadQueueNums() + "（还是 4 = 用了默认值 = 表根本没读回来）");
        assertEquals(WRITE_QUEUE_NUMS, restoredBeforeServing.getWriteQueueNums(),
                "★ 验收判据: 重启后读回来的写队列数必须是 " + WRITE_QUEUE_NUMS + "，实测="
                        + restoredBeforeServing.getWriteQueueNums());
        second.broker.start();

        // ---- 对外服务的那张嘴也必须看得见它（这条请求走真 Netty，不经过任何进程内对象）----
        Map<String, TopicConfig> served = askAllTopicConfigs(second.addr);
        assertTrue(served.containsKey(TOPIC),
                "GET_ALL_TOPIC_CONFIG 里没有这条 topic ⇒ 心跳/从主同步看到的还是空表, 实际=" + served.keySet());
        TopicConfig servedConfig = served.get(TOPIC);
        assertEquals(READ_QUEUE_NUMS, servedConfig.getReadQueueNums(), "对外读到的读队列数");
        assertEquals(WRITE_QUEUE_NUMS, servedConfig.getWriteQueueNums(), "对外读到的写队列数");

        // ---- 重启之后这条路还能继续写：不是只读回来的死数据 ----
        createTopicOverWire(second.addr, SECOND_TOPIC, 2, 5);
        assertTrue(askAllTopicConfigs(second.addr).containsKey(SECOND_TOPIC),
                "重启后新建的 topic 也要能被读到");
        List<String> lines = Files.readAllLines(configFile.toPath(), StandardCharsets.UTF_8);
        assertEquals(2, countTopicsIn(lines, TOPIC) + countTopicsIn(lines, SECOND_TOPIC),
                "盘上两条 topic 各一行，重启后新建的那条也得当场落盘");

        second.broker.shutdown();
        launched.remove(second.broker);
    }

    // ==================== 2. 全量替换也要落本地盘 ====================

    @Test
    @DisplayName("全量替换（对端覆盖进来那条路）: 内存与盘一起换, incoming 里没有的条目两边同时消失")
    public void fullOverwriteFromPeerIsDurableOnThisBrokerToo() throws Exception {
        String sub = "replace-all";
        Held first = startBroker(sub);
        createTopicOverWire(first.addr, TOPIC, READ_QUEUE_NUMS, WRITE_QUEUE_NUMS);
        File configFile = new File(storeRoot(sub), TopicConfigManager.TOPIC_CONFIG_FILE_NAME);

        Map<String, TopicConfig> incoming = new HashMap<>();
        incoming.put(SECOND_TOPIC, new TopicConfig(SECOND_TOPIC, 1, 1, TopicConfig.PERM_READ));
        first.broker.getAdminBrokerProcessor().replaceAllTopicConfigs(incoming);

        assertNull(first.broker.getAdminBrokerProcessor().getTopicConfig(TOPIC),
                "全量替换要连本地多出来的那条一起删掉（旧语义）");
        String onDisk = new String(Files.readAllBytes(configFile.toPath()), StandardCharsets.UTF_8);
        assertFalse(onDisk.contains(TOPIC),
                "★ 内存已经换了、盘上还留着旧的那条 ⇒ 两份状态, 下一次重启会读回一个谁都没认的集合");
        assertTrue(onDisk.contains(SECOND_TOPIC), "新进来的那条必须落盘");

        first.broker.shutdown();
        launched.remove(first.broker);

        Held second = startBroker(sub);
        Map<String, TopicConfig> served = askAllTopicConfigs(second.addr);
        assertEquals(1, served.size(), "重启后表里必须恰好是替换后的那一份, 实际=" + served.keySet());
        assertNotNull(served.get(SECOND_TOPIC));
        assertEquals(TopicConfig.PERM_READ, served.get(SECOND_TOPIC).getPerm(),
                "替换进来的那份里的权限位也要跨过来");
        second.broker.shutdown();
        launched.remove(second.broker);
    }

    // ==================== 3. 恢复出来的字段不许缩水 ====================

    @Test
    @DisplayName("重启后每一条配置都还在, 且 read/write/perm/order/unit/sysFlag 逐项对上")
    public void restoredTopicsAreVisibleThroughTheAdvertisedReadPaths() throws Exception {
        String sub = "field-fidelity";
        Held first = startBroker(sub);
        Map<String, TopicConfig> written = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            String topic = "W2dFidelityTopic_" + i;
            TopicConfig config = new TopicConfig(topic, i + 1, i + 2, TopicConfig.PERM_READ_WRITE);
            config.setOrder(i % 2 == 0);
            config.setUnit(i == 3);
            config.setTopicSysFlag(100 + i);
            written.put(topic, config);
            first.broker.getTopicConfigManager().putTopicConfig(config);
        }
        assertEquals(5, first.broker.getTopicConfigManager().size());
        first.broker.shutdown();
        launched.remove(first.broker);

        Held second = startBroker(sub);
        Map<String, TopicConfig> served = askAllTopicConfigs(second.addr);
        assertEquals(written.size(), served.size(),
                "五条都要回来, 实测=" + served.keySet());
        for (Map.Entry<String, TopicConfig> entry : written.entrySet()) {
            TopicConfig want = entry.getValue();
            TopicConfig got = served.get(entry.getKey());
            assertNotNull(got, "重启后读不到 " + entry.getKey());
            assertEquals(want.getReadQueueNums(), got.getReadQueueNums(), entry.getKey() + " readQueueNums");
            assertEquals(want.getWriteQueueNums(), got.getWriteQueueNums(), entry.getKey() + " writeQueueNums");
            assertEquals(want.getPerm(), got.getPerm(), entry.getKey() + " perm");
            assertEquals(want.isOrder(), got.isOrder(), entry.getKey() + " order");
            assertEquals(want.isUnit(), got.isUnit(), entry.getKey() + " unit");
            assertEquals(want.getTopicSysFlag(), got.getTopicSysFlag(), entry.getKey() + " topicSysFlag");
        }
        second.broker.shutdown();
        launched.remove(second.broker);
    }

    // ==================== 4. 坏行不牵连 ====================

    @Test
    @DisplayName("盘上有一行坏了: 只丢那一条 topic, 其余照读（半截文件不该让整张表空掉）")
    public void corruptedRecordDoesNotTakeDownTheRest() throws Exception {
        String sub = "damaged-line";
        Held first = startBroker(sub);
        createTopicOverWire(first.addr, TOPIC, READ_QUEUE_NUMS, WRITE_QUEUE_NUMS);
        createTopicOverWire(first.addr, SECOND_TOPIC, 2, 2);
        first.broker.shutdown();
        launched.remove(first.broker);

        File configFile = new File(storeRoot(sub), TopicConfigManager.TOPIC_CONFIG_FILE_NAME);
        List<String> lines = new ArrayList<>(Files.readAllLines(configFile.toPath(), StandardCharsets.UTF_8));
        assertEquals(2, lines.size(), "两条 topic 两行");
        lines.add("{\"topicName\":\"W2dHalfWritten");   // 半截 JSON, 模拟写到一半断电
        Files.write(configFile.toPath(), String.join("\n", lines).getBytes(StandardCharsets.UTF_8));

        Held second = startBroker(sub);
        Map<String, TopicConfig> served = askAllTopicConfigs(second.addr);
        assertEquals(2, served.size(),
                "坏的那一行跳过就好, 另外两条必须回来, 实测=" + served.keySet());
        assertNotNull(served.get(TOPIC));
        assertNotNull(served.get(SECOND_TOPIC));
        assertNull(served.get("W2dHalfWritten"), "半截那条不该被凭空造出来");
        second.broker.shutdown();
        launched.remove(second.broker);
    }

    // ==================== 走真 Netty ====================

    /** 建/改一条 topic（{@code UPDATE_AND_CREATE_TOPIC} 走真链路）. */
    private void createTopicOverWire(String addr, String topic, int readN, int writeN) throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.UPDATE_AND_CREATE_TOPIC);
        req.addExtField("topic", topic);
        req.addExtField("readQueueNums", String.valueOf(readN));
        req.addExtField("writeQueueNums", String.valueOf(writeN));
        RemotingCommand resp = ask(addr, req);
        assertEquals(RemotingSysResponseCode.SUCCESS, resp.getCode(),
                "建 topic 必须被 broker 接受, remark=" + resp.getRemark());
    }

    /**
     * 走真链路问一次全量 topic 配置 —— 这张嘴就是主从同步与心跳读的那张嘴。
     */
    private Map<String, TopicConfig> askAllTopicConfigs(String addr) throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.GET_ALL_TOPIC_CONFIG);
        RemotingCommand resp = ask(addr, req);
        assertEquals(RemotingSysResponseCode.SUCCESS, resp.getCode(), "GET_ALL_TOPIC_CONFIG 必须被应答");
        TopicConfigSerializeWrapper wrapper =
                JsonCodec.decode(resp.getBody(), TopicConfigSerializeWrapper.class);
        assertNotNull(wrapper, "响应体必须能解出 TopicConfigSerializeWrapper");
        assertNotNull(wrapper.getTopicConfigTable(), "wrapper 里必须带着表");
        return new HashMap<>(wrapper.getTopicConfigTable());
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

    private Held startBroker(String sub) throws Exception {
        Held held = buildBroker(sub);
        held.broker.start();
        return held;
    }

    /**
     * 起一个真 broker（真 load、真绑端口）。不配 nameserver —— 本用例量的是"这张表活不活得过重启"。
     * <p>
     * 故意把 {@code initialize()} 与 {@code start()} 拆开返回：调用方可以在"已经 load 完、
     * 还没开始收请求"这个窗口里断言。
     */
    private Held buildBroker(String sub) throws Exception {
        MessageStoreConfig msc = new MessageStoreConfig();
        msc.setStorePathRootDir(storeRoot(sub));
        msc.setStorePathCommitLog(storeRoot(sub) + File.separator + "commitlog");
        msc.setMappedFileSizeCommitLog(1024 * 1024);
        msc.setFlushDiskType(FlushDiskType.SYNC_FLUSH);

        int port = freePort();

        BrokerConfig bc = new BrokerConfig();
        bc.setBrokerName("W2dBroker");
        bc.setBrokerClusterName("W2dCluster");
        bc.setNamesrvAddr("");

        NettyServerConfig nsc = new NettyServerConfig();
        nsc.setListenPort(port);

        BrokerController broker = new BrokerController(bc, msc, nsc);
        assertTrue(broker.initialize(), "BrokerController.initialize() 必须成功（真 load）");
        launched.add(broker);
        return new Held(broker, "127.0.0.1:" + port);
    }

    private String storeRoot(String sub) {
        return tempDir.resolve(sub).toString();
    }

    private static int countTopicsIn(List<String> lines, String topic) {
        int n = 0;
        for (String line : lines) {
            if (line.contains("\"topicName\":\"" + topic + "\"")) {
                n++;
            }
        }
        return n;
    }

    /** 一个真起起来的 broker 连同它的 {@code host:port}. */
    private static final class Held {
        private final BrokerController broker;
        private final String addr;

        Held(BrokerController broker, String addr) {
            this.broker = broker;
            this.addr = addr;
        }
    }

    /** 现问一个当前空闲的端口，不占固定端口（250 上临时端口段会撞）。 */
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
            System.err.println("[w2d-e2e] leftover: " + file.getAbsolutePath());
        }
    }
}
