package com.taotao.cloud.log.biz.log.configuration;

import cn.bootx.common.mybatisplus.interceptor.MpInterceptor;
import cn.bootx.common.mybatisplus.extension.DataChangeRecorderInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author xxm
 * @date 2023/1/2
 */
@Configuration
public class DataVersionConfiguration {
    /**
     * 数据变更记录
     */
    @Bean
    public MpInterceptor dataChangeRecorderInnerInterceptor(DataChangeRecorderInnerInterceptor dataChangeRecorderInnerInterceptor){
        return new MpInterceptor(dataChangeRecorderInnerInterceptor,2);
    }
}
