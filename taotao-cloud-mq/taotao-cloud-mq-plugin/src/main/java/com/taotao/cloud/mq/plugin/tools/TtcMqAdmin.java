package com.taotao.cloud.mq.plugin.tools;

import com.taotao.cloud.mq.plugin.tools.admin.ClusterAdmin;
import com.taotao.cloud.mq.plugin.tools.command.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ttc-MQ 集群运维 CLI 入口.
 * <p>
 * 用法:
 * <pre>
 * # 查看帮助
 * java -cp ttc-mq-tools.jar com.taotao.cloud.mq.plugin.TtcMqAdmin help
 *
 * # 列出所有 Topic
 * java -cp ttc-mq-tools.jar com.taotao.cloud.mq.plugin.TtcMqAdmin topicList --namesrv localhost:9876
 *
 * # 查看 Topic 路由
 * java -cp ttc-mq-tools.jar com.taotao.cloud.mq.plugin.TtcMqAdmin topicRoute --namesrv localhost:9876 my-topic
 *
 * # 列出所有 Broker
 * java -cp ttc-mq-tools.jar com.taotao.cloud.mq.plugin.TtcMqAdmin brokerList --namesrv localhost:9876
 *
 * # 创建 Topic
 * java -cp ttc-mq-tools.jar com.taotao.cloud.mq.plugin.TtcMqAdmin createTopic --namesrv localhost:9876 localhost:10911 my-topic 4 4
 * </pre>
 */
public class TtcMqAdmin {

    private final Map<String, Command> commands = new LinkedHashMap<>();
    private String namesrvAddr = "localhost:9876";

    public TtcMqAdmin() {
        registerCommand(new TopicListCommand());
        registerCommand(new TopicRouteCommand());
        registerCommand(new BrokerListCommand());
        registerCommand(new CreateTopicCommand());
    }

    private void registerCommand(Command cmd) {
        commands.put(cmd.name(), cmd);
    }

    /**
     * 执行 CLI 命令.
     *
     * @param args 命令行参数
     * @return 输出结果
     */
    public String execute(String[] args) {
        // 解析 --namesrv 参数
        String commandName = null;
        String[] cmdArgs;
        int i = 0;

        while (i < args.length) {
            if ("--namesrv".equals(args[i]) && i + 1 < args.length) {
                namesrvAddr = args[i + 1];
                i += 2;
            } else if (commandName == null) {
                commandName = args[i];
                i++;
            } else {
                break;
            }
        }

        cmdArgs = new String[args.length - i];
        System.arraycopy(args, i, cmdArgs, 0, cmdArgs.length);

        // help 或无参数
        if (commandName == null || "help".equals(commandName)) {
            return showHelp();
        }

        Command command = commands.get(commandName);
        if (command == null) {
            return "Unknown command: " + commandName + "\n" + showHelp();
        }

        ClusterAdmin admin = new ClusterAdmin(namesrvAddr);
        admin.start();
        try {
            return command.execute(admin, cmdArgs);
        } finally {
            admin.shutdown();
        }
    }

    private String showHelp() {
        StringBuilder sb = new StringBuilder();
        sb.append("ttc-MQ Cluster Admin CLI\n");
        sb.append("======================\n\n");
        sb.append("Usage: TtcMqAdmin [--namesrv host:port] <command> [args]\n\n");
        sb.append("Commands:\n");
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            Command cmd = entry.getValue();
            sb.append(String.format("  %-16s %s\n", cmd.name(), cmd.description()));
            sb.append(String.format("  %16s Usage: %s\n", "", cmd.usage()));
        }
        sb.append("\nExamples:\n");
        sb.append("  TtcMqAdmin topicList\n");
        sb.append("  TtcMqAdmin topicRoute my-topic\n");
        sb.append("  TtcMqAdmin brokerList\n");
        sb.append("  TtcMqAdmin createTopic localhost:10911 my-topic 4 4\n");
        return sb.toString();
    }

    /** 获取已注册的命令 (供测试使用). */
    public Map<String, Command> getCommands() {
        return commands;
    }

    /** 设置 NameServer 地址 (供测试使用). */
    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    // ===== Main =====

    public static void main(String[] args) {
        TtcMqAdmin admin = new TtcMqAdmin();
        String result = admin.execute(args);
        System.out.println(result);
    }
}
