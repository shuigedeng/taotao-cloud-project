package com.taotao.cloud.mq.consistency.ttc.tools.command;

import com.taotao.cloud.mq.consistency.ttc.tools.admin.ClusterAdmin;

/**
 * createTopic — 在 Broker 上创建 Topic.
 */
public class CreateTopicCommand implements Command {

    @Override
    public String name() {
        return "createTopic";
    }

    @Override
    public String description() {
        return "在指定 Broker 上创建 Topic";
    }

    @Override
    public String usage() {
        return "createTopic <brokerAddr> <topic> [readQueueNums] [writeQueueNums]";
    }

    @Override
    public String execute(ClusterAdmin admin, String[] args) {
        if (args.length < 2) {
            return "Usage: " + usage();
        }
        String brokerAddr = args[0];
        String topic = args[1];
        int readN = args.length >= 3 ? Integer.parseInt(args[2]) : 4;
        int writeN = args.length >= 4 ? Integer.parseInt(args[3]) : 4;

        boolean ok = admin.createTopic(brokerAddr, topic, readN, writeN);
        if (ok) {
            return "Topic '" + topic + "' created on " + brokerAddr
                    + " (read=" + readN + ", write=" + writeN + ")";
        } else {
            return "Failed to create topic '" + topic + "' on " + brokerAddr;
        }
    }
}
