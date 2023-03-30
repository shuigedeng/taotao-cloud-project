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
 * 第三方用户绑定
 *
 * @since 2022-05-19
 */
@HeadRowHeight(20)
@ContentRowHeight(15)
@Data
@Schema(description = "第三方用户绑定数据导入模板")
public class JustAuthSocialUserImport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户id")
    @ExcelProperty(value = "用户id", index = 0)
    @ColumnWidth(20)
    private Long userId;

    @Schema(description = "第三方用户id")
    @ExcelProperty(value = "第三方用户id", index = 1)
    @ColumnWidth(20)
    private Long socialId;
}
