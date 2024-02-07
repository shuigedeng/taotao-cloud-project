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

package com.taotao.cloud.sys.adapter.model.co.region;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** 地区VO */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "地区VO")
public class RegionVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 5126530068827085130L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "地区父节点")
    private Long parentId;

    @Schema(description = "地区编码")
    private String code;

    @Schema(description = "地区名称")
    private String name;

    /**
     * 地区级别（1:省份province,2:市city,3:区县district,4:街道street） "行政区划级别" + "country:国家" +
     * "province:省份（直辖市会在province和city显示）" + "city:市（直辖市会在province和city显示）" + "district:区县" +
     * "street:街道"
     */
    @Schema(description = "地区级别")
    private String level;

    @Schema(description = "城市编码")
    private String cityCode;

    @Schema(description = "城市中心经度")
    private String lng;

    @Schema(description = "城市中心纬度")
    private String lat;

    @Schema(description = "行政地区路径,类似：1，2，3")
    private String path;

    @Schema(description = "排序")
    private Integer orderNum;

    @Schema(description = "子信息")
    private List<RegionVO> children;
}
