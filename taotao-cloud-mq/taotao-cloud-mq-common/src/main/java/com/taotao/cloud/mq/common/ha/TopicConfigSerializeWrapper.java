package com.taotao.cloud.mq.common.ha;

import com.taotao.cloud.mq.common.TopicConfig;
import com.taotao.cloud.mq.common.ha.DataVersion;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TopicConfig 同步包装类（对标 RocketMQ TopicConfigSerializeWrapper）.
 * <p>
 * Master → Slave 全量同步 TopicConfig 时使用, 携带版本号.
 */
public class TopicConfigSerializeWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 声明成 Map 而不是 ConcurrentMap：JsonUtil 只会构造它认识的容器具体类型
     * （Map/List/Collection），认不出的接口类型（如 ConcurrentMap）在反序列化时
     * 会去找接口的构造器并抛 NoSuchMethodException。Map 是超集，调用方不受影响。
     */
    private Map<String, TopicConfig> topicConfigTable = new ConcurrentHashMap<>();
    private DataVersion dataVersion = new DataVersion();

    public TopicConfigSerializeWrapper() {
    }

    public TopicConfigSerializeWrapper(Map<String, TopicConfig> table) {
        if (table != null) {
            this.topicConfigTable.putAll(table);
        }
    }

    public Map<String, TopicConfig> getTopicConfigTable() {
        return topicConfigTable;
    }

    public void setTopicConfigTable(Map<String, TopicConfig> topicConfigTable) {
        this.topicConfigTable = topicConfigTable;
    }

    public DataVersion getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(DataVersion dataVersion) {
        this.dataVersion = dataVersion;
    }
}
