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
package com.taotao.cloud.security.annotation;

import com.taotao.cloud.security.configuration.MethodSecurityAutoConfiguration;
import com.taotao.cloud.security.configuration.Oauth2ResourceAutoConfiguration;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * 使道道云oauth2资源
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-22 14:24:32
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Documented
@EnableWebSecurity
@Import({Oauth2ResourceAutoConfiguration.class, MethodSecurityAutoConfiguration.class})
public @interface EnableTaoTaoCloudOauth2Resource {


}
