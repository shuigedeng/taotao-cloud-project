package com.taotao.cloud.order.api.web.query.aftersale;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.common.enums.UserEnum;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.order.api.enums.trade.AfterSaleStatusEnum;
import com.taotao.cloud.order.api.enums.trade.AfterSaleTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 售后搜索参数
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "售后搜索参数")
public class AfterSalePageQuery extends PageParam {

	@Serial
	private static final long serialVersionUID = 8808470688518188146L;

	@Schema(description = "售后服务单号")
	private String sn;

	@Schema(description = "订单编号")
	private String orderSn;

	@Schema(description = "会员名称")
	private String memberName;

	@Schema(description = "商家名称")
	private String storeName;

	@Schema(description = "商家ID")
	private Long storeId;

	@Schema(description = "商品名称")
	private String goodsName;

	@Schema(description = "申请退款金额,可以为范围，如10_1000")
	private String applyRefundPrice;

	@Schema(description = "实际退款金额,可以为范围，如10_1000")
	private String actualRefundPrice;

	/**
	 * @see AfterSaleTypeEnum
	 */
	@Schema(description = "售后类型", allowableValues = "CANCEL,RETURN_GOODS,EXCHANGE_GOODS,REISSUE_GOODS")
	private String serviceType;

	/**
	 * @see AfterSaleStatusEnum
	 */
	@Schema(description = "售后单状态", allowableValues = "APPLY,PASS,REFUSE,BUYER_RETURN,SELLER_RE_DELIVERY,BUYER_CONFIRM,SELLER_CONFIRM,COMPLETE")
	private String serviceStatus;

	@Schema(description = "开始时间 yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startDate;

	@Schema(description = "结束时间 yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endDate;

	public <T> QueryWrapper<T> queryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (StringUtil.isNotEmpty(sn)) {
			queryWrapper.like("sn", sn);
		}
		if (StringUtil.isNotEmpty(orderSn)) {
			queryWrapper.like("order_sn", orderSn);
		}

		//按买家查询
		if (SecurityUtil.getCurrentUser().getType() == UserEnum.MEMBER.getCode()) {
			queryWrapper.eq("member_id", SecurityUtil.getCurrentUser().getUserId());
		}

		//按卖家查询
		if (SecurityUtil.getCurrentUser().getType() == UserEnum.STORE.getCode()) {
			queryWrapper.eq("store_id", SecurityUtil.getCurrentUser().getStoreId());
		}

		if (SecurityUtil.getCurrentUser().getType() == UserEnum.MANAGER.getCode() && StringUtil.isNotEmpty(
			storeId)) {
			queryWrapper.eq("store_id", storeId);
		}
		if (StringUtil.isNotEmpty(memberName)) {
			queryWrapper.like("member_name", memberName);
		}
		if (StringUtil.isNotEmpty(storeName)) {
			queryWrapper.like("store_name", storeName);
		}
		if (StringUtil.isNotEmpty(goodsName)) {
			queryWrapper.like("goods_name", goodsName);
		}
		//按时间查询
		if (startDate != null) {
			queryWrapper.ge("create_time", startDate);
		}
		if (endDate != null) {
			queryWrapper.le("create_time", endDate);
		}
		if (StringUtil.isNotEmpty(serviceStatus)) {
			queryWrapper.eq("service_status", serviceStatus);
		}
		if (StringUtil.isNotEmpty(serviceType)) {
			queryWrapper.eq("service_type", serviceType);
		}
		this.betweenWrapper(queryWrapper);
		queryWrapper.eq("delete_flag", false);
		return queryWrapper;
	}

	private <T> void betweenWrapper(QueryWrapper<T> queryWrapper) {
		if (StringUtil.isNotEmpty(applyRefundPrice)) {
			String[] s = applyRefundPrice.split("_");
			if (s.length > 1) {
				queryWrapper.ge("apply_refund_price", s[1]);
			} else {
				queryWrapper.le("apply_refund_price", s[0]);
			}
		}
		if (StringUtil.isNotEmpty(actualRefundPrice)) {
			String[] s = actualRefundPrice.split("_");
			if (s.length > 1) {
				queryWrapper.ge("actual_refund_price", s[1]);
			} else {
				queryWrapper.le("actual_refund_price", s[0]);
			}
		}
	}


}
