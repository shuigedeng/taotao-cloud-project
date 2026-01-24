package com.taotao.cloud.gateway.predicates;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.server.mvc.predicate.PredicateSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Mr han
 * @date 2025/12/17
 * @description 注册自定义的predicate，用在application.yml中
 *
 * 配置文件方式：格式"静态方法名（首字母大写小写都可）=参数值"
*/
@Configuration
@Profile("fileconfig")
public class PredicateConfiguration {

    private static final Logger log = LoggerFactory.getLogger(PredicateConfiguration.class);

    @PostConstruct
    public void init(){
        log.error("========开始加载PredicateConfiguration配置============");
    }
    @Bean
    public CustomPredicateSupplier customPredicateSupplier() {
        return new CustomPredicateSupplier();
    }
}

