package com.taotao.cloud.mqtt.live;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSONObject;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyuncs.utils.StringUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.taotao.cloud.mqtt.demo.TokenApiDemo;

/**
 * @author
 * @date 2020/5/10
 */
public class LiveAppServer {
    private Charset utf8Charset = Charset.forName("utf-8");
    private final String AccessKey = "LTAI4GFaZ5f2ry4FbGjCu9cP";
    private final String SecretKey = "gOPNRxiVoaU71b3TsnW1swlSpPp2Tp";
    private final String GroupId = "GID_test";
    private final String NameAddr = "http://1340373130606955.mqrest.cn-qingdao-public.aliyuncs.com";
    private final String mqttInstanceId = "MQ_INST_1340373130606955_BXSUHFf9";
    private final String regionId = "qingdao";

    private final String MqttSecondTopic = "mqttSecondTopic";
    private final String MsgTypeMessage = "message";
    private final String MsgTypeCommand = "command";
    private final String RoomId = "roomId";
    private final String CommandType = "type";
    private final String ShutUpCommand = "shutUp";

    private String subTopic = "roomSend";
    private String pubTopic = "room";
    private String mqttSecondTopicMessage = "message";
    private String mqttSecondTopicP2p = "p2p";
    private String mqttSecondTopicStatus = "status";
    private String mqttSecondTopicSys = "system";

    private Map<String, Boolean> shutUpClientMap = new ConcurrentHashMap<>(8);
    private Consumer consumer;
    private Producer producer;

    private void start() throws IOException {
        consumer.start();
        producer.start();

        HttpServer loginServer = HttpServer.create(new InetSocketAddress(8081), 0);
        loginServer.createContext("/login", new LoginHandler());
        loginServer.start();
    }

    public static void main(String[] args) throws IOException {
        LiveAppServer liveAppServer = new LiveAppServer();
        liveAppServer.initOnsConsumer();
        liveAppServer.initOnsProducer();
        liveAppServer.start();
    }

    class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            // todo 应用进行身份校验
            try {
                String token = TokenApiDemo.applyToken(AccessKey, SecretKey,
                    Arrays.asList(subTopic + "/#", pubTopic + "/#"),
                    "R,W", 3600 * 1000L, mqttInstanceId, regionId);
                httpExchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                httpExchange.sendResponseHeaders(200, 0);
                OutputStream os = httpExchange.getResponseBody();
                os.write(token.getBytes(utf8Charset));
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void processMsg(Message message) {
        try {
            String mqttSecondTopic = message.getUserProperties(MqttSecondTopic);
            if (mqttSecondTopic.contains(MsgTypeMessage)) {
                sendMessageToMqtt(message.getBody());
            } else if (mqttSecondTopic.contains(MsgTypeCommand)) {
                sendCommandToMqtt(message.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToMqtt(byte[] body) {
        // todo 消息校验
        String message = new String(body, Charset.forName("utf-8"));
        JSONObject jsonObject = JSONObject.parseObject(message);
        Long roomId = jsonObject.getLong(RoomId);
        String peer = jsonObject.getString("peer");
        if (peer != null && Boolean.FALSE.equals(shutUpClientMap.get(peer))) {
            //忽略禁言用户消息
            return;
        }
        if (roomId != null) {
            Message onsMessage = new Message();
            onsMessage.setTopic(pubTopic);
            onsMessage.putUserProperties(MqttSecondTopic, mqttSecondTopicMessage + "/" + roomId);
            onsMessage.setBody(body);
            if (rateLimit()) {
                SendResult sendResult = producer.send(onsMessage);
                System.out.println(sendResult.getMessageId());
            }
        }
    }

    private void sendCommandToMqtt(byte[] body) {
        // todo 命令校验
        String command = new String(body, utf8Charset);
        JSONObject jsonObject = JSONObject.parseObject(command);
        if (ShutUpCommand.equals(jsonObject.getString(CommandType))) {
            String peer = jsonObject.getString("peer");
            if (!StringUtils.isEmpty(peer)) {
                shutUpClientMap.put(peer, false);
                JSONObject toPeer = new JSONObject();
                toPeer.put("type", ShutUpCommand);
                Message onsMessage = new Message();
                onsMessage.setTopic(pubTopic);
                onsMessage.putUserProperties(MqttSecondTopic, mqttSecondTopicP2p + "/" + peer);
                onsMessage.setBody(toPeer.toJSONString().getBytes(utf8Charset));
                if (rateLimit()) {
                    SendResult sendResult = producer.send(onsMessage);
                    System.out.println(sendResult.getMessageId());
                }
            }
        }
    }

    private void sendSysMessageToMqtt(byte[] sysMessage) {
        Message onsMessage = new Message();
        onsMessage.setTopic(pubTopic);
        onsMessage.putUserProperties(MqttSecondTopic, mqttSecondTopicSys);
        onsMessage.setBody(sysMessage);
        if (rateLimit()) {
            SendResult sendResult = producer.send(onsMessage);
            System.out.println(sendResult.getMessageId());
        }
    }

    private void processStatus(Message statusMessage) {
        // 参考文档：https://help.aliyun.com/document_detail/50069.html?spm=a2c4g.11186623.6.556.7ec41e7bCK6jaz

        // 应用后端处理，参考com.aliyun.openservices.lmq.example.demo.MQTTClientStatusNoticeProcessDemo

        // 上下线事件推送至MQTT客户端, 由于可能的消息乱序，客户端的逻辑可以参考后端处理
        Message onsMessage = new Message();
        onsMessage.setTopic(pubTopic);
        onsMessage.putUserProperties(MqttSecondTopic, mqttSecondTopicStatus);
        onsMessage.setBody(statusMessage.getBody());
        if (rateLimit()) {
            producer.send(onsMessage);
        }
    }

    private boolean rateLimit() {
        // todo 限流，终端场景，客户端每秒接受消息量有限，建议每秒不超过10条

        return true;
    }

    public void initOnsProducer() {
        /**
         * 初始化消息队列 for RocketMQ 发送客户端，实际业务中一般部署在服务端应用中。
         */
        Properties properties = new Properties();

        /**
         * 设置 RocketMQ 客户端的 GroupID，注意此处的 groupId 和 MQ4IoT 实例中的 GroupId 是2个概念，请按照各自产品的说明申请填写
         */
        properties.setProperty(PropertyKeyConst.GROUP_ID, GroupId);
        /**
         * 账号 accesskey，从账号系统控制台获取
         */
        properties.put(PropertyKeyConst.AccessKey, AccessKey);
        /**
         * 账号 secretKey，从账号系统控制台获取，仅在Signature鉴权模式下需要设置
         */
        properties.put(PropertyKeyConst.SecretKey, SecretKey);
        /**
         * 设置 TCP 接入域名
         */
        properties.put(PropertyKeyConst.NAMESRV_ADDR, NameAddr);

        producer = ONSFactory.createProducer(properties);
        producer.start();
    }

    public void initOnsConsumer() {
        /**
         * 初始化消息队列 for RocketMQ 接收客户端，实际业务中一般部署在服务端应用中。
         */
        Properties properties = new Properties();
        /**
         * 设置 RocketMQ 客户端的 GroupID，注意此处的 groupId 和 MQ4IoT 实例中的 GroupId 是2个概念，请按照各自产品的说明申请填写
         */
        properties.setProperty(PropertyKeyConst.GROUP_ID, GroupId);
        /**
         * 账号 accesskey，从账号系统控制台获取
         */
        properties.put(PropertyKeyConst.AccessKey, AccessKey);
        /**
         * 账号 secretKey，从账号系统控制台获取，仅在Signature鉴权模式下需要设置
         */
        properties.put(PropertyKeyConst.SecretKey, SecretKey);
        /**
         * 设置 TCP 接入域名
         */
        properties.put(PropertyKeyConst.NAMESRV_ADDR, NameAddr);

        String teacherStatusTopic = "GID_teacher_MQTT";
        String studentStatusTopic = "GID_student_MQTT";

        consumer = ONSFactory.createConsumer(properties);
        consumer.subscribe(subTopic, "*", new MsgListener());

        /**
         *  此处仅处理客户端是否在线，因此只需要关注 connect 事件和 tcpclean 事件即可
         */
        consumer.subscribe(teacherStatusTopic, "connect||tcpclean", new StatusListener());
        consumer.subscribe(studentStatusTopic, "connect||tcpclean", new StatusListener());

        consumer.start();
    }

    class MsgListener implements MessageListener {

        @Override
        public Action consume(Message message, ConsumeContext context) {
            try {
                processMsg(message);
                return Action.CommitMessage;
            } catch (Exception e) {
                e.printStackTrace();
                return Action.ReconsumeLater;
            }
        }
    }

    class StatusListener implements MessageListener {

        @Override
        public Action consume(Message message, ConsumeContext context) {
            try {
                processStatus(message);
                return Action.CommitMessage;
            } catch (Exception e) {
                e.printStackTrace();
                return Action.ReconsumeLater;
            }
        }
    }

}
