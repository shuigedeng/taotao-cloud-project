package com.taotao.cloud.mq.nameserver.kvconfig;

import com.taotao.boot.common.utils.json.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.type.TypeReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * KV配置管理器
 * 管理NameServer的键值对配置，支持命名空间的配置存储
 */
public class KVConfigManager {

    private static final Logger log = LoggerFactory.getLogger(KVConfigManager.class);

    // 配置文件路径
    private final String configPath;

    // 配置表：namespace -> (key -> value)
    private final Map<String, Map<String, String>> configTable = new HashMap<>();

    public KVConfigManager(String configPath) {
        this.configPath = configPath;
    }

    /**
     * 加载配置
     */
    public void load() {
        File file = new File(configPath);
        if (!file.exists()) {
            log.info("KV config file not found: {}, will create on first save", configPath);
            return;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            String json = new String(data, StandardCharsets.UTF_8);

            Map<String, Map<String, String>> loaded = JacksonUtils.toObject(json,
                    new TypeReference<Map<String, Map<String, String>>>() {
                    });

            if (loaded != null) {
                synchronized (this) {
                    configTable.putAll(loaded);
                }
                log.info("KV config loaded successfully from {}", configPath);
            }
        } catch (Exception e) {
            // JsonUtil 抛的是非受检异常（JsonParseException），原来 Jackson 的
            // JsonParseException 是 IOException 才落在 IOException 这一支上；
            // 语义要求不变：load() 遇到坏 JSON 不能往外抛。
            log.error("Failed to load KV config from {}", configPath, e);
        }
    }

    /**
     * 持久化配置
     */
    public synchronized void persist() {
        try {
            File file = new File(configPath);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            String json = JacksonUtils.toJson(configTable);
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(json);
            }
            log.info("KV config persisted successfully to {}", configPath);
        } catch (IOException e) {
            log.error("Failed to persist KV config to {}", configPath, e);
        }
    }

    /**
     * 添加/更新配置
     */
    public synchronized void putConfig(String namespace, String key, String value) {
        configTable.computeIfAbsent(namespace, k -> new HashMap<>()).put(key, value);
    }

    /**
     * 获取配置
     */
    public synchronized String getConfig(String namespace, String key) {
        Map<String, String> namespaceConfig = configTable.get(namespace);
        return namespaceConfig != null ? namespaceConfig.get(key) : null;
    }

    /**
     * 删除配置
     */
    public synchronized void deleteConfig(String namespace, String key) {
        Map<String, String> namespaceConfig = configTable.get(namespace);
        if (namespaceConfig != null) {
            namespaceConfig.remove(key);
            if (namespaceConfig.isEmpty()) {
                configTable.remove(namespace);
            }
        }
    }

    /**
     * 获取命名空间下的所有配置
     */
    public synchronized Map<String, String> getConfigsByNamespace(String namespace) {
        Map<String, String> namespaceConfig = configTable.get(namespace);
        return namespaceConfig != null ? new HashMap<>(namespaceConfig) : new HashMap<>();
    }

    /**
     * 获取所有配置表（副本）
     */
    public synchronized Map<String, Map<String, String>> getAllConfigs() {
        Map<String, Map<String, String>> result = new HashMap<>();
        for (Map.Entry<String, Map<String, String>> entry : configTable.entrySet()) {
            result.put(entry.getKey(), new HashMap<>(entry.getValue()));
        }
        return result;
    }

    /**
     * 获取所有命名空间
     */
    public synchronized Set<String> getAllNamespaces() {
        return new HashSet<>(configTable.keySet());
    }

    /**
     * 清空所有配置
     */
    public synchronized void clearAll() {
        configTable.clear();
    }

    /**
     * 打印所有配置（用于日志）
     */
    public synchronized void printAllPeriodically() {
        log.info("--------------------------------------------------------");
        log.info("KV Config Table: ");
        for (Map.Entry<String, Map<String, String>> namespaceEntry : configTable.entrySet()) {
            String namespace = namespaceEntry.getKey();
            log.info("  Namespace: {}", namespace);
            for (Map.Entry<String, String> configEntry : namespaceEntry.getValue().entrySet()) {
                log.info("    {} = {}", configEntry.getKey(), configEntry.getValue());
            }
        }
        log.info("--------------------------------------------------------");
    }
}
