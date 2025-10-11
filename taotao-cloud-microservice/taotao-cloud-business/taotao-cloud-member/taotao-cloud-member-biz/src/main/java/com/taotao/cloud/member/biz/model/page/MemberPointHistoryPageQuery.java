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

import com.taotao.boot.common.model.request.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.*;
import lombok.Data;
import lombok.experimental.*;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 会员query
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/9/30 08:49
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "MemberPointHistoryPageQuery", description = "会员query")
public class MemberPointHistoryPageQuery extends PageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = -7605952923416404638L;

    private Long memberId;

    private String memberName;
}
