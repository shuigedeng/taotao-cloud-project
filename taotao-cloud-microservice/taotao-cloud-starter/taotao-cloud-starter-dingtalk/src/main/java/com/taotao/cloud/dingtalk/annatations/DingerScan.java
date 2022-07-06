/*
 * Copyright (c) ©2015-2021 Jaemon. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
 */
package com.taotao.cloud.dingtalk.annatations;

import com.taotao.cloud.dingtalk.spring.DingerScannerRegistrar;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.Import;

/**
 * DingerScan
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 15:18:07
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DingerScannerRegistrar.class)
public @interface DingerScan {

	/**
	 * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation
	 * declarations e.g.: {@code @DingerScan("org.my.pkg")} instead of {@code
	 *
	 * @return {@link String[] }
	 * @DingerScan(basePackages = "org.my.pkg"})}.
	 * @since 2022-07-06 15:18:07
	 */
	String[] value() default {};

	/**
	 * Base packages to scan for Dinger interfaces. Note that only interfaces with at least one
	 * method will be registered; concrete classes will be ignored.
	 *
	 * @return {@link String[] }
	 * @since 2022-07-06 15:18:07
	 */
	String[] basePackages() default {};

	/**
	 * The {@link BeanNameGenerator} class to be used for naming detected components within the
	 * Spring container.
	 *
	 * @return {@link Class }<{@link ? } {@link extends } {@link BeanNameGenerator }>
	 * @since 2022-07-06 15:18:07
	 */
	Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;

	/**
	 * This property specifies the annotation that the scanner will search for.
	 * <p>
	 * The scanner will register all interfaces in the base package that also have the specified
	 * annotation.
	 * <p>
	 * Note this can be combined with markerInterface.
	 *
	 * @return {@link Class }<{@link ? } {@link extends } {@link Annotation }>
	 * @since 2022-07-06 15:18:07
	 */
	Class<? extends Annotation> annotationClass() default Annotation.class;

	/**
	 * This property specifies the parent that the scanner will search for.
	 * <p>
	 * The scanner will register all interfaces in the base package that also have the specified
	 * interface class as a parent.
	 * <p>
	 * Note this can be combined with annotationClass.
	 *
	 * @return {@link Class }<{@link ? }>
	 * @since 2022-07-06 15:18:08
	 */
	Class<?> markerInterface() default Class.class;
}
