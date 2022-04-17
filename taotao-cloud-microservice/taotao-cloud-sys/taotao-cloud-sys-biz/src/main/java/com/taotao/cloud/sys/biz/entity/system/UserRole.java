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
package com.taotao.cloud.sys.biz.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.SuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用户-角色第三方表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:04:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = UserRole.TABLE_NAME)
@TableName(UserRole.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = UserRole.TABLE_NAME, comment = "用户-角色第三方表")
public class UserRole extends SuperEntity<UserRole,Long> {

	public static final String TABLE_NAME = "tt_user_role";

	/**
	 * 用户ID
	 */
	@Column(name = "user_id", columnDefinition = "bigint not null comment '用户ID'")
	private Long userId;

	/**
	 * 角色ID
	 */
	@Column(name = "role_id", columnDefinition = "bigint not null comment '角色ID'")
	private Long roleId;
}
