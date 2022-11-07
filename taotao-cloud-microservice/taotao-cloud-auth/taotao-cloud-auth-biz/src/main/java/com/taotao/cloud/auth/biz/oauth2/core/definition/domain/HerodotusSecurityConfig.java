/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.oauth2.core.definition.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 自定义SecurityConfig </p>
 * <p>
 * 自定义SecurityConfig，主要为了构建无参数构造函数，以解决序列化出错问题
 *
 * @author : gengwei.zheng
 * @date : 2021/9/11 15:57
 */
public class HerodotusSecurityConfig implements ConfigAttribute {

    private String attrib;

    public HerodotusSecurityConfig() {
    }

    public HerodotusSecurityConfig(String config) {
        Assert.hasText(config, "You must provide a configuration attribute");
        this.attrib = config;
    }

    @Override
    public String getAttribute() {
        return this.attrib;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HerodotusSecurityConfig that = (HerodotusSecurityConfig) o;
        return Objects.equal(attrib, that.attrib);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(attrib);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("attrib", attrib)
                .toString();
    }

    public static List<ConfigAttribute> createListFromCommaDelimitedString(String access) {
        return createList(StringUtils.commaDelimitedListToStringArray(access));
    }

    public static List<ConfigAttribute> createList(String... attributeNames) {
        Assert.notNull(attributeNames, "You must supply an array of attribute names");
        List<ConfigAttribute> attributes = new ArrayList<>(attributeNames.length);
        for (String attribute : attributeNames) {
            attributes.add(new HerodotusSecurityConfig(attribute.trim()));
        }
        return attributes;
    }
}
