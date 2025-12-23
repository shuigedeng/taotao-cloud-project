/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.shell;

import com.taotao.boot.core.startup.StartupSpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * pc端商城
 *
 * java -jar .\x2_sap_collect_shell-1.0-SNAPSHOT.jar
 * shell:>add --a 1 --b 2
 * 3
 *
 * @author shuigedeng
 * @since 2021/1/18 下午4:54
 * @version 2022.03
 */
@SpringBootApplication
public class TaoTaoCloudShellApplication {

    public static void main(String[] args) {
        new StartupSpringApplication(TaoTaoCloudShellApplication.class)
                .setTtcBanner()
                .setTtcProfileIfNotExists("dev")
                .setTtcApplicationProperty("taotao-cloud-shell")
                //.setTtcAllowBeanDefinitionOverriding(true)
                .run(args);
    }
    // @Bean
    // public PromptProvider shellPromptProvider() {
    //	return () -> new AttributedString("boot-shell:>",
    // AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
    // }
    // @Bean
    // public CorsFilter corsFilter() {
    //    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //    final CorsConfiguration config = new CorsConfiguration();
    //	// 允许cookies跨域
    //    config.setAllowCredentials(true);
    //	// #允许向该服务器提交请求的URI，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin
    //    config.addAllowedOriginPattern("*");
    //	// #允许访问的头信息,*表示全部
    //    config.addAllowedHeader("*");
    //	// 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
    //    config.setMaxAge(18000L);
    //	// 允许提交请求的方法，*表示全部允许
    //    config.addAllowedMethod("OPTIONS");
    //    config.addAllowedMethod("HEAD");
    //	// 允许Get的请求方法
    //    config.addAllowedMethod("GET");
    //    config.addAllowedMethod("PUT");
    //    config.addAllowedMethod("POST");
    //    config.addAllowedMethod("DELETE");
    //    config.addAllowedMethod("PATCH");
    //    source.registerCorsConfiguration("/**", config);
    //    return new CorsFilter(source);
    // }
}
