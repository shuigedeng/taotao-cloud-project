package com.taotao.cloud.java.javaee.s2.c6_elasticsearch.java.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qf.utils.ESClient;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;

public class Demo6 {


    ObjectMapper mapper = new ObjectMapper();
    RestHighLevelClient client = ESClient.getClient();
    String index = "sms-logs-index";
    String type = "sms-logs-type";


    @Test
    public void findByRegexp() throws IOException {
            //1. 创建SearchRequest
            SearchRequest request = new SearchRequest(index);
            request.types(type);

            //2. 指定查询条件
            SearchSourceBuilder builder = new SearchSourceBuilder();
            //----------------------------------------------------------
            builder.query(QueryBuilders.regexpQuery("mobile","139[0-9]{8}"));
            //----------------------------------------------------------
            request.source(builder);

            //3. 执行
            SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

            //4. 输出结果
            for (SearchHit hit : resp.getHits().getHits()) {
                System.out.println(hit.getSourceAsMap());
            }
        }


    @Test
    public void findByRange() throws IOException {
        //1. 创建SearchRequest
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        //2. 指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //----------------------------------------------------------
        builder.query(QueryBuilders.rangeQuery("fee").lte(10).gte(5));
        //----------------------------------------------------------
        request.source(builder);

        //3. 执行
        SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

        //4. 输出结果
        for (SearchHit hit : resp.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    @Test
    public void findByWildCard() throws IOException {
        //1. 创建SearchRequest
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        //2. 指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //----------------------------------------------------------
        builder.query(QueryBuilders.wildcardQuery("corpName","中国*"));
        //----------------------------------------------------------
        request.source(builder);

        //3. 执行
        SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

        //4. 输出结果
        for (SearchHit hit : resp.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    @Test
    public void findByFuzzy() throws IOException {
        //1. 创建SearchRequest
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        //2. 指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //----------------------------------------------------------
        builder.query(QueryBuilders.fuzzyQuery("corpName","盒马先生").prefixLength(2));
        //----------------------------------------------------------
        request.source(builder);

        //3. 执行
        SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

        //4. 输出结果
        for (SearchHit hit : resp.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    @Test
    public void findByPrefix() throws IOException {
        //1. 创建SearchRequest
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        //2. 指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //----------------------------------------------------------
        builder.query(QueryBuilders.prefixQuery("corpName","盒马"));
        //----------------------------------------------------------
        request.source(builder);

        //3. 执行
        SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

        //4. 输出结果
        for (SearchHit hit : resp.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }

    @Test
    public void findByIds() throws IOException {
        //1. 创建SearchRequest
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        //2. 指定查询条件
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //----------------------------------------------------------
        builder.query(QueryBuilders.idsQuery().addIds("1","2","3"));
        //----------------------------------------------------------
        request.source(builder);

        //3. 执行
        SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

        //4. 输出结果
        for (SearchHit hit : resp.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }


    @Test
    public void findById() throws IOException {
        //1. 创建GetRequest
        GetRequest request = new GetRequest(index,type,"1");

        //2. 执行查询
        GetResponse resp = client.get(request, RequestOptions.DEFAULT);

        //3. 输出结果
        System.out.println(resp.getSourceAsMap());
    }
















}
