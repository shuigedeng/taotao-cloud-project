/**
 * Copyright (c) 2015-2016 the original author or authors
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.taotao.cloud.rxjava.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * RxJava
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:54:47
 */
@Target({ElementType.TYPE, ElementType.PARAMETER, ElementType.METHOD, ElementType.CONSTRUCTOR,
	ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier
public @interface RxJava {

}
