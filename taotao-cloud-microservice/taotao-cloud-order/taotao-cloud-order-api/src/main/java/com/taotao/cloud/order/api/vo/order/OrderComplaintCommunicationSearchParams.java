package com.taotao.cloud.order.api.vo.order;

import cn.hutool.core.util.StrUtil;
import cn.lili.modules.order.order.entity.dos.OrderComplaintCommunication;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 订单投诉搜索参数
 *
 * @since 2020/12/5
 **/
@Schema(description = "订单投诉搜索参数")
public class OrderComplaintCommunicationSearchParams {

	/**
	 * 投诉id
	 */
	@Schema(description = "投诉id")
	private String complainId;

	/**
	 * 所属，买家/卖家
	 */
	@Schema(description = "所属，买家/卖家")
	private String owner;
	/**
	 * 对话所属名称
	 */
	@Schema(description = "对话所属名称")
	private String ownerName;
	/**
	 * 对话所属id,卖家id/买家id
	 */
	@Schema(description = "对话所属id,卖家id/买家id")
	private String ownerId;

	public LambdaQueryWrapper<OrderComplaintCommunication> lambdaQueryWrapper() {
		LambdaQueryWrapper<OrderComplaintCommunication> queryWrapper = new LambdaQueryWrapper<>();
		if (StrUtil.isNotEmpty(complainId)) {
			queryWrapper.eq(OrderComplaintCommunication::getComplainId, complainId);
		}
		if (StrUtil.isNotEmpty(owner)) {
			queryWrapper.eq(OrderComplaintCommunication::getOwner, owner);
		}
		if (StrUtil.isNotEmpty(ownerName)) {
			queryWrapper.eq(OrderComplaintCommunication::getOwnerName, ownerName);
		}
		if (StrUtil.isNotEmpty(ownerId)) {
			queryWrapper.eq(OrderComplaintCommunication::getOwnerId, ownerId);
		}
		return queryWrapper;
	}

}
