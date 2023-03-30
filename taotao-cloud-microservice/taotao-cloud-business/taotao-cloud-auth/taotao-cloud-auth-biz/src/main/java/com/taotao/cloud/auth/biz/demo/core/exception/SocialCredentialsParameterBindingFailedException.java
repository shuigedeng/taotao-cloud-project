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

package com.taotao.cloud.auth.biz.demo.core.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Description: 无法解析SocialType错误
 *
 * @author : gengwei.zheng
 * @date : 2021/5/16 9:37
 */
public class SocialCredentialsParameterBindingFailedException extends AuthenticationException {

    public SocialCredentialsParameterBindingFailedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SocialCredentialsParameterBindingFailedException(String msg) {
        super(msg);
    }
}
