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

package com.taotao.cloud.auth.infrastructure.authorization.converter;


import com.taotao.cloud.auth.infrastructure.persistent.management.po.OAuth2Application;

/**
 * <p>OAuth2Application 转 RegisteredClient 转换器 </p>
 *
 *
 * @since : 2023/5/21 19:04
 */
public class OAuth2ApplicationToRegisteredClientConverter
        extends AbstractOAuth2RegisteredClientConverter<OAuth2Application> {}
