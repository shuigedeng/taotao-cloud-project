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

package com.taotao.cloud.workflow.api.vo;

import java.util.Date;
import lombok.Data;
import lombok.experimental.*;

@Data
public class ColumnsPurviewEntity {

    /** 主键 */
    private String id;
    /** 列表字段数组 */
    private String fieldList;
    /** 模块ID */
    private String moduleId;

    /** 排序码 */
    private Long sortCode;

    /** 有效标志 */
    private Integer enabledMark;

    /** 创建时间 */
    private Date creatorTime;

    /** 创建用户 */
    private String creatorUserId;

    /** 修改时间 */
    private Date lastModifyTime;

    /** 修改用户 */
    private String lastModifyUserId;

    /** 删除时间 */
    private Date deleteTime;

    /** 删除用户 */
    private String deleteUserId;

    /** 删除标志 */
    private Integer deleteMark;
}
