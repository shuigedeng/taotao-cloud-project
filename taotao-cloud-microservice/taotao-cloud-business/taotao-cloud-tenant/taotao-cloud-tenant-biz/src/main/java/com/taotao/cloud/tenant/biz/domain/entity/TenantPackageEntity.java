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

package com.taotao.cloud.tenant.biz.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.boot.common.enums.GlobalStatusEnum;
import com.taotao.boot.webagg.entity.BaseLightSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = TenantPackageEntity.TABLE_NAME)
@TableName(TenantPackageEntity.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = TenantPackageEntity.TABLE_NAME)
public class TenantPackageEntity extends BaseLightSuperEntity<TenantPackageEntity> {

	public static final String TABLE_NAME = "tt_tenant_package";

	@Column(name = "`name`", columnDefinition = "varchar(255) null comment '套餐名'")
	private String name;

	@Column(name = "`status`", columnDefinition = "varchar(255) null comment '套餐状态'")
	private GlobalStatusEnum status;

	@Column(name = "`remark`", columnDefinition = "varchar(255) null comment '备注'")
	private String remark;

	@Column(name = "`menu_ids`", columnDefinition = "varchar(255) null comment '套餐关联的菜单编号'")
	private String menuIds;
}
