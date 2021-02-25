/**
 * Project Name: super-member-system
 * Package Name: com.grcloudcrm.youzan.config
 * Date: 2020/4/16 11:25
 * Author: dengtao
 */
package com.taotao.cloud.demo.youzan.config;

import com.youzan.cloud.open.sdk.core.HttpConfig;
import com.youzan.cloud.open.sdk.core.client.core.DefaultYZClient;
import com.youzan.cloud.open.sdk.core.client.core.YouZanClient;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 有赞配置类<br>
 *
 * @author dengtao
 * @version v1.0
 * @date 2020/4/16 11:25
 */
@Component
public class YouZanConfig {

    @Bean
    @ConditionalOnClass(DefaultYZClient.class)
    public YouZanClient youZanClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS);
        HttpConfig httpConfig = HttpConfig.builder().build();
        return new DefaultYZClient(httpConfig);
    }

}
