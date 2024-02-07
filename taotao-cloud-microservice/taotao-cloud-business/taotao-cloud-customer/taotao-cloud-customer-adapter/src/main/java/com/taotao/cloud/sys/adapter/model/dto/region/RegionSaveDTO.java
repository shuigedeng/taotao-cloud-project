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

package com.taotao.cloud.sys.adapter.model.dto.region;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.*;

/**
 * 地区添加对象
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/9/30 08:49
 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "地区添加对象")
public class RegionSaveDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4132785717179910025L;

    @Schema(description = "地区编码")
    private String code;

    @Schema(description = "地区名称")
    private String name;

    @Schema(description = "地区级别（1:省份province;2:市city;3:区县district;4:街道street）")
    private Integer level;

    @Schema(description = "城市编码")
    private String cityCode;

    @Schema(description = "城市中心经度")
    private String lng;

    @Schema(description = "城市中心纬度")
    private String lat;

    @Schema(description = "地区父节点")
    private Long parentId;
}
