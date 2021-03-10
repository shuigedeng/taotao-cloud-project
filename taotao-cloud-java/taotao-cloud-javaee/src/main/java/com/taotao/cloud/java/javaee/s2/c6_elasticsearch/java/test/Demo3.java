package com.taotao.cloud.java.javaee.s2.c6_elasticsearch.java.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.java.javaee.s2.c6_elasticsearch.java.entity.Person;
import com.taotao.cloud.java.javaee.s2.c6_elasticsearch.java.utils.ESClient;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Demo3 {

    ObjectMapper mapper = new ObjectMapper();
    RestHighLevelClient client = ESClient.getClient();
    String index = "person";
    String type = "man";

    @Test
    public void bulkDeleteDoc() throws IOException {
        //1. 封装Request对象
        BulkRequest request = new BulkRequest();
        request.add(new DeleteRequest(index,type,"1"));
        request.add(new DeleteRequest(index,type,"2"));
        request.add(new DeleteRequest(index,type,"3"));

        //2. client执行
        BulkResponse resp = client.bulk(request, RequestOptions.DEFAULT);

        //3. 输出
        System.out.println(resp);
    }

    @Test
    public void bulkCreateDoc() throws IOException {
        //1. 准备多个json数据
        Person p1 = new Person(1,"张三",23,new Date());
        Person p2 = new Person(2,"李四",24,new Date());
        Person p3 = new Person(3,"王五",25,new Date());

        String json1 = mapper.writeValueAsString(p1);
        String json2 = mapper.writeValueAsString(p2);
        String json3 = mapper.writeValueAsString(p3);

        //2. 创建Request，将准备好的数据封装进去
        BulkRequest request = new BulkRequest();
        request.add(new IndexRequest(index,type,p1.getId().toString()).source(json1,XContentType.JSON));
        request.add(new IndexRequest(index,type,p2.getId().toString()).source(json2,XContentType.JSON));
        request.add(new IndexRequest(index,type,p3.getId().toString()).source(json3,XContentType.JSON));

        //3. 用client执行
        BulkResponse resp = client.bulk(request, RequestOptions.DEFAULT);

        //4. 输出结果
        System.out.println(resp.toString());
    }


    @Test
    public void deleteDoc() throws IOException {
        //1. 封装Request对象
        DeleteRequest request = new DeleteRequest(index,type,"1");

        //2. client执行
        DeleteResponse resp = client.delete(request, RequestOptions.DEFAULT);

        //3. 输出结果
        System.out.println(resp.getResult().toString());
    }

    @Test
    public void updateDoc() throws IOException {
        //1. 创建一个Map，指定需要修改的内容
        Map<String,Object> doc = new HashMap<>();
        doc.put("name","张大三");
        String docId = "1";

        //2. 创建request对象，封装数据
        UpdateRequest request = new UpdateRequest(index,type,docId);
        request.doc(doc);

        //3. 通过client对象执行
        UpdateResponse update = client.update(request, RequestOptions.DEFAULT);

        //4. 输出返回结果
        System.out.println(update.getResult().toString());
    }


    @Test
    public void createDoc() throws IOException {
        //1. 准备一个json数据
        Person person = new Person(1,"张三",23,new Date());
        String json = mapper.writeValueAsString(person);

        //2. 准备一个request对象（手动指定id）
        IndexRequest request = new IndexRequest(index,type,person.getId().toString());
        request.source(json, XContentType.JSON);

        //3. 通过client对象执行添加
        IndexResponse resp = client.index(request, RequestOptions.DEFAULT);

        //4. 输出返回结果
        System.out.println(resp.getResult().toString());
    }

}
