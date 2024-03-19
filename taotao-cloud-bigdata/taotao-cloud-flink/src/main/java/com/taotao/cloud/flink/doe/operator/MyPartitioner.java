package com.taotao.cloud.flink.doe.operator;


import org.apache.flink.api.common.functions.Partitioner;

/**
 * @Date: 2023/12/31
 * @Author: Hang.Nian.YY
 * @WX: 17710299606
 * @Tips: 学大数据 ,到多易教育
 * @DOC: https://blog.csdn.net/qq_37933018?spm=1000.2115.3001.5343
 * @Description:
 */
public class MyPartitioner implements Partitioner<String> {

    /**
     * 传入一个key 根据key的值去计算  当前数据传递到下游的哪个分区
     * @param key The key.
     * @param numPartitions The number of partitions to partition into.
     * @return
     */
    @Override
    public int partition(String key, int numPartitions) {
        return (key.hashCode() & Integer.MAX_VALUE) % numPartitions ;
    }
}
