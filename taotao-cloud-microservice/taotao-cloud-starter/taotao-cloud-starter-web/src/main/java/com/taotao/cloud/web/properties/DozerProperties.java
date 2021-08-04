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
 * Dozer属性
 *
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

	public Resource[] resolveMapperLocations() {
		return Stream.of(Optional.ofNullable(this.mappingFiles).orElse(new String[0]))
			.flatMap(location -> Stream.of(getResources(location)))
			.toArray(Resource[]::new);
	}

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
