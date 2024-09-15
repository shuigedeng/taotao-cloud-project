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

package com.taotao.cloud.auth.infrastructure.authentication.extension.social.core.constants;


import com.taotao.boot.security.spring.constants.ErrorCodes;

/**
 * <p>Access 模块错误代码 </p>
 *
 *
 * @since : 2022/9/2 17:50
 */
public interface AccessErrorCodes extends ErrorCodes {

    int ILLEGAL_ACCESS_SOURCE = ACCESS_MODULE_406_BEGIN + 1;
    int ACCESS_CONFIG_ERROR = ILLEGAL_ACCESS_SOURCE + 1;
}
