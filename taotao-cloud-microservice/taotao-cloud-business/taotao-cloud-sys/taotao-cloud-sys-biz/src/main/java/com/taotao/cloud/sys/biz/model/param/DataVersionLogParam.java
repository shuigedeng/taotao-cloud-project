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

package com.taotao.cloud.sys.biz.model.param;

import com.taotao.boot.common.model.request.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.*;
import lombok.EqualsAndHashCode;

/**
 * @author shuigedeng
 * @since 2022/1/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "数据版本日志")
public class DataVersionLogParam extends PageQuery {

    @Schema(description = "表名称")
    private String tableName;

    @Schema(description = "数据名称")
    private String dataName;

    @Schema(description = "数据主键")
    private String dataId;

    @Schema(description = "数据内容对象")
    private Object dataContent;

    @Schema(description = "本次变动的数据内容")
    private Object changeContent;

    @Schema(description = "版本")
    private Integer version;
}
