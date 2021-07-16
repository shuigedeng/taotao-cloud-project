package com.taotao.cloud.bigdata.hadoop.atguigu.mapreduce.a7_partitionerandwritableComparable;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;


public class ProvincePartitioner2 extends Partitioner<FlowBean, Text> {
    @Override
    public int getPartition(FlowBean flowBean, Text text, int numPartitions) {

        String phone = text.toString();

        String prePhone = phone.substring(0, 3);

        int partition;
        if ("136".equals(prePhone)){
            partition =  0;
        }else if ("137".equals(prePhone)){
            partition =  1;
        }else if ("138".equals(prePhone)){
            partition =  2;
        }else if ("139".equals(prePhone)){
            partition =  3;
        }else {
            partition = 4;
        }

        return partition;
    }
}
