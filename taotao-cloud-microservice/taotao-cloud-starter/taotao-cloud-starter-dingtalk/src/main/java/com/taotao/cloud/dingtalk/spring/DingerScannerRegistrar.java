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
package com.taotao.cloud.dingtalk.spring;

import com.taotao.cloud.dingtalk.model.ClassPathDingerScanner;
import com.taotao.cloud.dingtalk.model.DefaultDingerDefinitionResolver;
import com.taotao.cloud.dingtalk.annatations.DingerScan;
import com.taotao.cloud.dingtalk.enums.ExceptionEnum;
import com.taotao.cloud.dingtalk.exception.DingerAnalysisException;
import com.taotao.cloud.dingtalk.exception.DingerException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

/**
 * DingerScannerRegistrar
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 15:25:22
 */
public class DingerScannerRegistrar extends DefaultDingerDefinitionResolver implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
		BeanDefinitionRegistry registry) {
		AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(
			importingClassMetadata.getAnnotationAttributes(DingerScan.class.getName())
		);

		if (annoAttrs != null) {
			registerBeanDefinitions(annoAttrs, registry);
		}
	}

	void registerBeanDefinitions(AnnotationAttributes annoAttrs, BeanDefinitionRegistry registry) {
		ClassPathDingerScanner scanner = new ClassPathDingerScanner(registry);

		Class<? extends Annotation> annotationClass = annoAttrs.getClass("annotationClass");
		if (!Annotation.class.equals(annotationClass)) {
			scanner.setAnnotationClass(annotationClass);
		}

		Class<?> markerInterface = annoAttrs.getClass("markerInterface");
		if (!Class.class.equals(markerInterface)) {
			scanner.setMarkerInterface(markerInterface);
		}

		Class<? extends BeanNameGenerator> generatorClass = annoAttrs.getClass("nameGenerator");
		if (!BeanNameGenerator.class.equals(generatorClass)) {
			scanner.setBeanNameGenerator(BeanUtils.instantiateClass(generatorClass));
		}

		List<String> basePackages = new ArrayList<>();

		basePackages.addAll(
			Arrays.stream(annoAttrs.getStringArray("value"))
				.filter(StringUtils::hasText)
				.collect(Collectors.toList())
		);

		basePackages.addAll(
			Arrays.stream(annoAttrs.getStringArray("basePackages"))
				.filter(StringUtils::hasText)
				.collect(Collectors.toList())
		);

		scanner.registerFilters();
		scanner.doScan(
			StringUtils.toStringArray(basePackages)
		);

		try {
			resolver(scanner.getDingerClasses());
			dingerClasses = scanner.getDingerClasses();
		} catch (DingerException ex) {
			throw new DingerAnalysisException(ex.getPairs(), ex.getMessage());
		} catch (Exception ex) {
			throw new DingerException(ex, ExceptionEnum.UNKNOWN);
		}
	}

}
