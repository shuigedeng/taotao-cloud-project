package com.taotao.cloud.mqtt.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class MQTTClientStatusNoticeProcessDemo {
    public static void main(String[] args) {
        /**
         * 初始化消息队列 for RocketMQ 接收客户端，实际业务中一般部署在服务端应用中。
         */
        Properties properties = new Properties();
        /**
         * 设置 RocketMQ 客户端的 GroupID，注意此处的 groupId 和 MQ4IoT 实例中的 GroupId 是2个概念，请按照各自产品的说明申请填写
         */
        properties.setProperty(PropertyKeyConst.GROUP_ID, "GID_XXXX");
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
        properties.put(PropertyKeyConst.NAMESRV_ADDR, "http://XXXX");
        /**
         * 使用 RocketMQ 消费端来处理 MQTT 客户端的上下线通知时，订阅的 topic 为上下线通知 Topic，请遵循控制台文档提前创建。
         */
        final String parentTopic = "GID_XXXX_MQTT";
        /**
         * 客户端状态数据，实际生产环境中建议使用数据库或者 Redis等外部持久化存储来保存该信息，避免应用重启丢失状态,本 demo 以单机内存版实现做演示
         */
        MqttClientStatusStore mqttClientStatusStore = new MemoryHashMapStoreImpl();
        Consumer consumer = ONSFactory.createConsumer(properties);
        /**
         *  此处仅处理客户端是否在线，因此只需要关注 connect 事件和 tcpclean 事件即可
         */
        consumer.subscribe(parentTopic, "connect||tcpclean", new MqttClientStatusNoticeListener(mqttClientStatusStore));
        consumer.start();
        String clientId = "GID_XXXXxXX@@@XXXXX";
        while (true) {
            System.out.println("ClientStatus :" + checkClientOnline(clientId, mqttClientStatusStore));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理上下线通知的逻辑。处理状态机参考文档：https://help.aliyun.com/document_detail/50069.html?spm=a2c4g.11186623.6.554.694f767aUuuUrQ
     * 实际部署过程中，消费上下线通知的应用可能部署多台机器，因此客户端在线状态的数据可以使用数据库或者Redis等外部共享存储来维护。
     * 其次需要单独做消息幂等处理，以免重复接收消息导致状态机判断错误。
     */
    static class MqttClientStatusNoticeListener implements MessageListener {
        private MqttClientStatusStore mqttClientStatusStore;

        public MqttClientStatusNoticeListener(
            MqttClientStatusStore mqttClientStatusStore) {
            this.mqttClientStatusStore = mqttClientStatusStore;
        }

        @Override
        public Action consume(Message message, ConsumeContext context) {
            try {
                JSONObject msgBody = JSON.parseObject(new String(message.getBody()));
                System.out.println(msgBody);
                String eventType = msgBody.getString("eventType");
                String clientId = msgBody.getString("clientId");
                String channelId = msgBody.getString("channelId");
                ClientStatusEvent event = new ClientStatusEvent();
                event.setChannelId(channelId);
                event.setClientIp(msgBody.getString("clientIp"));
                event.setEventType(eventType);
                event.setTime(msgBody.getLong("time"));
                /**
                 * 首先存储新的事件
                 */
                mqttClientStatusStore.addEvent(clientId, channelId, eventType, event);
                /**
                 * 读取当前 channel 的事件列表
                 */
                Set<ClientStatusEvent> events = mqttClientStatusStore.getEvent(clientId, channelId);
                if (events == null || events.isEmpty()) {
                    return Action.CommitMessage;
                }
                /**
                 * 如果事件列表里上线和下线事件都已经收到，则当前 channel 已经掉线，可以清理掉这个 channel 的数据
                 */
                boolean findOnlineEvent = false;
                boolean findOfflineEvent = false;
                for (ClientStatusEvent clientStatusEvent : events) {
                    if (clientStatusEvent.isOnlineEvent()) {
                        findOnlineEvent = true;
                    } else {
                        findOfflineEvent = true;
                    }
                }
                if (findOnlineEvent && findOfflineEvent) {
                    mqttClientStatusStore.deleteEvent(clientId, channelId);
                }
                return Action.CommitMessage;
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return Action.ReconsumeLater;
        }
    }

    /**
     * 根据状态表判断一个 clientId 是否有活跃的 tcp 连接
     * 1.如果没有 channel 表，则一定不在线
     * 2.如果 channel 表非空，检查一下 channel 数据中是否仅包含上线事件,如果有则代表有活跃连接，在线。
     * 如果全部的 channel 都有掉线断开事件则一定是不在线。
     *
     * @param clientId
     * @param mqttClientStatusStore
     * @return
     */
    public static boolean checkClientOnline(String clientId,
        MqttClientStatusStore mqttClientStatusStore) {
        Map<String, Set<ClientStatusEvent>> channelMap = mqttClientStatusStore.getEventsByClientId(clientId);
        if (channelMap == null) {
            return false;
        }
        for (Set<ClientStatusEvent> events : channelMap.values()) {
            boolean findOnlineEvent = false;
            boolean findOfflineEvent = false;
            for (ClientStatusEvent event : events) {
                if (event.isOnlineEvent()) {
                    findOnlineEvent = true;
                } else {
                    findOfflineEvent = true;
                }
            }
            if (findOnlineEvent & !findOfflineEvent) {
                return true;
            }
        }
        return false;
    }

}
