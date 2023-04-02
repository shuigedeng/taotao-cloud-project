package com.taotao.cloud.sys.biz.sensitive.filter.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * -不文明词语或敏感词词语配置<br>
 * -JVM参数内存最小3G 推荐设置-Xms4096M -Xmx4096M
 * @author yweijian
 * @date 2022年9月15日
 * @version 2.0.0
 * @description
 */
@Setter
@Getter
@Component
@RefreshScope
@ConfigurationProperties(prefix = "daffodil.security.sensitive-word")
public class SensitiveWordProperties {

    /**
     * 不文明词语拦截是否开启，默认不开启
     */
    private Boolean enable = false;
    
    /**
     * 不文明词语拦截 配置需要进行检验的url
     */
    private List<String> urls = new ArrayList<String>();
}
