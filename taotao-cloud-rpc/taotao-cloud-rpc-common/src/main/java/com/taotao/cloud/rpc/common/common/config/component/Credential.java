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

package com.taotao.cloud.rpc.common.common.config.component;

/**
 * 凭证信息
 * 针对安全的处理，最后关心。
 * @author shuigedeng
 * @since 2024.06
 */
public class Credential {

    /**
     * 用户名
     * @since 2024.06
     */
    private String username;

    /**
     * 密码
     * @since 2024.06
     */
    private String password;

    public String username() {
        return username;
    }

    public Credential username(String username) {
        this.username = username;
        return this;
    }

    public String password() {
        return password;
    }

    public Credential password(String password) {
        this.password = password;
        return this;
    }
}
