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

import org.springframework.security.authentication.AccountStatusException;

/**
 * Description: 自定义 Session 已过期
 *
 * @author : gengwei.zheng
 * @date : 2022/7/28 13:37
 */
public class SessionExpiredException extends AccountStatusException {

    public SessionExpiredException(String msg) {
        super(msg);
    }

    public SessionExpiredException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
