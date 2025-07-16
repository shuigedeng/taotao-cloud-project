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

package com.taotao.cloud.generator.maku.common.query;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * 查询公共参数
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
public class Query {
    String code;
    String tableName;
    String attrType;
    String columnType;
    String connName;
    String dbType;
    String projectName;

    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小值为 1")
    Integer page;

    @NotNull(message = "每页条数不能为空")
    @Range(min = 1, max = 1000, message = "每页条数，取值范围 1-1000")
    Integer limit;
}
