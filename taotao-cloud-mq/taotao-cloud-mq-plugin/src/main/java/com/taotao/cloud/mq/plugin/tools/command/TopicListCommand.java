package com.taotao.cloud.mq.plugin.tools.command;

import com.taotao.cloud.mq.plugin.tools.admin.ClusterAdmin;

import java.util.List;

/**
 * topicList — 查询所有 Topic.
 */
public class TopicListCommand implements Command {

    @Override
    public String name() {
        return "topicList";
    }

    @Override
    public String description() {
        return "查询 NameServer 上所有已注册的 Topic";
    }

    @Override
    public String usage() {
        return "topicList";
    }

    @Override
    public String execute(ClusterAdmin admin, String[] args) {
        List<String> topics = admin.listTopics();
        if (topics.isEmpty()) {
            return "No topics found.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Topics (").append(topics.size()).append("):\n");
        for (String topic : topics) {
            sb.append("  - ").append(topic).append("\n");
        }
        return sb.toString().trim();
    }
}
