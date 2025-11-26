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

package com.taotao.cloud.stock.biz.domain.user.model.vo;

import com.taotao.boot.common.utils.lang.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码
 *
 * @author shuigedeng
 * @since 2021-02-08
 */
public class Password implements ValueObject<Password> {

    public static final String DEFAULT = "123456";

    /** 密码 */
    private String password;

    public Password(String password) {
        if (StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("密码不能为空");
        }
        this.password = password;
    }

    public static Password create(String passwordStr) {
        String password = new BCryptPasswordEncoder().encode(passwordStr);
        return new Password(password);
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean sameValueAs(Password other) {
        return other != null && this.password.equals(other.password);
    }

    @Override
    public String toString() {
        return "Password{" + "password='" + password + '\'' + '}';
    }
}
