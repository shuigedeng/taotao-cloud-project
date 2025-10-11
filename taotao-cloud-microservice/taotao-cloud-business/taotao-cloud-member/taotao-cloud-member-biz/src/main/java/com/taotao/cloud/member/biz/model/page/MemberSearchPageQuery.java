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

package com.taotao.cloud.member.biz.model.page;

import com.taotao.boot.common.enums.SwitchEnum;
import com.taotao.boot.common.model.request.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 会员搜索DTO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:36:30
 */
@Setter
@Getter
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "会员搜索DTO")
public class MemberSearchPageQuery extends PageQuery {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "用户手机号码")
    private String mobile;

    /**
     * @see SwitchEnum
     */
    @Schema(description = "会员状态")
    private String disabled;
}
