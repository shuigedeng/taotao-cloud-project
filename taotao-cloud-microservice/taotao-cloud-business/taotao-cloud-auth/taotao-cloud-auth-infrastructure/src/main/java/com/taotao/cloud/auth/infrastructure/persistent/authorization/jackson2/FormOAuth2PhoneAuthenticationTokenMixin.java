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

package com.taotao.cloud.auth.infrastructure.persistent.authorization.jackson2;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.jackson2.SimpleGrantedAuthorityMixin;
import org.springframework.security.jackson2.UnmodifiableSetMixin;

/**
 * This mixin class is used to serialize / deserialize
 * {@link org.springframework.security.authentication.UsernamePasswordAuthenticationToken}. This
 * class register a custom deserializer {@link FormOAuth2PhoneAuthenticationTokenDeserializer}.
 * <p>
 * In order to use this mixin you'll need to add 3 more mixin classes.
 * <ol>
 * <li>{@link UnmodifiableSetMixin}</li>
 * <li>{@link SimpleGrantedAuthorityMixin}</li>
 * <li>{@link UserMixin}</li>
 * </ol>
 *
 * <pre>
 *     ObjectMapper mapper = new ObjectMapper();
 *     mapper.registerModule(new CoreJackson2Module());
 * </pre>
 *
 * @author Jitendra Singh
 * @see CoreJackson2Module
 * @see SecurityJackson2Modules
 * @since 4.2
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonAutoDetect(
	fieldVisibility = JsonAutoDetect.Visibility.ANY,
	getterVisibility = JsonAutoDetect.Visibility.NONE,
	isGetterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonDeserialize(using = FormOAuth2PhoneAuthenticationTokenDeserializer.class)
public abstract class FormOAuth2PhoneAuthenticationTokenMixin {

}
