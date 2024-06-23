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

package com.taotao.cloud.sys.api.shortlink.java.com.taotao.cloud.shorlink.api.api.common;

import java.io.Serializable;

public class Range<T> implements Serializable {

    private T lower;
    private T upper;

    public T getLower() {
        return this.lower;
    }

    public T getUpper() {
        return this.upper;
    }

    public boolean hasLowerBound() {
        return this.lower != null;
    }

    public boolean hasUpperBound() {
        return this.upper != null;
    }

    private Range() {}

    public static <T> Builder<T> newBuilder() {
        return new Builder();
    }

    public static class Builder<T> {

        private Range<T> range;

        private Builder() {
            this.range = new Range();
        }

        public Builder<T> lower(T lower) {
            this.range.lower = lower;
            return this;
        }

        public Builder<T> upper(T upper) {
            this.range.upper = upper;
            return this;
        }

        public Range<T> build() {
            return this.range;
        }
    }
}
