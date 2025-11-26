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

package com.taotao.cloud.stock.biz.domain.tenant.model.vo;

import java.util.regex.Pattern;
import org.apache.commons.lang.Validate;
import com.taotao.boot.common.utils.lang.StringUtils;

/**
 * 租户编码
 *
 * @author shuigedeng
 * @since 2021-02-08
 */
public class TenantCode implements ValueObject<TenantCode> {

    private String code;

    private static final Pattern VALID_PATTERN = Pattern.compile("^[A-Za-z0-9]+$");

    public TenantCode(final String code) {
        if (StringUtils.isEmpty(code)) {
            throw new IllegalArgumentException("租户编码不能为空");
        }
        Validate.isTrue(VALID_PATTERN.matcher(code).matches(), "编码格式不正确");
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean sameValueAs(TenantCode other) {
        return other != null && this.code.equals(other.code);
    }

    @Override
    public String toString() {
        return code;
    }
}
