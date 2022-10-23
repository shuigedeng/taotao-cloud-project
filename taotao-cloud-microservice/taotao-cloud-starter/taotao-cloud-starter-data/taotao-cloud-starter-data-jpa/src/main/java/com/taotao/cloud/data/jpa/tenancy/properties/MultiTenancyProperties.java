package com.taotao.cloud.data.jpa.tenancy.properties;

import com.taotao.cloud.data.jpa.tenancy.DataResource;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.MultiTenancyStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * <p>Description: 自定义 JPA 配置 </p>
 */
@ConfigurationProperties(prefix = "taotao.cloud.multi-tenancy.enabled")
public class MultiTenancyProperties {

	/**
	 * 是否开始多租户
	 */
	private Boolean enabled;

	/**
	 * 数据资源类型
	 */
	private DataResource resource = DataResource.DATABASE;

	/**
	 * 多租户数据隔离策略
	 */
	private MultiTenancyStrategy tenancyStrategy = MultiTenancyStrategy.DATABASE;

	private String[] packageToScan;

	/**
	 * 多租户数据源配置
	 */
	private Map<String, MultiTenancyDataSource> dataSources;

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public DataResource getResource() {
		return resource;
	}

	public void setResource(DataResource resource) {
		this.resource = resource;
	}

	public Map<String, MultiTenancyDataSource> getDataSources() {
		return dataSources;
	}

	public void setDataSources(Map<String, MultiTenancyDataSource> dataSources) {
		this.dataSources = dataSources;
	}

	public MultiTenancyStrategy getTenancyStrategy() {
		return tenancyStrategy;
	}

	public void setTenancyStrategy(MultiTenancyStrategy tenancyStrategy) {
		this.tenancyStrategy = tenancyStrategy;
	}

	public String[] getPackageToScan() {
		String defaultPackage = "cn.herodotus";
		if (ArrayUtils.isEmpty(packageToScan)) {
			return new String[]{defaultPackage};
		} else {
			return ArrayUtils.add(packageToScan, defaultPackage);
		}
	}

	public void setPackageToScan(String[] packageToScan) {
		this.packageToScan = packageToScan;
	}
}
