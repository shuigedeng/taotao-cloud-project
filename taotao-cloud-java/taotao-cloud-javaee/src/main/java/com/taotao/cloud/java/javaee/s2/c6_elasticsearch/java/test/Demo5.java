package com.taotao.cloud.java.javaee.s2.c6_elasticsearch.java.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qf.utils.ESClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;

public class Demo5 {

    ObjectMapper mapper = new ObjectMapper();
    RestHighLevelClient client = ESClient.getClient();
    String index = "sms-logs-index";
    String type = "sms-logs-type";

    @Test
    public void multiMatchQuery() throws IOException {
        //1. 创建Request
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        //2. 指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //-----------------------------------------------
        builder.query(QueryBuilders.multiMatchQuery("北京","province","smsContent"));
        //-----------------------------------------------
        request.source(builder);
        //3. 执行查询
        SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

        //4. 输出结果
        for (SearchHit hit : resp.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }


    @Test
    public void booleanMatchQuery() throws IOException {
        //1. 创建Request
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        //2. 指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //-----------------------------------------------                               选择AND或者OR
        builder.query(QueryBuilders.matchQuery("smsContent","中国 健康").operator(Operator.OR));
        //-----------------------------------------------
        request.source(builder);
        //3. 执行查询
        SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

        //4. 输出结果
        for (SearchHit hit : resp.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    @Test
    public void matchQuery() throws IOException {
        //1. 创建Request
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        //2. 指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //-----------------------------------------------
        builder.query(QueryBuilders.matchQuery("smsContent","收货安装"));
        //-----------------------------------------------
        request.source(builder);
        //3. 执行查询
        SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

        //4. 输出结果
        for (SearchHit hit : resp.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    @Test
    public void matchAllQuery() throws IOException {
        //1. 创建Request
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        //2. 指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        builder.size(20);           // ES默认只查询10条数据，如果想查询更多，添加size
        request.source(builder);

        //3. 执行查询
        SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

        //4. 输出结果
        for (SearchHit hit : resp.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
        System.out.println(resp.getHits().getHits().length);
    }

}
