package com.taotao.cloud.web.annotation;

import com.taotao.cloud.data.jpa.annotation.EnableTaoTaoCloudJPA;
import com.taotao.cloud.elasticsearch.annotation.EnableTaoTaoCloudElasticsearch;
import com.taotao.cloud.file.annotation.EnableTaoTaoCloudUploadFile;
import com.taotao.cloud.idempotent.annotation.EnableTaoTaoIdempotent;
import com.taotao.cloud.job.annotation.EnableTaoTaoCloudXxlJob;
import com.taotao.cloud.loadbalancer.annotation.EnableTaoTaoCloudFeign;
import com.taotao.cloud.loadbalancer.annotation.EnableTaoTaoCloudHttpClient;
import com.taotao.cloud.loadbalancer.annotation.EnableTaoTaoCloudLoadbalancer;
import com.taotao.cloud.lock.annotation.EnableRedissonLock;
import com.taotao.cloud.log.annotation.EnableTaoTaoCloudRequestLog;
import com.taotao.cloud.openapi.annotation.EnableTaoTaoCloudOpenapi;
import com.taotao.cloud.p6spy.annotation.EnableTaoTaoCloudP6spy;
import com.taotao.cloud.seata.annotation.EnableTaoTaoCloudSeata;
import com.taotao.cloud.security.taox.annotation.EnableTaoTaoCloudOauth2ResourceSecurity;
import com.taotao.cloud.sentinel.annotation.EnableTaoTaoCloudSentinel;
import com.taotao.cloud.shardingsphere.annotation.EnableTaoTaoCloudShardingsphere;
import com.taotao.cloud.sms.annotation.EnableTaoTaoCloudAliSms;
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
@EnableTaoTaoCloudElasticsearch
@EnableTaoTaoCloudUploadFile
@EnableTaoTaoIdempotent
@EnableRedissonLock
@EnableTaoTaoCloudAliSms
@EnableTaoTaoCloudShardingsphere
@EnableTaoTaoCloudRequestLog
@EnableTaoTaoCloudXxlJob
@EnableTaoTaoCloudP6spy
@EnableTaoTaoCloudFeign
@EnableTaoTaoCloudOpenapi
@EnableTaoTaoCloudSeata
@EnableTaoTaoCloudJPA
@EnableTaoTaoCloudLoadbalancer
@EnableTaoTaoCloudHttpClient
@EnableTaoTaoCloudSentinel
@EnableTaoTaoCloudOauth2ResourceSecurity
@EnableEncryptableProperties
@EnableDiscoveryClient
@SpringBootApplication
public @interface TaoTaoCloudApplication {

}
