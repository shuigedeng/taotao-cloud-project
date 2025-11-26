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
import com.taotao.boot.common.utils.lang.StringUtils;

/**
 * 菜单地址
 *
 * @author shuigedeng
 * @since 2021-02-08
 */
public class MenuUrl implements ValueObject<MenuUrl> {

    private String url;

    public MenuUrl(final String url) {
        if (StringUtils.isEmpty(url)) {
            throw new IllegalArgumentException("菜单地址不能为空");
        }
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean sameValueAs(MenuUrl other) {
        return other != null && this.url.equals(other.url);
    }

    @Override
    public String toString() {
        return url;
    }
}
