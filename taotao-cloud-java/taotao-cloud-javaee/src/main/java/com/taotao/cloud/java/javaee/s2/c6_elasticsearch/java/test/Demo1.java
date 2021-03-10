package com.taotao.cloud.java.javaee.s2.c6_elasticsearch.java.test;

import com.taotao.cloud.java.javaee.s2.c6_elasticsearch.java.utils.ESClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;

public class Demo1 {

    @Test
    public void testConnect(){
        RestHighLevelClient client = ESClient.getClient();
        System.out.println("OK!!");
    }

}
