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
import com.taotao.cloud.member.api.enums.MemberReceiptEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 会员发票查询DTO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:38:47
 */
@Getter
@Setter
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "会员发票查询DTO")
public class MemberReceiptPageQuery extends PageQuery {

    @Serial
    private static final long serialVersionUID = -8210927982915677995L;

    @Schema(description = "会员ID")
    private String memberId;

    @Schema(description = "会员名称")
    private String memberName;

    /**
     * @see MemberReceiptEnum
     */
    @Schema(description = "发票类型")
    private String receiptType;

    // public LambdaQueryWrapper<MemberReceipt> lambdaQueryWrapper() {
    //	LambdaQueryWrapper<MemberReceipt> queryWrapper = new LambdaQueryWrapper<>();
    //
    //	//会员名称查询
    //	if (StringUtil.isNotEmpty(memberName)) {
    //		queryWrapper.like(MemberReceipt::getMemberName, memberName);
    //	}
    //	//会员id查询
    //	if (StringUtil.isNotEmpty(memberId)) {
    //		queryWrapper.eq(MemberReceipt::getMemberId, memberId);
    //	}
    //	//会员id查询
    //	if (StringUtil.isNotEmpty(receiptType)) {
    //		queryWrapper.eq(MemberReceipt::getReceiptType, receiptType);
    //	}
    //	queryWrapper.eq(MemberReceipt::getDeleteFlag, true);
    //	queryWrapper.orderByDesc(MemberReceipt::getCreateTime);
    //	return queryWrapper;
    // }
}
