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

package com.taotao.cloud.sys.adapter.model.co.position;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 岗位查询对象
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/9/30 08:49
 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "岗位查询对象")
public class PositionQueryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4132785717179910025L;

    @Schema(description = "岗位名称")
    private String name;

    @Schema(description = "部门id")
    private Long deptId;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "排序值")
    private Integer sortNum;

    @Schema(description = "租户id")
    private String tenantId;
}
