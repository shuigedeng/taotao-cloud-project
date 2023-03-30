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

package com.taotao.cloud.sys.biz.model.bo;

import io.soabase.recordbuilder.core.RecordBuilder;
import java.io.Serializable;

/**
 * 岗位查询对象
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-23 08:49:58
 */
@RecordBuilder
public record JobBO(
        /** 岗位名称 */
        String name,

        /** 部门id */
        Long deptId,

        /** 备注 */
        String remark,

        /** 排序值 */
        Integer sortNum,

        /** 租户id */
        String tenantId)
        implements Serializable {

    static final long serialVersionUID = -7605952923416404638L;
}
