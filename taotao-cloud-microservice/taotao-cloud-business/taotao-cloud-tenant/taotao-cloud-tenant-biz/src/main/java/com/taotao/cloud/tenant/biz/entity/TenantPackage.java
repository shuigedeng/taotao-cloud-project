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

package com.taotao.cloud.tenant.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.common.enums.GlobalStatusEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import lombok.Data;

@Data
@TableName("tenant_package")
public class TenantPackage extends BaseSuperEntity<TenantPackage, Long> {

    /** 套餐名 */
    private String name;

    /**
     * 套餐状态
     *
     * <p>枚举 {@link GlobalStatusEnum}
     */
    private Integer status;

    /** 备注 */
    private String remark;

    /** 套餐关联的菜单编号 */
    private String menuIds;
}
