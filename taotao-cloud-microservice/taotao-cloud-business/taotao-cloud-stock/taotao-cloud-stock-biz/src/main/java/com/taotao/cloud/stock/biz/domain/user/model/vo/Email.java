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

import java.util.regex.Pattern;
import org.apache.commons.lang.Validate;

/**
 * 邮箱
 *
 * @author shuigedeng
 * @since 2021-02-08
 */
public final class Email implements ValueObject<Email> {

    private String email;

    /** 有效性正则 */
    private static final Pattern VALID_PATTERN =
            Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");

    /**
     * Constructor.
     *
     * @param email
     */
    public Email(final String email) {
        if (email != null) {
            Validate.isTrue(VALID_PATTERN.matcher(email).matches(), "邮箱格式不正确");
        }
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean sameValueAs(Email other) {
        return other != null && this.email.equals(other.email);
    }

    @Override
    public String toString() {
        return email;
    }
}
