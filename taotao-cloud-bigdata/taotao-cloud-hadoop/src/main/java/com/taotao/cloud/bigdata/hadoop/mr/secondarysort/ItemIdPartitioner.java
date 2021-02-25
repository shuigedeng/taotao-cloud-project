package com.taotao.cloud.bigdata.hadoop.mr.secondarysort;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;


public class ItemIdPartitioner extends Partitioner<OrderBean, NullWritable> {

    @Override
    public int getPartition(OrderBean bean, NullWritable value, int numReduceTasks) {
        //相同id的订单bean，会发往相同的partition
        //而且，产生的分区数，是会跟用户设置的reduce task数保持一致
        return (bean.getItemid().hashCode() & Integer.MAX_VALUE) % numReduceTasks;

    }

}
