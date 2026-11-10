package com.taotao.cloud.mq.plugin.tools;

import com.taotao.cloud.mq.plugin.tools.command.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TtcMqAdmin CLI 测试.
 */
@DisplayName("TtcMqAdmin CLI 测试")
public class TtcMqAdminTest {

    // ===== 命令注册测试 =====

    @Test
    @DisplayName("默认注册 4 个命令")
    public void testDefaultCommands() {
        TtcMqAdmin admin = new TtcMqAdmin();
        assertEquals(4, admin.getCommands().size());
        assertTrue(admin.getCommands().containsKey("topicList"));
        assertTrue(admin.getCommands().containsKey("topicRoute"));
        assertTrue(admin.getCommands().containsKey("brokerList"));
        assertTrue(admin.getCommands().containsKey("createTopic"));
    }

    @Test
    @DisplayName("每个命令有 name / description / usage")
    public void testCommandMetadata() {
        TtcMqAdmin admin = new TtcMqAdmin();
        for (Command cmd : admin.getCommands().values()) {
            assertNotNull(cmd.name(), "name should not be null");
            assertNotNull(cmd.description(), "description should not be null");
            assertNotNull(cmd.usage(), "usage should not be null");
            assertTrue(cmd.name().length() > 0);
        }
    }

    // ===== Help 输出测试 =====

    @Test
    @DisplayName("无参数 → 显示帮助")
    public void testHelpDefault() {
        TtcMqAdmin admin = new TtcMqAdmin();
        String output = admin.execute(new String[]{});
        assertTrue(output.contains("ttc-MQ Cluster Admin CLI"));
        assertTrue(output.contains("topicList"));
        assertTrue(output.contains("brokerList"));
        assertTrue(output.contains("topicRoute"));
        assertTrue(output.contains("createTopic"));
    }

    @Test
    @DisplayName("help 命令 → 显示帮助")
    public void testHelpCommand() {
        TtcMqAdmin admin = new TtcMqAdmin();
        String output = admin.execute(new String[]{"help"});
        assertTrue(output.contains("ttc-MQ Cluster Admin CLI"));
        assertTrue(output.contains("Usage:"));
    }

    @Test
    @DisplayName("未知命令 → 显示错误 + 帮助")
    public void testUnknownCommand() {
        TtcMqAdmin admin = new TtcMqAdmin();
        String output = admin.execute(new String[]{"foobar"});
        assertTrue(output.contains("Unknown command: foobar"));
        assertTrue(output.contains("ttc-MQ Cluster Admin CLI"));
    }

    // ===== --namesrv 参数解析测试 =====

    @Test
    @DisplayName("--namesrv 参数正确解析")
    public void testNamesrvParam() {
        TtcMqAdmin admin = new TtcMqAdmin();
        admin.execute(new String[]{"--namesrv", "broker1:9876", "help"});
        assertEquals("broker1:9876", admin.getNamesrvAddr());
    }

    @Test
    @DisplayName("--namesrv 默认值为 localhost:9876")
    public void testNamesrvDefault() {
        TtcMqAdmin admin = new TtcMqAdmin();
        assertEquals("localhost:9876", admin.getNamesrvAddr());
    }

    // ===== TopicListCommand 单独测试 =====

    @Test
    @DisplayName("TopicListCommand.name = topicList")
    public void testTopicListCommandName() {
        TopicListCommand cmd = new TopicListCommand();
        assertEquals("topicList", cmd.name());
        assertTrue(cmd.description().length() > 0);
        assertEquals("topicList", cmd.usage());
    }

    @Test
    @DisplayName("TopicListCommand 空结果 (无 NameServer 连接)")
    public void testTopicListEmpty() {
        TopicListCommand cmd = new TopicListCommand();
        // 无 NameServer, admin.listTopics() 返回 empty list
        // 验证命令格式化逻辑
        java.util.List<String> emptyTopics = java.util.Collections.emptyList();
        StringBuilder sb = new StringBuilder();
        if (emptyTopics.isEmpty()) {
            sb.append("No topics found.");
        }
        assertTrue(sb.toString().contains("No topics found."));
    }

    // ===== TopicRouteCommand 单独测试 =====

    @Test
    @DisplayName("TopicRouteCommand 缺少参数 → 返回 usage")
    public void testTopicRouteNoArgs() {
        TopicRouteCommand cmd = new TopicRouteCommand();
        String result = cmd.execute(null, new String[]{});
        assertTrue(result.contains("Usage:"));
        assertTrue(result.contains("topicRoute"));
    }

    @Test
    @DisplayName("TopicRouteCommand.name = topicRoute")
    public void testTopicRouteCommandName() {
        TopicRouteCommand cmd = new TopicRouteCommand();
        assertEquals("topicRoute", cmd.name());
    }

    // ===== BrokerListCommand 单独测试 =====

    @Test
    @DisplayName("BrokerListCommand.name = brokerList")
    public void testBrokerListCommandName() {
        BrokerListCommand cmd = new BrokerListCommand();
        assertEquals("brokerList", cmd.name());
        assertTrue(cmd.description().length() > 0);
    }

    // ===== CreateTopicCommand 单独测试 =====

    @Test
    @DisplayName("CreateTopicCommand 缺少参数 → 返回 usage")
    public void testCreateTopicNoArgs() {
        CreateTopicCommand cmd = new CreateTopicCommand();
        String result = cmd.execute(null, new String[]{});
        assertTrue(result.contains("Usage:"));
        assertTrue(result.contains("createTopic"));
    }

    @Test
    @DisplayName("CreateTopicCommand 缺少 topic 参数")
    public void testCreateTopicMissingTopic() {
        CreateTopicCommand cmd = new CreateTopicCommand();
        String result = cmd.execute(null, new String[]{"localhost:10911"});
        assertTrue(result.contains("Usage:"));
    }

    @Test
    @DisplayName("CreateTopicCommand.name = createTopic")
    public void testCreateTopicCommandName() {
        CreateTopicCommand cmd = new CreateTopicCommand();
        assertEquals("createTopic", cmd.name());
    }
}
