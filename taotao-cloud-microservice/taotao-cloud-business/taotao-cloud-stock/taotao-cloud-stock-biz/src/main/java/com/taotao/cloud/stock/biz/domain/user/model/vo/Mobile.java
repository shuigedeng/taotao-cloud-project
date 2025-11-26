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
import com.taotao.boot.common.utils.lang.StringUtils;

/**
 * 手机
 *
 * @author shuigedeng
 * @since 2021-02-08
 */
public final class Mobile implements ValueObject<Mobile> {

    private String mobile;

    /** 有效性正则 */
    private static final Pattern VALID_PATTERN = Pattern.compile(
            "^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$");

    /**
     * Constructor.
     *
     * @param mobile
     */
    public Mobile(final String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            throw new IllegalArgumentException("手机号不能为空");
        }
        Validate.isTrue(VALID_PATTERN.matcher(mobile).matches(), "手机号格式不正确");
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    @Override
    public boolean sameValueAs(Mobile other) {
        return other != null && this.mobile.equals(other.mobile);
    }

    @Override
    public String toString() {
        return mobile;
    }
}
