package com.taotao.cloud.mq.consistency.ttc.spring.host;

import com.taotao.cloud.mq.consistency.ttc.client.MQClientInstance;
import com.taotao.cloud.mq.consistency.ttc.remoting.netty.NettyClientConfig;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RemotingCommand;
import com.taotao.cloud.mq.consistency.ttc.remoting.protocol.RequestCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * z-mq 测试 topic 预创建器.
 * <p>
 * 背景: z-mq 1.0.2 的 broker 不会在收到 SendMessage 时自动向 nameserver 注册 topic,
 * 第一次 producer 发消息时 nameserver 返回 "topic not exist", producer 报错 "No route".
 * <p>
 * z-mq nameserver 的 UPDATE_AND_CREATE_TOPIC RPC 需要 request extField:
 * topic / readQueueNums / writeQueueNums (nameserver 自己构造 TopicConfig, 不解析 body).
 * <p>
 * 时序关键:
 * <ol>
 *   <li>Spring 启动 → ApplicationStartedEvent</li>
 *   <li>ZMqEmbeddedServerConfig.spawn nameserver + broker (子进程)</li>
 *   <li>nameserver 启动监听 9876 (≈1s)</li>
 *   <li>broker 启动监听 10911 (≈3-5s), 然后向 nameserver 发送 REGISTER_BROKER 心跳</li>
 *   <li>nameserver 处理 REGISTER_BROKER → brokerAddrTable 写入</li>
 *   <li>调 UPDATE_AND_CREATE_TOPIC, nameserver 会把 topicQueueTable 中的 topic 绑到已注册 broker</li>
 * </ol>
 * <p>
 * 如果在 broker 注册之前就调 UPDATE_AND_CREATE_TOPIC, nameserver 返回 code=0 (创建成功),
 * 但 topicQueueTable 里的 topic 没绑 QueueData. producer/consumer 仍然 "No route".
 * 所以必须等 broker 注册完成 (≈5-10s) 后再调, 并且 **重试直到 nameserver 真的把 topic 绑到 broker**.
 * <p>
 * 实际上 nameserver 的 UPDATE_AND_CREATE_TOPIC 是 idempotent + 总是把 topic 绑到所有已注册 broker.
 * 所以简单地一直重试就能解决问题.
 */
@Configuration
@ConditionalOnProperty(prefix = "z.mq", name = "enabled", havingValue = "true")
public class ZMqTopicBootstrap {

    private static final Logger log = LoggerFactory.getLogger(ZMqTopicBootstrap.class);
    private static final String TOPIC = "z-opc-test";
    private static final int DEFAULT_QUEUE_NUMS = 4;
    private static final long RETRY_INTERVAL_MS = 1000L;
    private static final long TIMEOUT_MS = 60000L;

    @Value("${z.mq.namesrv-addr:127.0.0.1:9876}")
    private String namesrvAddr;

    private final AtomicBoolean done = new AtomicBoolean(false);

    @EventListener(ApplicationStartedEvent.class)
    public void scheduleCreate() {
        Thread t = new Thread(this::runWithRetry, "zmq-topic-bootstrap");
        t.setDaemon(true);
        t.start();
    }

    private void runWithRetry() {
        long deadline = System.currentTimeMillis() + TIMEOUT_MS;
        int attempt = 0;
        int consecutiveFail = 0;
        while (System.currentTimeMillis() < deadline && !done.get()) {
            attempt++;
            try {
                AttemptResult r = doCreateTopic();
                if (r.success) {
                    done.set(true);
                    log.info("[zmq-topic-bootstrap] ✓ topic '{}' ready after {} attempts (broker-bound)", TOPIC, attempt);
                    return;
                }
                consecutiveFail++;
                // 同样的 "no broker" 失败不值得一直 retry, 等 2s 让 broker 完成注册
                long sleepMs = (r.reason != null && r.reason.contains("no broker")) ? 2000L : RETRY_INTERVAL_MS;
                if (consecutiveFail % 10 == 1) {
                    log.warn("[zmq-topic-bootstrap] attempt {} not yet: {}", attempt, r.reason);
                }
                Thread.sleep(sleepMs);
            } catch (Exception e) {
                consecutiveFail++;
                log.warn("[zmq-topic-bootstrap] attempt {} failed: {}", attempt, e.getMessage());
                try { Thread.sleep(RETRY_INTERVAL_MS); } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); return;
                }
            }
        }
        if (!done.get()) {
            log.error("[zmq-topic-bootstrap] ✗ topic '{}' register timeout after {}ms / {} attempts",
                    TOPIC, TIMEOUT_MS, attempt);
        }
    }

    private AttemptResult doCreateTopic() throws Exception {
        MQClientInstance client = new MQClientInstance(
                "z-opc-topic-bootstrap", namesrvAddr, new NettyClientConfig());
        try {
            client.start();

            RemotingCommand request = RemotingCommand.createRequestCommand(RequestCode.UPDATE_AND_CREATE_TOPIC);
            request.addExtField("topic", TOPIC);
            request.addExtField("readQueueNums", String.valueOf(DEFAULT_QUEUE_NUMS));
            request.addExtField("writeQueueNums", String.valueOf(DEFAULT_QUEUE_NUMS));

            RemotingCommand response = client.invokeSync(namesrvAddr, request, 5000);
            if (response.getCode() == 0) {
                // nameserver 没把 broker-bound 信息放在 response body, 我们用 GET_ALL_TOPIC_LIST 来确认
                if (verifyTopicBound(client)) {
                    return new AttemptResult(true, "topic bound to broker");
                }
                return new AttemptResult(false, "topic registered but not bound to any broker yet");
            }
            return new AttemptResult(false, "code=" + response.getCode() + " remark=" + response.getRemark());
        } finally {
            client.shutdown();
        }
    }

    /**
     * 调 GET_ROUTEINFO_BY_TOPIC 验证 topic 是否真正绑了 broker (queueDatas 非空).
     * <p>
     * TopicRouteData JsonCodec 输出:
     * {"orderTopicConf":0,"order":false,"queueDatas":[{...brokerName...}],"brokerDatas":[{...}]}
     * <p>
     * 关键: TopicRouteData 字段不含 topic 名字段 (topic 在 request extField 里),
     * 所以 body 里只有 "queueDatas" / "brokerDatas" / "brokerName".
     */
    private boolean verifyTopicBound(MQClientInstance client) throws Exception {
        RemotingCommand req = RemotingCommand.createRequestCommand(RequestCode.GET_ROUTEINFO_BY_TOPIC);
        req.addExtField("topic", TOPIC);
        RemotingCommand resp = client.invokeSync(namesrvAddr, req, 3000);
        if (resp.getCode() != 0) return false;
        byte[] body = resp.getBody();
        if (body == null || body.length < 30) return false;
        String bodyStr = new String(body, java.nio.charset.StandardCharsets.UTF_8);
        // 检查 queueDatas 不为空数组 — 如果 topic 没绑 broker, queueDatas 不会出现或为空
        // 简化判断: 包含 "queueDatas":" 后面不是 []  (即包含至少一个 queue)
        int idx = bodyStr.indexOf("\"queueDatas\":");
        if (idx < 0) return false;
        int bracketIdx = bodyStr.indexOf('[', idx);
        if (bracketIdx < 0) return false;
        int closeBracketIdx = bodyStr.indexOf(']', bracketIdx);
        return closeBracketIdx > bracketIdx + 1;  // [] 是空, [{...}] 是非空
    }

    private static class AttemptResult {
        final boolean success;
        final String reason;
        AttemptResult(boolean s, String r) { this.success = s; this.reason = r; }
    }
}
