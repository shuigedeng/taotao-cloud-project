package com.taotao.cloud.mq.plugin.tools.command;

import com.taotao.cloud.mq.plugin.tools.admin.ClusterAdmin;

/**
 * CLI 命令接口.
 */
public interface Command {

    /**
     * 命令名 (如 "topicList", "topicRoute", "brokerList").
     */
    String name();

    /**
     * 命令描述.
     */
    String description();

    /**
     * 用法提示.
     */
    String usage();

    /**
     * 执行命令.
     *
     * @param admin   集群管理客户端
     * @param args    命令参数
     * @return 输出结果
     */
    String execute(ClusterAdmin admin, String[] args);
}
