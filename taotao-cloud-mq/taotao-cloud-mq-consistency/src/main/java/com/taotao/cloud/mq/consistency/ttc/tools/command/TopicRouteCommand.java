package com.taotao.cloud.mq.consistency.ttc.tools.command;

import com.taotao.cloud.mq.consistency.ttc.common.BrokerData;
import com.taotao.cloud.mq.consistency.ttc.common.QueueData;
import com.taotao.cloud.mq.consistency.ttc.common.TopicRouteData;
import com.taotao.cloud.mq.consistency.ttc.tools.admin.ClusterAdmin;

/**
 * topicRoute — 查询 Topic 路由信息.
 */
public class TopicRouteCommand implements Command {

    @Override
    public String name() {
        return "topicRoute";
    }

    @Override
    public String description() {
        return "查询指定 Topic 的路由信息 (Broker 列表 + 队列配置)";
    }

    @Override
    public String usage() {
        return "topicRoute <topic>";
    }

    @Override
    public String execute(ClusterAdmin admin, String[] args) {
        if (args.length < 1) {
            return "Usage: " + usage();
        }
        String topic = args[0];
        TopicRouteData route = admin.topicRoute(topic);
        if (route == null) {
            return "Topic '" + topic + "' not found or no route available.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Topic: ").append(topic).append("\n");

        // Broker 列表
        if (route.getBrokerDatas() != null && !route.getBrokerDatas().isEmpty()) {
            sb.append("Brokers:\n");
            for (BrokerData bd : route.getBrokerDatas()) {
                sb.append("  - ").append(bd.getBrokerName())
                        .append(" master=").append(bd.selectBrokerAddr()).append("\n");
            }
        }

        // 队列配置
        if (route.getQueueDatas() != null && !route.getQueueDatas().isEmpty()) {
            sb.append("Queue Config:\n");
            for (QueueData qd : route.getQueueDatas()) {
                sb.append("  - ").append(qd.getBrokerName())
                        .append(" read=").append(qd.getReadQueueNums())
                        .append(" write=").append(qd.getWriteQueueNums()).append("\n");
            }
        }

        return sb.toString().trim();
    }
}
