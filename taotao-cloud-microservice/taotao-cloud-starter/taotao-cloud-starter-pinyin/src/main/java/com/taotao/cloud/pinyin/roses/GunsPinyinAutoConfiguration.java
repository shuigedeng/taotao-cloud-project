package com.taotao.cloud.pinyin.roses;

import com.taotao.cloud.pinyin.roses.api.PinYinApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 拼音的自动配置
 */
@AutoConfiguration
public class GunsPinyinAutoConfiguration {

    /**
     * 拼音工具接口的封装
     *
     */
    @Bean
    @ConditionalOnMissingBean(PinYinApi.class)
    public PinYinApi pinYinApi() {
        return new PinyinServiceImpl();
    }

}
