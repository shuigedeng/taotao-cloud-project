package com.taotao.cloud.java.javaee.s2.c7_springboot.search.java.service.impl;

import com.qf.openapi.search.entity.Customer;
import com.qf.openapi.search.service.CustomerService;
import com.qf.openapi.search.utils.JSON;
import com.qf.openapi.search.vo.LayUITableVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private String index = "openapi_customer";
    private String type = "customer";
    @Autowired
    private RestHighLevelClient client;


    @Override
    public String searchCustomerByQuery(Map<String, Object> param) throws IOException {
        //1. SearchRequest
        SearchRequest request = new SearchRequest(index);
        request.types(type);

        //2. 封装查询条件
        SearchSourceBuilder source = new SearchSourceBuilder();
        Object name = param.get("name");
        if(!StringUtils.isEmpty(name)){
            source.query(QueryBuilders.termQuery("username",name));
        }
        Object state = param.get("state");
        if(state != null){
            source.query(QueryBuilders.termsQuery("state",state));
        }
        Integer page = (Integer) param.get("page");
        Integer limit = (Integer) param.get("limit");
        source.from((page - 1) * limit);
        source.size(limit);

        request.source(source);

        //3. 执行查询
        SearchResponse resp = client.search(request, RequestOptions.DEFAULT);

        //4. 封装数据
        LayUITableVO<Customer> vo = new LayUITableVO<>();
        vo.setCount(resp.getHits().getTotalHits());
        List<Customer> data = new ArrayList<>();
        for (SearchHit hit : resp.getHits().getHits()) {
            Customer customer = new Customer();
            try {
                BeanUtils.populate(customer,hit.getSourceAsMap());
            } catch (Exception e) {
                e.printStackTrace();
            }
            data.add(customer);
        }
        vo.setData(data);

        //5. 返回数据
        return JSON.toJSON(vo);
    }



    @Override
    public void saveCustomer(Customer customer) throws IOException {
        //1. 创建IndexRequest
        IndexRequest request = new IndexRequest(index,type,customer.getId() + "");

        //2. 封装数据
        request.source(JSON.toJSON(customer), XContentType.JSON);

        //3. 执行添加
        IndexResponse resp = client.index(request, RequestOptions.DEFAULT);

        //4. 判断添加是否成功（失败，抛出异常）
        if(!"CREATED".equalsIgnoreCase(resp.getResult().toString())){
            log.error("【向ES添加客户信息失败】 index = {},type = {},customer = {}",index,type,customer);
            throw new RuntimeException("【向ES添加客户信息失败】");
        }

    }




}
