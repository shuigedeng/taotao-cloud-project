package com.taotao.cloud.mq.consistency.ttc.tools;

import com.taotao.cloud.mq.consistency.ttc.tools.admin.ClusterAdmin;
import com.taotao.cloud.mq.consistency.ttc.tools.command.BrokerListCommand;
import com.taotao.cloud.mq.consistency.ttc.tools.command.Command;
import com.taotao.cloud.mq.consistency.ttc.tools.command.CreateTopicCommand;
import com.taotao.cloud.mq.consistency.ttc.tools.command.TopicListCommand;
import com.taotao.cloud.mq.consistency.ttc.tools.command.TopicRouteCommand;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Z-MQ 集群运维 CLI 入口.
 * <p>
 * 用法:
 * <pre>
 * # 查看帮助
 * java -cp z-mq-tools.jar com.taotao.cloud.mq.consistency.ttc.tools.ZmqAdmin help
 *
 * # 列出所有 Topic
 * java -cp z-mq-tools.jar com.taotao.cloud.mq.consistency.ttc.tools.ZmqAdmin topicList --namesrv localhost:9876
 *
 * # 查看 Topic 路由
 * java -cp z-mq-tools.jar com.taotao.cloud.mq.consistency.ttc.tools.ZmqAdmin topicRoute --namesrv localhost:9876 my-topic
 *
 * # 列出所有 Broker
 * java -cp z-mq-tools.jar com.taotao.cloud.mq.consistency.ttc.tools.ZmqAdmin brokerList --namesrv localhost:9876
 *
 * # 创建 Topic
 * java -cp z-mq-tools.jar com.taotao.cloud.mq.consistency.ttc.tools.ZmqAdmin createTopic --namesrv localhost:9876 localhost:10911 my-topic 4 4
 * </pre>
 */
public class ZmqAdmin {

    private final Map<String, Command> commands = new LinkedHashMap<>();
    private String namesrvAddr = "localhost:9876";

    public ZmqAdmin() {
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
        sb.append("Z-MQ Cluster Admin CLI\n");
        sb.append("======================\n\n");
        sb.append("Usage: ZmqAdmin [--namesrv host:port] <command> [args]\n\n");
        sb.append("Commands:\n");
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            Command cmd = entry.getValue();
            sb.append(String.format("  %-16s %s\n", cmd.name(), cmd.description()));
            sb.append(String.format("  %16s Usage: %s\n", "", cmd.usage()));
        }
        sb.append("\nExamples:\n");
        sb.append("  ZmqAdmin topicList\n");
        sb.append("  ZmqAdmin topicRoute my-topic\n");
        sb.append("  ZmqAdmin brokerList\n");
        sb.append("  ZmqAdmin createTopic localhost:10911 my-topic 4 4\n");
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
        ZmqAdmin admin = new ZmqAdmin();
        String result = admin.execute(args);
        System.out.println(result);
    }
}
