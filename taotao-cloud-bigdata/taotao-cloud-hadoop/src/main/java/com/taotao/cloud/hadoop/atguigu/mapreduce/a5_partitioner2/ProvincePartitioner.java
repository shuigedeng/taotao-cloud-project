package com.taotao.cloud.bigdata.hadoop.atguigu.mapreduce.a5_partitioner2;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class ProvincePartitioner extends Partitioner<Text, FlowBean> {
    @Override
    public int getPartition(Text text, FlowBean flowBean, int numPartitions) {
        // text 是手机号

        String phone = text.toString();

        String prePhone = phone.substring(0, 3);

        int partition ;

        if ("136".equals(prePhone)){
            partition = 0;
        }else if ("137".equals(prePhone)){
            partition = 1;
        }else if ("138".equals(prePhone)){
            partition = 2;
        }else if ("139".equals(prePhone)){
            partition = 3;
        }else {
            partition = 4;
        }

        return partition;
    }
}
