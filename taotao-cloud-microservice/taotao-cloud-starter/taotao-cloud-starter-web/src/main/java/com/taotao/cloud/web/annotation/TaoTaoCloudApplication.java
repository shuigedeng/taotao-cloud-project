/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.web.annotation;

import com.taotao.cloud.canal.annotation.EnableTaoTaoCloudCanalClient;
import com.taotao.cloud.captcha.annotation.EnableTaoTaoCloudCaptcha;
import com.taotao.cloud.core.annotation.EnableTaoTaoCloudCloud;
import com.taotao.cloud.data.jpa.annotation.EnableTaoTaoCloudJPA;
import com.taotao.cloud.dingtalk.annatations.EnableTaoTaoCloudDingtalk;
import com.taotao.cloud.disruptor.annotation.EnableTaoTaoCloudDisruptor;
import com.taotao.cloud.elasticsearch.annotation.EnableTaoTaoCloudElasticsearch;
import com.taotao.cloud.feign.annotation.EnableTaoTaoCloudFeign;
import com.taotao.cloud.feign.annotation.EnableTaoTaoCloudLoadbalancer;
import com.taotao.cloud.file.annotation.EnableTaoTaoCloudUploadFile;
import com.taotao.cloud.health.annotation.EnableTaoTaoCloudHealth;
import com.taotao.cloud.job.xxl.annotation.EnableTaoTaoCloudJobXxl;
import com.taotao.cloud.log.annotation.EnableTaoTaoCloudRequestLog;
import com.taotao.cloud.mail.annotation.EnableTaoTaoCloudMail;
import com.taotao.cloud.netty.annotation.EnableTaoTaoCloudWebSocket;
import com.taotao.cloud.openapi.annotation.EnableTaoTaoCloudOpenapi;
import com.taotao.cloud.p6spy.annotation.EnableTaoTaoCloudP6spy;
import com.taotao.cloud.rxjava.annotation.EnableTaoTaoCloudRxjava;
import com.taotao.cloud.seata.annotation.EnableTaoTaoCloudSeata;
import com.taotao.cloud.security.annotation.EnableTaoTaoCloudOauth2ResourceSecurity;
import com.taotao.cloud.sentinel.annotation.EnableTaoTaoCloudSentinel;
import com.taotao.cloud.shardingsphere.annotation.EnableTaoTaoCloudShardingsphere;
import com.taotao.cloud.sms.annotation.EnableTaoTaoCloudSms;
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
 * @version 2021.9
 * @since 2021-09-02 21:02:52
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableTaoTaoCloudCloud
@EnableTaoTaoCloudWebSocket
@EnableTaoTaoCloudMail
@EnableTaoTaoCloudRxjava
@EnableTaoTaoCloudDisruptor
@EnableTaoTaoCloudDingtalk
@EnableTaoTaoCloudCanalClient
@EnableTaoTaoCloudHealth
@EnableTaoTaoCloudSms
@EnableTaoTaoCloudCaptcha
@EnableTaoTaoCloudElasticsearch
@EnableTaoTaoCloudUploadFile
@EnableTaoTaoCloudZookeeper
@EnableTaoTaoCloudShardingsphere
@EnableTaoTaoCloudRequestLog
@EnableTaoTaoCloudJobXxl
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
