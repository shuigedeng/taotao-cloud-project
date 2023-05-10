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

package com.taotao.cloud.promotion.api.model.vo;

import com.taotao.cloud.promotion.api.model.vo.KanjiaActivityBaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/** 砍价活动参与实体类 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@Schema(description = "砍价活动VO")
public class KanjiaActivityVO extends KanjiaActivityBaseVO {

    @Schema(description = "是否可以砍价")
    private Boolean help;

    @Schema(description = "是否已发起砍价")
    private Boolean launch;

    @Schema(description = "是否可购买")
    private Boolean pass;

    public KanjiaActivityVO() {
        this.setHelp(false);
        this.setLaunch(false);
        this.setPass(false);
    }
}
