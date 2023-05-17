/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
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
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.dante.authorization.definition;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * <p>Description: 自定义 AntPathRequestMatcher </p>
 * <p>
 * 基于 AntPathRequestMatcher 代码，扩展了一些方法，解决原有 AntPathRequestMatcher 使用不方便问题。
 *
 * @author : gengwei.zheng
 * @date : 2022/3/9 10:47
 */
public final class HerodotusRequest implements Serializable {

    private String pattern;

    private String httpMethod;

    private boolean hasWildcard;

    public HerodotusRequest() {
    }

    /**
     * Creates a matcher with the specific pattern which will match all HTTP methods in a
     * case sensitive manner.
     *
     * @param pattern the ant pattern to use for matching
     */
    public HerodotusRequest(String pattern) {
        this(pattern, null);
    }

    /**
     * Creates a matcher with the supplied pattern which will match the specified Http
     * method
     *
     * @param pattern    the ant pattern to use for matching
     * @param httpMethod the HTTP method. The {@code matches} method will return false if
     *                   the incoming request doesn't have the same method.
     */
    public HerodotusRequest(String pattern, String httpMethod) {
        Assert.hasText(pattern, "Pattern cannot be null or empty");
        this.pattern = pattern;
        this.hasWildcard = containSpecialCharacters(pattern);
        this.httpMethod = checkHttpMethod(httpMethod);
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public boolean isHasWildcard() {
        return hasWildcard;
    }

    private String checkHttpMethod(String method) {
        if (StringUtils.isNotBlank(method)) {
            HttpMethod httpMethod = HttpMethod.valueOf(method);
            if (ObjectUtils.isNotEmpty(httpMethod)) {
                return httpMethod.name();
            }
        }
        return null;
    }

    private boolean containSpecialCharacters(String source) {
        if (StringUtils.isNotBlank(source)) {
            return StringUtils.containsAny(source, new String[]{"*", "?", "{"});
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HerodotusRequest that = (HerodotusRequest) o;
        return Objects.equal(pattern, that.pattern) && Objects.equal(httpMethod, that.httpMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pattern, httpMethod);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("pattern", pattern)
                .add("httpMethod", httpMethod)
                .toString();
    }
}
