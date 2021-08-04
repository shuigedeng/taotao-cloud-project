package com.taotao.cloud.web.configuration;

import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.spring.DozerBeanMapperFactoryBean;
import com.taotao.cloud.web.properties.DozerProperties;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * Dozer配置
 *
 * @link http://dozer.sourceforge.net/documentation/usage.html http://www.jianshu.com/p/bf8f0e8aee23
 */
@ConditionalOnClass({DozerBeanMapperFactoryBean.class, Mapper.class})
@ConditionalOnMissingBean(Mapper.class)
@ConditionalOnProperty(prefix = DozerProperties.PREFIX, name = "enabled", havingValue = "true")
public class DozerConfiguration {

	private final DozerProperties properties;

	/**
	 * Constructor for creating auto configuration.
	 *
	 * @param properties properties
	 */
	public DozerConfiguration(DozerProperties properties) {
		this.properties = properties;
	}

	@Bean
	public DozerHelper getDozerUtil(Mapper mapper) {
		return new DozerHelper(mapper);
	}

	/**
	 * Creates default Dozer mapper
	 *
	 * @return Dozer mapper
	 * @throws IOException if there is an exception during initialization.
	 */
	@Bean
	public DozerBeanMapperFactoryBean dozerMapper() throws IOException {
		DozerBeanMapperFactoryBean factoryBean = new DozerBeanMapperFactoryBean();
		// 官方这样子写，没法用 匹配符！
		//factoryBean.setMappingFiles(properties.getMappingFiles());
		factoryBean.setMappingFiles(properties.resolveMapperLocations());
		return factoryBean;
	}

	public class DozerHelper {

		private Mapper mapper;

		public DozerHelper(Mapper mapper) {
			this.mapper = mapper;
		}

		public Mapper getMapper() {
			return this.mapper;
		}

		/**
		 * Constructs new instance of destinationClass and performs mapping between from source
		 *
		 * @param source
		 * @param destinationClass
		 * @param <T>
		 * @return
		 */
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

		/**
		 * Performs mapping between source and destination objects
		 *
		 * @param source
		 * @param destination
		 */
		public void map(Object source, Object destination) {
			if (source == null) {
				return;
			}
			mapper.map(source, destination);
		}

		/**
		 * Constructs new instance of destinationClass and performs mapping between from source
		 *
		 * @param source
		 * @param destinationClass
		 * @param mapId
		 * @param <T>
		 * @return
		 */
		public <T> T map(Object source, Class<T> destinationClass, String mapId) {
			if (source == null) {
				return null;
			}
			return mapper.map(source, destinationClass, mapId);
		}

		/**
		 * Performs mapping between source and destination objects
		 *
		 * @param source
		 * @param destination
		 * @param mapId
		 */
		public void map(Object source, Object destination, String mapId) {
			if (source == null) {
				return;
			}
			mapper.map(source, destination, mapId);
		}

		/**
		 * 将集合转成集合 List<A> -->  List<B>
		 *
		 * @param sourceList       源集合
		 * @param destinationClass 待转类型
		 * @param <T>
		 * @return
		 */
		public <T, E> List<T> mapList(Collection<E> sourceList, Class<T> destinationClass) {
			return mapPage(sourceList, destinationClass);
		}


		public <T, E> List<T> mapPage(Collection<E> sourceList, Class<T> destinationClass) {
			if (sourceList == null || sourceList.isEmpty() || destinationClass == null) {
				return Collections.emptyList();
			}
			List<T> destinationList = sourceList.parallelStream()
				.filter(item -> item != null)
				.map((sourceObject) -> mapper.map(sourceObject, destinationClass))
				.collect(Collectors.toList());

			return destinationList;
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
