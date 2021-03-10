package com.taotao.cloud.java.javaee.s2.c6_elasticsearch.java.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.java.javaee.s2.c6_elasticsearch.java.utils.ESClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class Demo4 {

    ObjectMapper mapper = new ObjectMapper();
    RestHighLevelClient client = ESClient.getClient();
    String index = "sms-logs-index";
    String type = "sms-logs-type";

    @Test
    public void termsQuery() throws IOException {
        //1. 创建request
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        //2. 封装查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.termsQuery("province","北京","山西"));

        request.source(builder);

        //3. 执行查询
        SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

        //4. 输出_source
        for (SearchHit hit : resp.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }


    @Test
    public void termQuery() throws IOException {
        //1. 创建Request对象
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        //2. 指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.from(0);
        builder.size(5);
        builder.query(QueryBuilders.termQuery("province","北京"));

        request.source(builder);

        //3. 执行查询
        SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

        //4. 获取到_source中的数据，并展示
        for (SearchHit hit : resp.getHits().getHits()) {
            Map<String, Object> result = hit.getSourceAsMap();
            System.out.println(result);
        }
    }
}
