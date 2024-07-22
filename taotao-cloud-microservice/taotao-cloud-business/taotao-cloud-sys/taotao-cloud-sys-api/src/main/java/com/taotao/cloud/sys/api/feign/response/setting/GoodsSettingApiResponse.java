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

package com.taotao.cloud.sys.api.feign.response.setting;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** 商品配置 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "商品配置VO")
public class GoodsSettingApiResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -4132785717179910025L;

    /** 是否开启商品审核 */
    private Boolean goodsCheck;
    /** 小图宽 */
    private Integer smallPictureWidth;
    /** 小图高 */
    private Integer smallPictureHeight;
    /** 缩略图宽 */
    private Integer abbreviationPictureWidth;
    /** 缩略图高 */
    private Integer abbreviationPictureHeight;
    /** 原图宽 */
    private Integer originalPictureWidth;
    /** 原图高 */
    private Integer originalPictureHeight;
}
