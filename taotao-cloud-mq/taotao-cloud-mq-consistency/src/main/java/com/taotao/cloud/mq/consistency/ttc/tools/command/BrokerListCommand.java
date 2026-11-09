package com.taotao.cloud.mq.consistency.ttc.tools.command;

import com.taotao.cloud.mq.consistency.ttc.common.BrokerData;
import com.taotao.cloud.mq.consistency.ttc.tools.admin.ClusterAdmin;

import java.util.List;
import java.util.Map;

/**
 * brokerList — 查询所有 Broker 集群信息.
 */
public class BrokerListCommand implements Command {

    @Override
    public String name() {
        return "brokerList";
    }

    @Override
    public String description() {
        return "查询所有已注册的 Broker 集群信息";
    }

    @Override
    public String usage() {
        return "brokerList";
    }

    @Override
    public String execute(ClusterAdmin admin, String[] args) {
        Map<String, List<BrokerData>> clusters = admin.listBrokers();
        if (clusters.isEmpty()) {
            return "No brokers found.";
        }
        StringBuilder sb = new StringBuilder();
        int total = 0;
        for (Map.Entry<String, List<BrokerData>> entry : clusters.entrySet()) {
            sb.append("Cluster: ").append(entry.getKey()).append("\n");
            for (BrokerData bd : entry.getValue()) {
                sb.append("  - ").append(bd.getBrokerName())
                        .append(" addr=").append(bd.selectBrokerAddr()).append("\n");
                total++;
            }
        }
        sb.insert(0, "Brokers (" + total + "):\n");
        return sb.toString().trim();
    }
}
