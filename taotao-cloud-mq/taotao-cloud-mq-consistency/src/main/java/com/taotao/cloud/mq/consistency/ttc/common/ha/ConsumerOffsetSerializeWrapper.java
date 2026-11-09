package com.taotao.cloud.mq.consistency.ttc.common.ha;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * ConsumerOffset 同步包装类（对标 RocketMQ ConsumerOffsetSerializeWrapper）.
 * <p>
 * Master → Slave 全量同步 ConsumerOffset 时使用, 携带版本号.
 * <p>
 * 内部数据格式:
 * <pre>
 * {
 *   "topicName1": { "queueId1": offset1, "queueId2": offset2 },
 *   "topicName2": { "queueId1": offset3 }
 * }
 * </pre>
 * <p>
 * Slave 启动时若版本号 > 本地, 则全量覆盖; 否则跳过.
 */
public class ConsumerOffsetSerializeWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, Map<Integer, Long>> offsetTable = new HashMap<>();
    private DataVersion dataVersion = new DataVersion();

    public ConsumerOffsetSerializeWrapper() {
    }

    public Map<String, Map<Integer, Long>> getOffsetTable() {
        return offsetTable;
    }

    public void setOffsetTable(Map<String, Map<Integer, Long>> offsetTable) {
        this.offsetTable = offsetTable;
    }

    public DataVersion getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(DataVersion dataVersion) {
        this.dataVersion = dataVersion;
    }
}
