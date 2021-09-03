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
package com.taotao.cloud.web.properties;


import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * DozerProperties
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:14:08
 */
@RefreshScope
@ConfigurationProperties(prefix = DozerProperties.PREFIX)
public class DozerProperties {

	public static final String PREFIX = "taotao.cloud.web.dozer";

	private boolean enabled = true;

	private static final ResourcePatternResolver PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();

	/**
	 * Mapping files configuration. For example "classpath:*.dozer.xml".
	 */
	private String[] mappingFiles = new String[]{"classpath*:dozer/*.dozer.xml"};

	/**
	 * Mapping files configuration.
	 *
	 * @return mapping files
	 */
	public String[] getMappingFiles() {
		return Arrays.copyOf(mappingFiles, mappingFiles.length);
	}

	/**
	 * Set mapping files configuration. For example <code>classpath:*.dozer.xml</code>.
	 *
	 * @param mappingFiles dozer mapping files
	 * @return dozer properties
	 */
	public DozerProperties setMappingFiles(String[] mappingFiles) {
		this.mappingFiles = Arrays.copyOf(mappingFiles, mappingFiles.length);
		return this;
	}

	/**
	 * resolveMapperLocations
	 *
	 * @return org.springframework.core.io.Resource[]
	 * @author shuigedeng
	 * @since 2021-09-02 22:14:32
	 */
	public Resource[] resolveMapperLocations() {
		return Stream.of(Optional.ofNullable(this.mappingFiles).orElse(new String[0]))
				.flatMap(location -> Stream.of(getResources(location)))
				.toArray(Resource[]::new);
	}

	/**
	 * getResources
	 *
	 * @param location location
	 * @return org.springframework.core.io.Resource[]
	 * @author shuigedeng
	 * @since 2021-09-02 22:14:22
	 */
	private Resource[] getResources(String location) {
		try {
			return PATTERN_RESOLVER.getResources(location);
		} catch (IOException var3) {
			return new Resource[0];
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
