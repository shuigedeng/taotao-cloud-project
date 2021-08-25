/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.web.configuration;

import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.spring.DozerBeanMapperFactoryBean;
import com.taotao.cloud.web.properties.DozerProperties;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * Dozer配置
 *
 * @author shuigedeng
 * @version 1.0.0
 * @see <a href="http://dozer.sourceforge.net/documentation/usage.html">http://www.jianshu.com/p/bf8f0e8aee23</a>
 * @since 2021/8/24 23:44
 */
@ConditionalOnClass({DozerBeanMapperFactoryBean.class, Mapper.class})
@ConditionalOnMissingBean(Mapper.class)
@ConditionalOnProperty(prefix = DozerProperties.PREFIX, name = "enabled", havingValue = "true")
public class DozerConfiguration {

	private final DozerProperties properties;

	public DozerConfiguration(DozerProperties properties) {
		this.properties = properties;
	}

	@Bean
	public DozerHelper getDozerUtil(Mapper mapper) {
		return new DozerHelper(mapper);
	}

	@Bean
	public DozerBeanMapperFactoryBean dozerMapper() throws IOException {
		DozerBeanMapperFactoryBean factoryBean = new DozerBeanMapperFactoryBean();
		// 官方这样子写，没法用 匹配符！
		// factoryBean.setMappingFiles(properties.getMappingFiles());
		factoryBean.setMappingFiles(properties.resolveMapperLocations());
		return factoryBean;
	}

	/**
	 * DozerHelper
	 *
	 * @version 1.0.0
	 * @author shuigedeng
	 * @since 2021/8/24 23:45
	 */
	public static class DozerHelper {

		private final Mapper mapper;

		public DozerHelper(Mapper mapper) {
			this.mapper = mapper;
		}

		public Mapper getMapper() {
			return this.mapper;
		}

		public <T> T map(Object source, Class<T> destinationClass) {
			if (source == null) {
				return null;
			}
			return mapper.map(source, destinationClass);
		}

		public <T> T map2(Object source, Class<T> destinationClass) {
			if (source == null) {
				try {
					return destinationClass.newInstance();
				} catch (Exception e) {
				}
			}
			return mapper.map(source, destinationClass);
		}

		public void map(Object source, Object destination) {
			if (source == null) {
				return;
			}
			mapper.map(source, destination);
		}

		public <T> T map(Object source, Class<T> destinationClass, String mapId) {
			if (source == null) {
				return null;
			}
			return mapper.map(source, destinationClass, mapId);
		}

		public void map(Object source, Object destination, String mapId) {
			if (source == null) {
				return;
			}
			mapper.map(source, destination, mapId);
		}

		public <T, E> List<T> mapList(Collection<E> sourceList, Class<T> destinationClass) {
			return mapPage(sourceList, destinationClass);
		}

		public <T, E> List<T> mapPage(Collection<E> sourceList, Class<T> destinationClass) {
			if (sourceList == null || sourceList.isEmpty() || destinationClass == null) {
				return Collections.emptyList();
			}

			return sourceList.parallelStream()
				.filter(Objects::nonNull)
				.map((sourceObject) -> mapper.map(sourceObject, destinationClass))
				.collect(Collectors.toList());
		}

		public <T, E> Set<T> mapSet(Collection<E> sourceList, Class<T> destinationClass) {
			if (sourceList == null || sourceList.isEmpty() || destinationClass == null) {
				return Collections.emptySet();
			}
			return sourceList.parallelStream()
				.map((sourceObject) -> mapper.map(sourceObject, destinationClass))
				.collect(Collectors.toSet());
		}
	}
}
