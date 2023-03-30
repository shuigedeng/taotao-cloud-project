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

package com.taotao.cloud.auth.biz.demo.core.definition;

import cn.herodotus.engine.assistant.core.definition.constants.BaseConstants;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

/**
 * Description: 自定义 Grant Type 类型
 *
 * @author : gengwei.zheng
 * @date : 2022/2/25 9:53
 */
public interface HerodotusGrantType {

    AuthorizationGrantType SOCIAL = new AuthorizationGrantType(BaseConstants.SOCIAL_CREDENTIALS);

    AuthorizationGrantType PASSWORD = new AuthorizationGrantType(BaseConstants.PASSWORD);
}
