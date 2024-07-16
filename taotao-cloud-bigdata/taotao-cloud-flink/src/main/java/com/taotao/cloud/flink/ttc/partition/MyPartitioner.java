package com.taotao.cloud.flink.ttc.partition;

import org.apache.flink.api.common.functions.Partitioner;

/**
 * TODO
 *
 * @author cjp
 * @version 1.0
 */
public class MyPartitioner implements Partitioner<String> {
    @Override
    public int partition(String key, int numPartitions) {
        return Integer.parseInt(key) % numPartitions;
    }
}
