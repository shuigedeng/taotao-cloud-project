package com.taotao.cloud.mqtt.demo;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.order.ConsumeOrderContext;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.ons.api.order.OrderAction;
import com.aliyun.openservices.ons.api.order.OrderConsumer;
import java.util.Properties;

import com.taotao.cloud.mqtt.util.ConnectionOptionWrapper;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * 本代码提供 MQ4IOT 客户端发送顺序消息到 消息队列 For RocketMQ 客户端的示例，其中初始化参数请根据实际情况修改
 * 阿里云生产环境中 消息队列 For RocketMQ 除了公网 region 之外，其他 region 不允许在本地使用，必须在对应区域的 ECS 机器上部署使用。
 * 本示例代码仅供参考，RocketMQ 客户端部分的逻辑请根据实际情况部署到对应区域测试。
 * MQ4IoT和 RocketMQ 配合使用的详细文档请参考链接。https://help.aliyun.com/document_detail/94521.html?spm=a2c4g.11186623.6.543.694f78717ugCj1
 *
 * MQ4IoT客户端发送顺序消息的场景，服务端仅支持单个客户端的消息顺序，不同的客户端的消息是不保证顺序的。
 *
 * 该场景需要满足三点条件：
 * 1. 创建 Topic 时必须选择顺序 topic。
 * 使用 MQ4IoT 发顺序消息，首先需要保证 topic 本身是支持顺序的，即 topic 的创建必须选择分区顺序或者全局顺序类型。如果使用普通 topic 发送，即使程序用法正确，也无法保证顺序性。
 *
 * 2. MQ4IoT客户端初始化时设置顺序属性。
 * MQ4IoT 客户端默认上行发送消息是乱序的，如果需要使用顺序场景，需要参考本文档设置顺序属性。
 *
 * 3. 必须要使用 RocketMQ 协议的顺序客户端消费。
 * 目前仅支持 MQ4IoT 客户端发送顺序消息，并使用 RocketMQ 客户端顺序消费的场景，使用 MQ4IoT 客户端消费暂时不支持顺序消费。
 * 对应的业务场景，例如物联网领域里只需要保证每个传感器采集的数据是顺序上传即可。消息的处理方，即服务端应用可以使用 RocketMQ 协议的 SDK 来做顺序消费。
 *
 */
public class MQ4IoTSendOrderMessageToRocketMQ {
    public static void main(String[] args) throws Exception {
        /**
         * 初始化消息队列 for RocketMQ 接收客户端，实际业务中一般部署在服务端应用中。
         */
        Properties properties = new Properties();
        /**
         * 设置 RocketMQ 客户端的 GroupID，注意此处的 groupId 和 MQ4IoT 实例中的 GroupId 是2个概念，请按照各自产品的说明申请填写
         */
        properties.setProperty(PropertyKeyConst.GROUP_ID, "GID-XXXXX");
        /**
         * 账号 accesskey，从账号系统控制台获取
         */
        properties.put(PropertyKeyConst.AccessKey,"XXXX");
        /**
         * 账号 secretKey，从账号系统控制台获取，仅在Signature鉴权模式下需要设置
         */
        properties.put(PropertyKeyConst.SecretKey, "XXXX");
        /**
         * 设置 TCP 接入域名
         */
        properties.put(PropertyKeyConst.NAMESRV_ADDR, "http://xxxxx.cn-qingdao-publictest.mq-internet.aliyuncs.com");
        /**
         * MQ4IoT 和 RocketMQ 配合使用时，RocketMQ 客户端仅操作一级 Topic。顺序消息必须使用顺序 Consumer，否则无法保证强顺序。
         */
        final String parentTopic = "XXXXX";
        Properties initProperties = new Properties();
        OrderConsumer consumer = ONSFactory.createOrderedConsumer(initProperties);
        consumer.subscribe(parentTopic, "*", new MessageOrderListener() {
            @Override
            public OrderAction consume(Message message, ConsumeOrderContext context) {
                System.out.println("recv msg:" + message);
                return OrderAction.Success;
            }
        });
        consumer.start();
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /**
         * 初始化 MQ4IoT 发送客户端，实际业务中 MQ4IoT 一般部署在移动终端环境
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
        final String mq4IotTopic = parentTopic + "/" + "testMq4Iot";
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
        mqttClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                System.out.println("connect success");
                //连接成功，需要上传客户端的顺序控制报文
                /**
                 * 客户端连接成功后就需要立即初始化顺序属性，然后再进行业务的发送消息。
                 */
                JSONObject object = new JSONObject();
                object.put("order", "true");//设置顺序发送的标记
                MqttMessage message = new MqttMessage(object.toJSONString().getBytes());
                message.setQos(1);
                try {
                    /**
                     * $SYS 开头的 topic 都是系统控制类型的 topic，具体的子级 Topic 都有特定的控制含义，比如enableOrderMsg代表设置客户端的顺序属性，
                     * 设置该属性后，当前客户端发送的消息都会分布到服务端同一个分区内。
                     */
                    mqttClient.publish("$SYS/enableOrderMsg", message);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                System.out.println("send msg succeed topic is : " + iMqttDeliveryToken.getTopics()[0]);
            }
        });
        mqttClient.connect(connectionOptionWrapper.getMqttConnectOptions());
        for (int i = 0; i < 10; i++) {
            MqttMessage message = new MqttMessage("hello mq4Iot order msg".getBytes());
            message.setQos(qosLevel);
            /**
             *  发送普通消息时，topic 必须和接收方订阅的 topic 一致，或者符合通配符匹配规则
             */
            mqttClient.publish(mq4IotTopic, message);
        }
        Thread.sleep(Long.MAX_VALUE);

    }

}
