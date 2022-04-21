package com.taotao.cloud.order.api.query.order;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.order.api.enums.aftersale.ComplaintStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 订单投诉查询参数
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "订单投诉查询参数")
public class OrderComplaintPageQuery extends PageParam {

	/**
	 * @see ComplaintStatusEnum
	 */
	@Schema(description = "交易投诉状态")
	private String status;

	@Schema(description = "订单号")
	private String orderSn;

	@Schema(description = "会员id")
	private Long memberId;

	@Schema(description = "会员名称")
	private String memberName;

	@Schema(description = "商家id")
	private Long storeId;

	@Schema(description = "商家名称")
	private String storeName;
}
