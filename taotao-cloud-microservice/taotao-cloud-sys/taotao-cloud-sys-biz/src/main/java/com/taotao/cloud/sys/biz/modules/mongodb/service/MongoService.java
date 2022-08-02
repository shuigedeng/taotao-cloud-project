package com.taotao.cloud.sys.biz.modules.mongodb.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import com.taotao.cloud.sys.biz.modules.classloader.ClassloaderService;
import com.taotao.cloud.sys.biz.modules.core.dtos.PageResponseDto;
import com.taotao.cloud.sys.biz.modules.core.dtos.param.ConnectParam;
import com.taotao.cloud.sys.biz.modules.core.dtos.param.MongoAuthParam;
import com.taotao.cloud.sys.biz.modules.core.dtos.param.MongoConnectParam;
import com.taotao.cloud.sys.biz.modules.core.service.connect.ConnectService;
import org.apache.commons.collections.IteratorUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.client.*;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MongoService {
    private Map<String, MongoClient> mongoClientMap = new ConcurrentHashMap<>();

    private static final String MODULE = "mongo";

    @Autowired
    private ConnectService connectService;

    @Autowired
    private ClassloaderService classloaderService;

    /**
     * 查询当前连接所有数据库
     * @param connName
     * @return
     * @throws IOException
     */
    public List<String> databaseNames(String connName) throws IOException {
        MongoClient mongoClient = mongoClient(connName);
        MongoIterable<String> strings = mongoClient.listDatabaseNames();
        MongoCursor<String> iterator = strings.iterator();
        List<String> list = IteratorUtils.toList(iterator);
        list.remove("admin");
        list.remove("local");
        return list;
    }

    /**
     * 列出某个库的所有集合
     * @param connName
     * @param databaseName
     * @return
     * @throws IOException
     */
    public List<CollectionDto> collectionNames(String connName, String databaseName) throws IOException {
        MongoClient mongoClient = mongoClient(connName);
        MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName);
        MongoCursor<String> iterator = mongoDatabase.listCollectionNames().iterator();
        List<CollectionDto> collectionDtos = new ArrayList<>();
        DB db = new DB(mongoClient,databaseName);
        while (iterator.hasNext()){
            String collectionName = iterator.next();
            MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
            CommandResult stats = db.getStats();
            CollectionDto collectionDto = new CollectionDto(collectionName,stats);
            collectionDtos.add(collectionDto);
        }
        return collectionDtos;
    }

    /**
     * 分页 mongo 数据查询
     * @param mongoQueryParam
     * @param pageParam
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public PageResponseDto<List<String>> queryDataPage(MongoQueryParam mongoQueryParam, PageParam pageParam) throws IOException, ClassNotFoundException {
        List<String> objects = new ArrayList<>();

        String filterJson = mongoQueryParam.getFilter();
        String sortJson = mongoQueryParam.getSort();

        BasicDBObject filterBson = filterJson == null ? new BasicDBObject() :  BasicDBObject.parse(filterJson);
        BasicDBObject sortBson = sortJson == null ? new BasicDBObject() :  BasicDBObject.parse(sortJson);
        sortBson.append("_id",-1);

        MongoClient mongoClient = mongoClient(mongoQueryParam.getConnName());
        MongoDatabase database = mongoClient.getDatabase(mongoQueryParam.getDatabaseName());
        MongoCollection<Document> collection = database.getCollection(mongoQueryParam.getCollectionName());
        FindIterable<Document> limit = collection.find(filterBson).sort(sortBson)
                .skip(pageParam.getPageNo()).limit(pageParam.getPageSize());
        MongoCursor<Document> iterator = limit.iterator();
        while (iterator.hasNext()){
            Document document = iterator.next();
            String json = document.toJson();
            objects.add(json);
        }

        // 查询数据总数
        long countDocuments = collection.countDocuments(filterBson);
        return new PageResponseDto<>(objects,countDocuments);
    }


//    @PostConstruct
//    public void register(){
//        pluginManager.register(PluginDto.builder().module("monitor").author("9420").logo("mongo.jpg").desc("mongodb 监控管理").name(MODULE).build());
//    }

    /**
     * 获取一个 mongo 客户端
     * @param connName
     * @return
     */
    MongoClient mongoClient(String connName) throws IOException {
        MongoClient mongoClient = mongoClientMap.get(connName);
        if (mongoClient == null){
            MongoConnectParam mongoConnectParam = (MongoConnectParam) connectService.readConnParams(MODULE,connName);
            ConnectParam connectParam = mongoConnectParam.getConnectParam();
            ServerAddress serverAddress = new ServerAddress(connectParam.getHost(), connectParam.getPort());
            MongoAuthParam mongoAuthParam = mongoConnectParam.getAuthParam();
            MongoCredential credential = MongoCredential.createCredential(mongoAuthParam.getUsername(), mongoAuthParam.getDatabase(), mongoAuthParam.getPassword().toCharArray());
            mongoClient = new MongoClient(serverAddress,credential,MongoClientOptions.builder().build());
            mongoClientMap.put(connName,mongoClient);
        }
        return mongoClient;
    }

    @PreDestroy
    public void destory(){
        log.info("清除 {} 客户端列表:{}", MODULE,mongoClientMap.keySet());
        Iterator<MongoClient> iterator = mongoClientMap.values().iterator();
        while (iterator.hasNext()){
            MongoClient next = iterator.next();
            try{
                next.close();
            }catch (Exception e){}
        }
    }
}
