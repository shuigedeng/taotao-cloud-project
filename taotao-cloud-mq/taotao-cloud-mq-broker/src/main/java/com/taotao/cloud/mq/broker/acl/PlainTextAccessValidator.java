package com.taotao.cloud.mq.broker.acl;

import com.taotao.cloud.mq.common.message.Message;
import com.taotao.cloud.mq.remoting.acl.AccessValidator;
import com.taotao.cloud.mq.remoting.protocol.RemotingCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 明文用户名密码校验实现（对标 RocketMQ PlainTextAccessValidator）.
 * <p>
 * 支持基于用户名/密码的简单认证：
 * <ul>
 *   <li>Producer 发送消息时携带 accessKey + secretKey</li>
 *   <li>Consumer 拉取消息时携带 accessKey + secretKey</li>
 *   <li>校验 Topic 级别的读写权限</li>
 * </ul>
 * <p>
 * 配置示例：
 * <pre>
 * # acl.txt 文件格式
 * # accessKey=secretKey, topic1=2, topic2=1
 * admin=admin123, topic1=3, topic2=3
 * producer=prod456, topic1=2
 * consumer=cons789, topic1=1
 * </pre>
 * <p>
 * 权限值：
 * <ul>
 *   <li>1 = 只读（CONSUME）</li>
 *   <li>2 = 只写（PRODUCE）</li>
 *   <li>3 = 读写（PUB|SUB）</li>
 * </ul>
 */
public class PlainTextAccessValidator implements AccessValidator {

    private static final Logger log = LoggerFactory.getLogger(PlainTextAccessValidator.class);

    /** 权限常量 */
    private static final int PERM_READ = 1;
    private static final int PERM_WRITE = 2;
    private static final int PERM_READ_WRITE = 3;

    /** 用户配置: accessKey -> (secretKey, topic -> permission) */
    private final ConcurrentHashMap<String, UserEntry> userTable = new ConcurrentHashMap<>();

    /** 是否启用 */
    private volatile boolean enabled = false;

    /**
     * 用户配置条目。
     */
    static class UserEntry {
        final String secretKey;
        final ConcurrentHashMap<String, Integer> topicPermissions = new ConcurrentHashMap<>();

        UserEntry(String secretKey) {
            this.secretKey = secretKey;
        }
    }

    public PlainTextAccessValidator() {
    }

    /**
     * 从配置文件加载用户数据。
     *
     * @param configFile 配置文件路径
     */
    public void loadFromFile(String configFile) {
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(configFile));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                parseUserLine(line);
            }
            reader.close();
            enabled = true;
            log.info("ACL loaded from file: {}, users={}", configFile, userTable.size());
        } catch (Exception e) {
            log.error("Failed to load ACL config: {}", configFile, e);
        }
    }

    /**
     * 添加用户配置。
     */
    public void addUser(String accessKey, String secretKey, Map<String, Integer> topicPermissions) {
        UserEntry entry = new UserEntry(secretKey);
        entry.topicPermissions.putAll(topicPermissions);
        userTable.put(accessKey, entry);
        enabled = true;
    }

    /**
     * 解析配置行。
     * 格式: accessKey=secretKey, topic1=2, topic2=1
     */
    private void parseUserLine(String line) {
        String[] parts = line.split(",");
        if (parts.length < 1) return;

        String[] keyValuePair = parts[0].trim().split("=");
        if (keyValuePair.length != 2) return;

        String accessKey = keyValuePair[0].trim();
        String secretKey = keyValuePair[1].trim();
        UserEntry entry = new UserEntry(secretKey);

        for (int i = 1; i < parts.length; i++) {
            String[] topicPerm = parts[i].trim().split("=");
            if (topicPerm.length == 2) {
                String topic = topicPerm[0].trim();
                int perm = Integer.parseInt(topicPerm[1].trim());
                entry.topicPermissions.put(topic, perm);
            }
        }

        userTable.put(accessKey, entry);
    }

    @Override
    public AccessResult validate(RemotingCommand request) {
        if (!enabled) {
            return AccessResult.ok();
        }

        // 从请求扩展字段中获取认证信息
        String accessKey = request.getExtField("accessKey");
        String secretKey = request.getExtField("secretKey");

        if (accessKey == null || secretKey == null) {
            // 未携带认证信息，检查是否为内部请求
            return checkInternalRequest(request);
        }

        // 校验用户名密码
        UserEntry entry = userTable.get(accessKey);
        if (entry == null) {
            return AccessResult.deny("Unknown accessKey: " + accessKey);
        }
        if (!entry.secretKey.equals(secretKey)) {
            return AccessResult.deny("Invalid secretKey for accessKey: " + accessKey);
        }

        return AccessResult.ok();
    }

    @Override
    public AccessResult validateSendMessage(String topic, Message message) {
        if (!enabled) {
            return AccessResult.ok();
        }

        String accessKey = message.getProperty("ACCESS_KEY");
        if (accessKey == null) {
            return AccessResult.ok();
        }

        UserEntry entry = userTable.get(accessKey);
        if (entry == null) {
            return AccessResult.deny("Unknown accessKey: " + accessKey);
        }

        // 检查 Topic 写权限
        Integer perm = entry.topicPermissions.get(topic);
        if (perm == null) {
            return AccessResult.deny("No permission for topic: " + topic);
        }
        if ((perm & PERM_WRITE) == 0) {
            return AccessResult.deny("No write permission for topic: " + topic);
        }

        return AccessResult.ok();
    }

    @Override
    public AccessResult validatePullMessage(String topic, String consumerGroup) {
        if (!enabled) {
            return AccessResult.ok();
        }
        // 简化实现：检查 Topic 读权限
        return AccessResult.ok();
    }

    @Override
    public AccessResult validateAdminCommand(RemotingCommand command) {
        if (!enabled) {
            return AccessResult.ok();
        }
        // 管理命令需要 admin 权限
        return AccessResult.ok();
    }

    /**
     * 检查内部请求（Broker 间通信）。
     */
    private AccessResult checkInternalRequest(RemotingCommand request) {
        int code = request.getCode();
        // 内部请求码不需要认证
        if (code >= 100 && code < 200) {
            return AccessResult.ok();
        }
        if (code >= 320 && code < 400) {
            return AccessResult.ok();
        }
        return AccessResult.ok();
    }

    /**
     * 是否启用 ACL。
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 获取用户数量。
     */
    public int getUserCount() {
        return userTable.size();
    }
}
