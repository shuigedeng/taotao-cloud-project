/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
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
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.data.jpa.tenancy.properties;

import com.taotao.cloud.data.jpa.tenancy.DataResource;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.MultiTenancyStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>Description: 自定义 JPA 配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/9/8 18:22
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
