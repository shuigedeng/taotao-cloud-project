package com.taotao.cloud.mqtt.demo;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;
import java.util.Properties;
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
 * 本代码提供消息队列 For RocketMQ 客户端发消息到 MQ4IoT 客户端的示例，其中初始化参数请根据实际情况修改
 * 阿里云生产环境中 消息队列 For RocketMQ 除了公网 region 之外，其他 region 不允许在本地使用，必须在对应区域的 ECS 机器上部署使用。
 * 本示例代码仅供参考，RocketMQ 客户端部分的逻辑请根据实际情况部署到对应区域测试。
 * MQ4IoT和 RocketMQ 配合使用的详细文档请参考链接。https://help.aliyun.com/document_detail/94521.html?spm=a2c4g.11186623.6.543.694f78717ugCj1
 */
public class RocketMQSendMessageToMQ4IoT {
    public static void main(String[] args) throws Exception {
        /**
         * 初始化消息队列 for RocketMQ 发送客户端，实际业务中一般部署在服务端应用中。
         */
        Properties properties = new Properties();
        /**
         * 设置 RocketMQ 客户端的 GroupID，注意此处的 groupId 和 MQ4IoT 实例中的 GroupId 是2个概念，请按照各自产品的说明申请填写
         */
        properties.setProperty(PropertyKeyConst.GROUP_ID, "GID-XXXXX");
        /**
         * 账号 accesskey，从账号系统控制台获取
         */
        properties.put(PropertyKeyConst.AccessKey, "XXXX");
        /**
         * 账号 secretKey，从账号系统控制台获取，仅在Signature鉴权模式下需要设置
         */
        properties.put(PropertyKeyConst.SecretKey, "XXXX");
        /**
         * 设置 TCP 接入域名
         */
        properties.put(PropertyKeyConst.NAMESRV_ADDR, "http://xxxxx.cn-qingdao-publictest.mq-internet.aliyuncs.com");
        /**
         * MQ4IoT 和 RocketMQ 配合使用时，RocketMQ 客户端仅操作一级 Topic。
         */
        final String parentTopic = "XXXXX";
        Producer producer = ONSFactory.createProducer(properties);
        producer.start();
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /**
         * 初始化 MQ4IoT 接收客户端，实际业务中 MQ4IoT 一般部署在移动终端环境
         */

        /**
         * MQ4IOT 实例 ID，购买后控制台获取
         */
        String instanceId = "XXXXX";
        /**
         * 接入点地址，购买 MQ4IOT 实例，且配置完成后即可获取，接入点地址必须填写分配的域名，不得使用 IP 地址直接连接，否则可能会导致客户端异常。
         */
        String endPoint = "XXXXXX.mqtt.aliyuncs.com";
        /**
         * 账号 accesskey，从账号系统控制台获取
         */
        String accessKey = "XXXX";
        /**
         * 账号 secretKey，从账号系统控制台获取，仅在Signature鉴权模式下需要设置
         */
        String secretKey = "XXXX";
        /**
         * MQ4IOT clientId，由业务系统分配，需要保证每个 tcp 连接都不一样，保证全局唯一，如果不同的客户端对象（tcp 连接）使用了相同的 clientId 会导致连接异常断开。
         * clientId 由两部分组成，格式为 GroupID@@@DeviceId，其中 groupId 在 MQ4IOT 控制台申请，DeviceId 由业务方自己设置，clientId 总长度不得超过64个字符。
         */
        String clientId = "GID_XXXX@@@XXXXX";
        /**
         * MQ4IOT支持子级 topic，用来做自定义的过滤，此处为示意，可以填写任何字符串，具体参考https://help.aliyun.com/document_detail/42420.html?spm=a2c4g.11186623.6.544.1ea529cfAO5zV3
         * 需要注意的是，完整的 topic 长度不得超过128个字符。
         */
        final String subTopic = "/testMq4Iot";
        final String mq4IotTopic = parentTopic + subTopic;
        /**
         * QoS参数代表传输质量，可选0，1，2，根据实际需求合理设置，具体参考 https://help.aliyun.com/document_detail/42420.html?spm=a2c4g.11186623.6.544.1ea529cfAO5zV3
         */
        final int qosLevel = 0;
        ConnectionOptionWrapper connectionOptionWrapper = new ConnectionOptionWrapper(instanceId, accessKey, secretKey, clientId);
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
            /**
             * 使用 RocketMQ 客户端发消息给 MQ4IoT客户端时，Topic 指定为一级父Topic，Tag指定为 MQ2MQTT
             */
            Message msg = new Message(parentTopic, "MQ2MQTT", "hello mq send mqtt msg".getBytes());
            /**
             * 使用 RocketMQ 客户端发消息给 MQ4IoT客户端时，可以通过mqttSecondTopic属性设置 MQ4IoT的子级 topic 属性
             * 其他参数例如 QoS， CleanSession 参数等都可以设置，具体可以参考文档https://help.aliyun.com/document_detail/94521.html?spm=a2c4g.11186623.6.543.694f78717ugCj1
             *
             */
            msg.putUserProperties(PropertyKeyConst.MqttSecondTopic, subTopic);
            SendResult result = producer.send(msg);
            System.out.println(result);
            /**
             * 发送 P2P 消息时，子级 topic如下设置， 具体参考https://help.aliyun.com/document_detail/96176.html?spm=a2c4g.11186623.6.565.10ac1cf7NKGfjI
             */
            msg.putUserProperties(PropertyKeyConst.MqttSecondTopic, "/p2p/" + clientId);
            result = producer.send(msg);
            System.out.println(result);
        }
        Thread.sleep(Long.MAX_VALUE);

    }

}
