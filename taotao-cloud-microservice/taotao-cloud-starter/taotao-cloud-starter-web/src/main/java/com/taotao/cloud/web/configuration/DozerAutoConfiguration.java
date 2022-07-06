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
package com.taotao.cloud.web.configuration;

import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.spring.DozerBeanMapperFactoryBean;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.web.properties.DozerProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DozerConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @see <a>http://dozer.sourceforge.net/documentation/usage.html</a>
 * @see <a>http://www.jianshu.com/p/bf8f0e8aee23</a>
 * @since 2021-09-02 21:24:03
 */
@AutoConfiguration
@ConditionalOnMissingBean(Mapper.class)
@EnableConfigurationProperties({DozerProperties.class})
@ConditionalOnClass({DozerBeanMapperFactoryBean.class, Mapper.class})
@ConditionalOnProperty(prefix = DozerProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class DozerAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(DozerAutoConfiguration.class, StarterName.WEB_STARTER);
	}

	@Bean
	public DozerHelper dozerHelper(Mapper mapper) {
		return new DozerHelper(mapper);
	}

	@Bean
	public DozerBeanMapperFactoryBean dozerMapper(DozerProperties properties) throws IOException {
		DozerBeanMapperFactoryBean factoryBean = new DozerBeanMapperFactoryBean();
		// 官方这样子写，没法用 匹配符！
		// factoryBean.setMappingFiles(properties.getMappingFiles());
		factoryBean.setMappingFiles(properties.resolveMapperLocations());
		return factoryBean;
	}

	/**
	 * DozerHelper
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 21:24:48
	 */
	public static class DozerHelper {

		private final Mapper mapper;

		public DozerHelper(Mapper mapper) {
			this.mapper = mapper;
		}

		public Mapper getMapper() {
			return this.mapper;
		}

		/**
		 * map
		 *
		 * @param source           数据源
		 * @param destinationClass 目标类
		 * @param <T>              T
		 * @return 对象
		 * @since 2021-09-02 21:24:52
		 */
		public <T> T map(Object source, Class<T> destinationClass) {
			if (source == null) {
				return null;
			}
			return mapper.map(source, destinationClass);
		}

		/**
		 * map2
		 *
		 * @param source           数据源
		 * @param destinationClass 目标类
		 * @return 对象
		 * @since 2021-09-02 21:25:04
		 */
		public <T> T map2(Object source, Class<T> destinationClass) {
			if (source == null) {
				try {
					return destinationClass.getDeclaredConstructor().newInstance();
				} catch (Exception e) {
				}
			}
			return mapper.map(source, destinationClass);
		}

		/**
		 * map
		 *
		 * @param source      数据源
		 * @param destination 目标类
		 * @since 2021-09-02 21:25:12
		 */
		public void map(Object source, Object destination) {
			if (source == null) {
				return;
			}
			mapper.map(source, destination);
		}

		/**
		 * map
		 *
		 * @param source           数据源
		 * @param destinationClass 目标类
		 * @param mapId            mapId
		 * @return 结果对象
		 * @since 2021-09-02 21:25:15
		 */
		public <T> T map(Object source, Class<T> destinationClass, String mapId) {
			if (source == null) {
				return null;
			}
			return mapper.map(source, destinationClass, mapId);
		}

		/**
		 * map
		 *
		 * @param source      数据源
		 * @param destination 目标类
		 * @param mapId       mapId
		 * @since 2021-09-02 21:25:21
		 */
		public void map(Object source, Object destination, String mapId) {
			if (source == null) {
				return;
			}
			mapper.map(source, destination, mapId);
		}

		/**
		 * mapList
		 *
		 * @param sourceList       数据源
		 * @param destinationClass 目标类
		 * @return 数据集合
		 * @since 2021-09-02 21:25:26
		 */
		public <T, E> List<T> mapList(Collection<E> sourceList, Class<T> destinationClass) {
			return mapPage(sourceList, destinationClass);
		}

		/**
		 * mapPage
		 *
		 * @param sourceList       数据源
		 * @param destinationClass 目标类
		 * @return 分页结果
		 * @since 2021-09-02 21:25:36
		 */
		public <T, E> List<T> mapPage(Collection<E> sourceList, Class<T> destinationClass) {
			if (sourceList == null || sourceList.isEmpty() || destinationClass == null) {
				return Collections.emptyList();
			}

			return sourceList.parallelStream()
				.filter(Objects::nonNull)
				.map((sourceObject) -> mapper.map(sourceObject, destinationClass))
				.collect(Collectors.toList());
		}

		/**
		 * mapSet
		 *
		 * @param sourceList       数据源
		 * @param destinationClass 目标类
		 * @return 数据集合
		 * @since 2021-09-02 21:25:56
		 */
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
