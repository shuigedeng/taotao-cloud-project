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

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 数据表
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
@TableName("gen_table")
public class TableEntity {
    @TableId private Long id;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 实体类名称
     */
    private String className;

    /**
     * 功能名
     */
    private String tableComment;

    /**
     * 项目包名
     */
    private String packageName;

    /**
     * 项目版本号
     */
    private String version;

    /**
     * 作者
     */
    private String author;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 生成方式  1：zip压缩包   2：自定义目录
     */
    private Integer generatorType;

    /**
     * 后端生成路径
     */
    private String backendPath;

    /**
     * 前端生成路径
     */
    private String frontendPath;

    /**
     * 模块名
     */
    private String moduleName;

    /**
     * 功能名
     */
    private String functionName;

    /**
     * 表单布局
     */
    private Integer formLayout;

    /**
     * 数据源ID
     */
    private Long datasourceId;

    /**
     * 基类ID
     */
    @TableField(updateStrategy = FieldStrategy.DEFAULT)
    private Long baseclassId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 字段列表
     */
    @TableField(exist = false)
    private List<TableFieldEntity> fieldList;
}
