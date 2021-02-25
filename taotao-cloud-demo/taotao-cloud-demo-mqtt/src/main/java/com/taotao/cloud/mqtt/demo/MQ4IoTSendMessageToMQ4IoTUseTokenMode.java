package com.taotao.cloud.mqtt.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.taotao.cloud.mqtt.util.ConnectionOptionWrapper;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * 本代码提供Token 鉴权模式下 MQ4IOT 客户端发送消息到 MQ4IOT 客户端的示例，其中初始化参数请根据实际情况修改
 * Token 模式和签名模式仅仅是 MQ4IoT 客户端的鉴权方式，业务层面功能都是一致的。
 */
public class MQ4IoTSendMessageToMQ4IoTUseTokenMode {
    public static void main(String[] args) throws Exception {
        /**
         * MQ4IOT 实例 ID，购买后控制台获取
         */
        String instanceId = "XXXXX";
        /**
         * 接入点地址，购买 MQ4IOT 实例，且配置完成后即可获取，接入点地址必须填写分配的域名，不得使用 IP 地址直接连接，否则可能会导致客户端异常。
         */
        String endPoint = "XXXXX.mqtt.aliyuncs.com";
        /**
         * 账号 accesskey，从账号系统控制台获取
         */
        String accessKey = "XXXXX";
        /**
         * MQ4IOT clientId，由业务系统分配，需要保证每个 tcp 连接都不一样，保证全局唯一，如果不同的客户端对象（tcp 连接）使用了相同的 clientId 会导致连接异常断开。
         * clientId 由两部分组成，格式为 GroupID@@@DeviceId，其中 groupId 在 MQ4IOT 控制台申请，DeviceId 由业务方自己设置，clientId 总长度不得超过64个字符。
         */
        String clientId = "GID_XXXXX@@@XXXXX";
        /**
         * MQ4IOT 消息的一级 topic，需要在控制台申请才能使用。
         * 如果使用了没有申请或者没有被授权的 topic 会导致鉴权失败，服务端会断开客户端连接。
         */
        final String parentTopic = "XXXXX";
        /**
         * MQ4IOT支持子级 topic，用来做自定义的过滤，此处为示意，可以填写任何字符串，具体参考https://help.aliyun.com/document_detail/42420.html?spm=a2c4g.11186623.6.544.1ea529cfAO5zV3
         * 需要注意的是，完整的 topic 长度不得超过128个字符。
         */
        final String mq4IotTopic = parentTopic + "/" + "testMq4Iot";
        /**
         * QoS参数代表传输质量，可选0，1，2，根据实际需求合理设置，具体参考 https://help.aliyun.com/document_detail/42420.html?spm=a2c4g.11186623.6.544.1ea529cfAO5zV3
         */
        final int qosLevel = 0;
        /**
         * 当前操作区域
         */
        final String regionId = "XXXX";
        /**
         * 客户端使用的 token 内容，需要提前分配。实际业务中一般是有业务应用服务分配给移动终端。具体交互流程参考
         *  https://help.aliyun.com/document_detail/54226.html?spm=a2c4g.11186623.6.559.ca8a695aco5DF7
         *  此处作为示意，在 MQ4IoT 客户端本地申请 token， 实际使用时禁止在 MQTT 客户端程序申请 Token，以免引起 AccessKey，SecretKey 泄露，失去 token 的意义。
         */
        List<String> resource = new ArrayList<String>();
        resource.add(mq4IotTopic);
        /**
         * 此处示意，申请一个小时有效期的 token,实际使用时禁止在 MQTT 客户端程序申请 Token，以免引起 AccessKey，SecretKey 泄露，失去 token 的意义。
         */
        String token = TokenApiDemo.applyToken(accessKey, "XXX", resource, "R,W", 3600 * 1000, instanceId, regionId);
        Map<String, String> tokenData = new HashMap<String, String>();
        tokenData.put("RW", token);
        ConnectionOptionWrapper connectionOptionWrapper = new ConnectionOptionWrapper(instanceId, accessKey, clientId, tokenData);
        final MemoryPersistence memoryPersistence = new MemoryPersistence();
        /**
         * 客户端使用的协议和端口必须匹配，具体参考文档 https://help.aliyun.com/document_detail/44866.html?spm=a2c4g.11186623.6.552.25302386RcuYFB
         * 如果是 SSL 加密则设置ssl://endpoint:8883
         */
        final MqttClient mqttClient = new MqttClient("tcp://" + endPoint + ":1883", clientId, memoryPersistence);
        /**
         * 客户端设置好发送超时时间，防止无限阻塞
         */
        mqttClient.setTimeToWait(5000);
        final ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
        mqttClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                /**
                 * 客户端连接成功后就需要尽快订阅需要的 topic
                 */
                System.out.println("connect success");
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final String topicFilter[] = {mq4IotTopic};
                            final int[] qos = {qosLevel};
                            mqttClient.subscribe(topicFilter, qos);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void connectionLost(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                /**
                 * 消费消息的回调接口，需要确保该接口不抛异常，该接口运行返回即代表消息消费成功。
                 * 消费消息需要保证在规定时间内完成，如果消费耗时超过服务端约定的超时时间，对于可靠传输的模式，服务端可能会重试推送，业务需要做好幂等去重处理。超时时间约定参考限制
                 * https://help.aliyun.com/document_detail/63620.html?spm=a2c4g.11186623.6.546.229f1f6ago55Fj
                 */
                System.out.println(
                    "receive msg from topic " + s + " , body is " + new String(mqttMessage.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                System.out.println("send msg succeed topic is : " + iMqttDeliveryToken.getTopics()[0]);
            }
        });
        mqttClient.connect(connectionOptionWrapper.getMqttConnectOptions());
        for (int i = 0; i < 10; i++) {
            MqttMessage message = new MqttMessage("hello mq4Iot pub sub msg".getBytes());
            message.setQos(qosLevel);
            /**
             *  发送普通消息时，topic 必须和接收方订阅的 topic 一致，或者符合通配符匹配规则
             */
            mqttClient.publish(mq4IotTopic, message);
            /**
             * MQ4IoT支持点对点消息，即如果发送方明确知道该消息只需要给特定的一个设备接收，且知道对端的 clientId，则可以直接发送点对点消息。
             * 点对点消息不需要经过订阅关系匹配，可以简化订阅方的逻辑。点对点消息的 topic 格式规范是  {{parentTopic}}/p2p/{{targetClientId}}
             */
            final String p2pSendTopic = parentTopic + "/p2p/" + clientId;
            message = new MqttMessage("hello mq4Iot p2p msg".getBytes());
            message.setQos(qosLevel);
            mqttClient.publish(p2pSendTopic, message);
        }
        Thread.sleep(Long.MAX_VALUE);
    }
}
