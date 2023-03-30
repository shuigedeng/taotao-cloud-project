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

package com.taotao.cloud.im.biz.platform.common.shiro;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/** token */
@Data
public class ShiroLoginAuth implements AuthenticationToken {

    private String phone;
    private char[] password;

    public ShiroLoginAuth(String phone, String password) {
        this.phone = phone;
        this.password = password.toCharArray();
    }

    @Override
    public Object getPrincipal() {
        return phone;
    }

    @Override
    public Object getCredentials() {
        return password;
    }
}
