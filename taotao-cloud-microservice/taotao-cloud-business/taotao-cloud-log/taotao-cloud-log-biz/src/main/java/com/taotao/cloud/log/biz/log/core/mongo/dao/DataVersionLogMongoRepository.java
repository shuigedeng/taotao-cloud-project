package com.taotao.cloud.log.biz.log.core.mongo.dao;

import com.taotao.cloud.log.biz.log.core.mongo.entity.DataVersionLogMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
* mongo持久化方式
* @author shuigedeng
* @date 2022/1/10
*/
public interface DataVersionLogMongoRepository extends MongoRepository<DataVersionLogMongo,Long>{
}
