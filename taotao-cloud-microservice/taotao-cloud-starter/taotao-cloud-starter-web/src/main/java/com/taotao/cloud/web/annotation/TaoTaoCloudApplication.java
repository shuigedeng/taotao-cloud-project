package com.taotao.cloud.web.annotation;

import com.taotao.cloud.data.jpa.annotation.EnableTaoTaoCloudJPA;
import com.taotao.cloud.feign.annotation.EnableTaoTaoCloudFeign;
import com.taotao.cloud.feign.annotation.EnableTaoTaoCloudLoadbalancer;
import com.taotao.cloud.job.annotation.EnableTaoTaoCloudXxlJob;
import com.taotao.cloud.log.annotation.EnableTaoTaoCloudRequestLog;
import com.taotao.cloud.openapi.annotation.EnableTaoTaoCloudOpenapi;
import com.taotao.cloud.p6spy.annotation.EnableTaoTaoCloudP6spy;
import com.taotao.cloud.seata.annotation.EnableTaoTaoCloudSeata;
import com.taotao.cloud.security.annotation.EnableTaoTaoCloudOauth2ResourceSecurity;
import com.taotao.cloud.sentinel.annotation.EnableTaoTaoCloudSentinel;
import com.taotao.cloud.shardingsphere.annotation.EnableTaoTaoCloudShardingsphere;
import com.taotao.cloud.zookeeper.annotation.EnableTaoTaoCloudZookeeper;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * TaoTaoCloudApplication
 *
 * @author shuigedeng
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@EnableTaoTaoCloudElasticsearch
//@EnableTaoTaoCloudUploadFile
//@EnableTaoTaoIdempotent
//@EnableRedissonLock
@EnableTaoTaoCloudZookeeper
@EnableTaoTaoCloudShardingsphere
@EnableTaoTaoCloudRequestLog
@EnableTaoTaoCloudXxlJob
@EnableTaoTaoCloudP6spy
@EnableTaoTaoCloudFeign
@EnableTaoTaoCloudLoadbalancer
@EnableTaoTaoCloudSentinel
@EnableTaoTaoCloudOpenapi
@EnableTaoTaoCloudSeata
@EnableTaoTaoCloudJPA
@EnableTaoTaoCloudOauth2ResourceSecurity
@EnableEncryptableProperties
@EnableDiscoveryClient
@SpringBootApplication
public @interface TaoTaoCloudApplication {

}
