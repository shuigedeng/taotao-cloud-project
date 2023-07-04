/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
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
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.strategy.user;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

/**
 * <p>Description: 系统权限实体 </p>
 *
 */
@Schema(name = "系统权限")
@Entity
@Table(name = "sys_permission", indexes = {@Index(name = "sys_permission_id_idx", columnList = "permission_id")})
@Cacheable
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = UpmsConstants.REGION_SYS_PERMISSION)
public class SysPermission {

	@Schema(name = "权限ID")
	@Id
	@UuidGenerator
	@Column(name = "permission_id", length = 64)
	private String permissionId;

	@Schema(name = "权限代码")
	@Column(name = "permission_code", length = 128)
	private String permissionCode;

	@Schema(name = "权限名称")
	@Column(name = "permission_name", length = 1024)
	private String permissionName;

	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}

	public String getPermissionCode() {
		return permissionCode;
	}

	public void setPermissionCode(String permissionCode) {
		this.permissionCode = permissionCode;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SysPermission that = (SysPermission) o;
		return Objects.equal(permissionId, that.permissionId);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(permissionId);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("permissionId", permissionId)
			.add("permissionCode", permissionCode)
			.add("permissionName", permissionName)
			.toString();
	}
}
