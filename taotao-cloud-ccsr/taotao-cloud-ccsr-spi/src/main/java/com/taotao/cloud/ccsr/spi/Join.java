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

package com.taotao.cloud.ccsr.spi;

import java.lang.annotation.*;

/**
 * Join
 * Adding this annotation to a class indicates joining the extension mechanism.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Join {

    /**
     * It will be sorted according to the current serial number.
     *
     * @return int.
     */
    int order() default 0;

    /**
     * Indicates that the object joined by @Join is a singleton,
     * otherwise a completely new instance is created each time.
     *
     * @return true or false.
     */
    boolean isSingleton() default true;
}
