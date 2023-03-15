/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.tenant.biz.tenant;

import com.art.common.core.constant.GlobalStatusEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import com.art.common.mp.core.base.MpEntity;
import lombok.Data;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/10/1 16:22
 */
@Data
@TableName("sys_tenant_package")
public class TenantPackageDO extends MpEntity {

	/**
	 * 套餐名
	 */
	private String name;

	/**
	 * 套餐状态
	 * <p>
	 * 枚举 {@link GlobalStatusEnum}
	 */
	private Integer status;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 套餐关联的菜单编号
	 */
	private String menuIds;

}
