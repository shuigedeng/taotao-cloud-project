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

package com.taotao.cloud.workflow.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("base_sysconfig")
public class SysConfigEntity {

    /** 主键 */
    @TableId("F_ID")
    private String id;

    /** 名称 */
    @TableField("F_NAME")
    private String name;

    /** 键 */
    @TableField("F_KEY")
    private String fkey;

    /** 值 */
    @TableField("F_VALUE")
    private String value;

    /** 分类 */
    @TableField("F_CATEGORY")
    private String category;
}
