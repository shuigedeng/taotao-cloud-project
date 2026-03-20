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

package com.taotao.cloud.tenant.biz.domain.aggregate;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.boot.common.enums.GlobalStatusEnum;
import com.taotao.boot.ddd.model.domain.repository.light.LightAggregateRoot;
import com.taotao.cloud.tenant.biz.domain.valobj.CodeVal;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = TenantAgg.TABLE_NAME,
	uniqueConstraints = {
		@UniqueConstraint(name = "uniq_apply_no", columnNames = "apply_no"),
	},
	indexes = {
		@Index(name = "idx_create_date", columnList = "`create_date`"),
	})
@TableName(TenantAgg.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = TenantAgg.TABLE_NAME)
public class TenantAgg extends LightAggregateRoot<TenantAgg> {
	public static final String TABLE_NAME = "tt_tenant";

	@Embedded
	private CodeVal codeVal;

	@Column(name = "`name`", columnDefinition = "varchar(255) null comment '租户名'")
    private String name;

	@Column(name = "`tenant_admin_id`", columnDefinition = "bigint not null comment '当前租户管理员id'")
    private Long tenantAdminId;

	@Column(name = "`tenant_admin_name`", columnDefinition = "varchar(255) null comment '当前租户管理员姓名'")
    private String tenantAdminName;

	@Column(name = "`tenant_admin_mobile`", columnDefinition = "varchar(255) null comment '当前租户管理员手机号'")
    private String tenantAdminMobile;

	@Enumerated(EnumType.STRING)
	@Column(name = "`status`", columnDefinition = "varchar(255) null comment '租户状态'")
    private GlobalStatusEnum status;

	@Column(name = "`package_id`", columnDefinition = "bigint  null comment '租户套餐id'")
    private Long packageId;

	@Column(name = "`expire_time`", columnDefinition = "DATETIME  null comment '租户过期时间'")
    private LocalDateTime expireTime;

	@Column(name = "`account_count`", columnDefinition = "int  null comment '账号数量'")
    private Integer accountCount;


	@Override
	public String identifier() {
		return codeVal.getApplyNo();
	}
}
