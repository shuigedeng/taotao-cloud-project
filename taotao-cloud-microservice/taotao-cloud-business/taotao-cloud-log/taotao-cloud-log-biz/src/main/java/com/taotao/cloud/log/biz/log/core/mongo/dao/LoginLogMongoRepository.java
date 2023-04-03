package com.taotao.cloud.log.biz.log.core.mongo.dao;

import cn.bootx.starter.audit.log.core.mongo.entity.LoginLogMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**   
* mongo持久化方式
* @author xxm  
* @date 2021/12/2 
*/
public interface LoginLogMongoRepository extends MongoRepository<LoginLogMongo,Long> {
}
