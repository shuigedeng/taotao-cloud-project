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

package com.taotao.cloud.sa.just.biz.just.justauth.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;

/**
 * 租户第三方登录功能配置表
 *
 * @since 2022-05-16
 */
@HeadRowHeight(20)
@ContentRowHeight(15)
@Data
@Schema(description = "租户第三方登录功能配置表数据导出")
public class JustAuthConfigExport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "登录开关")
    @ExcelProperty(value = "登录开关", index = 0)
    @ColumnWidth(20)
    private Boolean enabled;

    @Schema(description = "配置类")
    @ExcelProperty(value = "配置类", index = 1)
    @ColumnWidth(20)
    private String enumClass;

    @Schema(description = "Http超时")
    @ExcelProperty(value = "Http超时", index = 2)
    @ColumnWidth(20)
    private Integer httpTimeout;

    @Schema(description = "缓存类型")
    @ExcelProperty(value = "缓存类型", index = 3)
    @ColumnWidth(20)
    private String cacheType;

    @Schema(description = "缓存前缀")
    @ExcelProperty(value = "缓存前缀", index = 4)
    @ColumnWidth(20)
    private String cachePrefix;

    @Schema(description = "缓存超时")
    @ExcelProperty(value = "缓存超时", index = 5)
    @ColumnWidth(20)
    private Integer cacheTimeout;

    @Schema(description = "状态")
    @ExcelProperty(value = "状态", index = 6)
    @ColumnWidth(20)
    private Integer status;

    @Schema(description = "备注")
    @ExcelProperty(value = "备注", index = 7)
    @ColumnWidth(20)
    private String remark;
}
