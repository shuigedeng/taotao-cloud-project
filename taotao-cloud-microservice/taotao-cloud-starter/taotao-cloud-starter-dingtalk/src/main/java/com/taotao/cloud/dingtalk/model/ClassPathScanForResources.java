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
package com.taotao.cloud.dingtalk.model;

import static com.taotao.cloud.dingtalk.enums.ExceptionEnum.RESOURCE_CONFIG_EXCEPTION;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.dingtalk.exception.DingerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.ClassUtils;


/**
 * ClassPathScanForResources
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 15:22:20
 */
public final class ClassPathScanForResources {

	private static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
	private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
	private static final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

	/**
	 * 扫描包
	 *
	 * @param packageSearchPath 扫描的包路径， <code>classpath*:com.jaemon.dinger/**\/*.class</code>
	 * @return 包下的所有资源文件集
	 */
	public static Resource[] doScanPackage(String packageSearchPath) {
		try {
			return resolver.getResources(packageSearchPath);
		} catch (IOException ex) {
			LogUtil.error(packageSearchPath, ex);
			throw new DingerException(RESOURCE_CONFIG_EXCEPTION, packageSearchPath);
		}
	}

	/**
	 * @param basePackage 包名， eg： <code>com.jaemon.dinger</code>
	 * @return 包下的所有接口集合
	 */
	public static List<Class<?>> scanInterfaces(String basePackage) {
		return scanClasses(basePackage, true);
	}


	/**
	 * @param basePackage 包名， eg： <code>com.jaemon.dinger</code>
	 * @return 包下的所有类集合
	 */
	public static List<Class<?>> scanClasses(String basePackage) {
		return scanClasses(basePackage, false);
	}


	/**
	 * @param basePackage     包名， eg： <code>com.jaemon.dinger</code>
	 * @param filterInterface 是否过滤接口
	 * @return 包下的所有类集合
	 */
	private static List<Class<?>> scanClasses(String basePackage, boolean filterInterface) {
		String packageSearchPath = CLASSPATH_ALL_URL_PREFIX +
			resolveBasePackage(basePackage) + "/" + DEFAULT_RESOURCE_PATTERN;
		Resource[] resources = doScanPackage(packageSearchPath);

		List<Class<?>> classes = new ArrayList<>();

		if (resources.length == 0) {
			return classes;
		}

		SimpleMetadataReaderFactory factory = new SimpleMetadataReaderFactory();
		for (Resource resource : resources) {
			String resourceFilename = resource.getFilename();
			if (!resource.isReadable()) {
				LogUtil.debug("Ignored because not readable: {} ", resourceFilename);
				continue;
			}
			try {
				MetadataReader metadataReader = factory.getMetadataReader(resource);
				ClassMetadata classMetadata = metadataReader.getClassMetadata();
				Class<?> clazz = Class.forName(classMetadata.getClassName());
				if (filterInterface && !clazz.isInterface()) {
					LogUtil.debug("source class={} is interface and skip.", resourceFilename);
					continue;
				}
				classes.add(clazz);
			} catch (IOException | ClassNotFoundException e) {
				LogUtil.warn("resource={} read exception and message={}.", resourceFilename,
					e.getMessage());
				continue;
			}
		}
		return classes;
	}

	private static String resolveBasePackage(String basePackage) {
		return ClassUtils.convertClassNameToResourcePath(basePackage);
	}
}
