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

package com.taotao.cloud.order.application.command.order.dto;

import com.taotao.boot.common.model.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 发票搜索参数
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "发票搜索参数")
public class ReceiptPageQry extends PageQuery {

    @Serial
    private static final long serialVersionUID = 8808470688518188146L;

    @Schema(description = "发票抬头")
    private String receiptTitle;

    @Schema(description = "纳税人识别号")
    private Long taxpayerId;

    @Schema(description = "会员ID")
    private Long memberId;

    @Schema(description = "会员名称")
    private String memberName;

    @Schema(description = "店铺名称")
    private String storeName;

    @Schema(description = "商家ID")
    private Long storeId;

    @Schema(description = "订单号")
    private String orderSn;

    @Schema(description = "发票状态")
    private String receiptStatus;

    // public <T> QueryWrapper<T> wrapper() {
    // 	QueryWrapper<T> queryWrapper = new QueryWrapper<>();
    // 	if (StrUtil.isNotEmpty(receiptTitle)) {
    // 		queryWrapper.like("r.receipt_title", receiptTitle);
    // 	}
    // 	if (Objects.nonNull(taxpayerId)) {
    // 		queryWrapper.like("r.taxpayer_id", taxpayerId);
    // 	}
    // 	if (Objects.nonNull(memberId)) {
    // 		queryWrapper.eq("r.member_id", memberId);
    // 	}
    // 	if (StrUtil.isNotEmpty(storeName)) {
    // 		queryWrapper.like("r.store_name", storeName);
    // 	}
    // 	if (Objects.nonNull(storeId)) {
    // 		queryWrapper.eq("r.store_id", storeId);
    // 	}
    // 	if (StrUtil.isNotEmpty(memberName)) {
    // 		queryWrapper.like("r.member_name", memberName);
    // 	}
    // 	if (StrUtil.isNotEmpty(receiptStatus)) {
    // 		queryWrapper.like("r.receipt_status", receiptStatus);
    // 	}
    // 	if (StrUtil.isNotEmpty(orderSn)) {
    // 		queryWrapper.like("r.order_sn", orderSn);
    // 	}
    // 	queryWrapper.eq("r.delete_flag", false);
    // 	return queryWrapper;
    // }

}
