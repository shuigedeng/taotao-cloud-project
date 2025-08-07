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

package com.taotao.cloud.stock.biz.domain.permission.model.vo;

import com.taotao.cloud.stock.api.common.domain.ValueObject;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang.Validate;

/**
 * 权限编码
 *
 * @author shuigedeng
 * @since 2021-02-15
 */
public class PermissionCodes implements ValueObject<PermissionCodes> {

    private Set<String> codes;

    public PermissionCodes(final Set<String> codes) {
        Validate.notEmpty(codes);
        Validate.noNullElements(codes);

        this.codes = codes;
    }

    public Set<String> getCodes() {
        return codes;
    }

    public String getCodesString() {
        if (codes == null) {
            return null;
        }
        Object[] array = codes.toArray();
        return StringUtils.join(array, ",");
    }

    @Override
    public boolean sameValueAs(PermissionCodes other) {
        return other != null && codes.equals(other.codes);
    }

    @Override
    public String toString() {
        return "PermissionCodes{" + "codes=" + codes + '}';
    }
}
