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

package com.taotao.cloud.workflow.biz.common.database.model.dto;

import lombok.Data;

/** 数据源参数传输对象 -- 注意：这里的参数dataSourceUtil是spring托管的全局唯一变量，此数据传输对象防止数据源互串 */
@Data
public class DataSourceDTO extends DataSourceUtil {

    /** 数据来源 0：自身创建 1：配置 2：数据连接 */
    private Integer dataSourceFrom;

    /** 表名 */
    private String tableName;
}
