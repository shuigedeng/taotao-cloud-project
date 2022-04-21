/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.data.mybatis.plus.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
/**
 * MybatisPlusAutoFillProperties 
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:44:25
 */
@RefreshScope
@ConfigurationProperties(prefix = MybatisPlusAutoFillProperties.PREFIX)
public class MybatisPlusAutoFillProperties {

	public static final String PREFIX = "taotao.cloud.data.mybatis-plus.auto-fill";

	/**
	 * 是否开启自动填充字段
	 */
	private Boolean enabled = true;
	/**
	 * 是否开启了插入填充
	 */
	private Boolean enableInsertFill = true;
	/**
	 * 是否开启了更新填充
	 */
	private Boolean enableUpdateFill = true;
	/**
	 * 创建时间字段名
	 */
	private String idField = "id";

	public String getIdField() {
		return idField;
	}

	public void setIdField(String idField) {
		this.idField = idField;
	}

	/**
	 * 创建时间字段名
	 */
	private String createTimeField = "createTime";
	/**
	 * 更新时间字段名
	 */
	private String updateTimeField = "updateTime";
	/**
	 * 创建时间字段名
	 */
	private String createByField = "createBy";
	/**
	 * 更新时间字段名
	 */
	private String updateByField = "updateBy";

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getEnableInsertFill() {
		return enableInsertFill;
	}

	public void setEnableInsertFill(Boolean enableInsertFill) {
		this.enableInsertFill = enableInsertFill;
	}

	public Boolean getEnableUpdateFill() {
		return enableUpdateFill;
	}

	public void setEnableUpdateFill(Boolean enableUpdateFill) {
		this.enableUpdateFill = enableUpdateFill;
	}

	public String getCreateTimeField() {
		return createTimeField;
	}

	public void setCreateTimeField(String createTimeField) {
		this.createTimeField = createTimeField;
	}

	public String getUpdateTimeField() {
		return updateTimeField;
	}

	public void setUpdateTimeField(String updateTimeField) {
		this.updateTimeField = updateTimeField;
	}

	public String getCreateByField() {
		return createByField;
	}

	public void setCreateByField(String createByField) {
		this.createByField = createByField;
	}

	public String getUpdateByField() {
		return updateByField;
	}

	public void setUpdateByField(String updateByField) {
		this.updateByField = updateByField;
	}
}
