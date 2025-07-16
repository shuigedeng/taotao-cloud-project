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

package com.taotao.cloud.generator.maku.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 表字段
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
@TableName("gen_table_field")
public class TableFieldEntity {
    @TableId private Long id;

    /**
     * 表ID
     */
    private Long tableId;

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 字段类型
     */
    private String fieldType;

    /**
     * 字段说明
     */
    private String fieldComment;

    /**
     * 属性名
     */
    private String attrName;

    /**
     * 属性类型
     */
    private String attrType;

    /**
     * 属性包名
     */
    private String packageName;

    /**
     * 自动填充
     */
    private String autoFill;

    /**
     * 主键 0：否  1：是
     */
    private boolean primaryPk;

    /**
     * 基类字段 0：否  1：是
     */
    private boolean baseField;

    /**
     * 表单项 0：否  1：是
     */
    private boolean formItem;

    /**
     * 表单必填 0：否  1：是
     */
    private boolean formRequired;

    /**
     * 表单类型
     */
    private String formType;

    /**
     * 表单字典类型
     */
    private String formDict;

    /**
     * 表单效验
     */
    private String formValidator;

    /**
     * 列表项 0：否  1：是
     */
    private boolean gridItem;

    /**
     * 列表排序 0：否  1：是
     */
    private boolean gridSort;

    /**
     * 查询项 0：否  1：是
     */
    private boolean queryItem;

    /**
     * 查询方式
     */
    private String queryType;

    /**
     * 查询表单类型
     */
    private String queryFormType;
}
