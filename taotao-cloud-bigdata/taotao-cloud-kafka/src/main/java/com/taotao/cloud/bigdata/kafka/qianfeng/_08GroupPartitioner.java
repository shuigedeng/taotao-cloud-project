package com.taotao.cloud.bigdata.kafka.qianfeng;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义分区之分组分区
 */
public class _08GroupPartitioner implements Partitioner {
    /*
     将要分区的数据划分好
     */
    private Map<String,Integer> map = new HashMap<String,Integer>();
    {
        map.put("java.learn.com",0);
        map.put("ui.learn.com",1);
        map.put("bigdata.learn.com",2);
        map.put("android.learn.com",3);
        map.put("h5.learn.com",4);
    }
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        String line = value.toString();
        String[] str = line.split("\\s+");
        try {
            if(str == null || str.length != 2){
                return 0;
            }else{
                URL url = new URL(str[1]);
                String host = url.getHost();
                return map.getOrDefault(host,0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0; // key不存在，直接返回0
    }

    public void close() {

    }

    public void configure(Map<String, ?> configs) {

    }
}
