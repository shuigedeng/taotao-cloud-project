package com.taotao.cloud.order.api.vo.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taotao.cloud.common.enums.ClientTypeEnum;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.order.api.enums.order.CommentStatusEnum;
import com.taotao.cloud.order.api.enums.order.DeliverStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderComplaintStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderItemAfterSaleStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderTypeEnum;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单简略信息 用于订单列表查看
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "订单简略信息 用于订单列表查看")
public class OrderSimpleVO implements Serializable {

	@Serial
	private static final long serialVersionUID = -6293102172184734928L;

	@Schema(description = "sn")
	private String sn;

	@Schema(description = "总价格")
	private BigDecimal flowPrice;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	/**
	 * @see OrderStatusEnum
	 */
	@Schema(description = "订单状态")
	private String orderStatus;

	/**
	 * @see PayStatusEnum
	 */
	@Schema(description = "付款状态")
	private String payStatus;

	@Schema(description = "支付方式")
	private String paymentMethod;

	@Schema(description = "支付时间")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime paymentTime;

	@Schema(description = "用户名")
	private String memberName;

	@Schema(description = "店铺名称")
	private String storeName;

	@Schema(description = "店铺ID")
	private Long storeId;

	/**
	 * @see ClientTypeEnum
	 */
	@Schema(description = "订单来源")
	private String clientType;

	/**
	 * 子订单信息
	 */
	private List<OrderItemVO> orderItems;

	@Schema(hidden = true, description = "item goods_id")
	private String groupGoodsId;

	@Schema(hidden = true, description = "item sku id")
	private String groupSkuId;

	@Schema(hidden = true, description = "item 数量")
	private String groupNum;

	@Schema(hidden = true, description = "item 图片")
	private String groupImages;

	@Schema(hidden = true, description = "item 名字")
	private String groupName;

	@Schema(hidden = true, description = "item 编号")
	private String groupOrderItemsSn;

	@Schema(hidden = true, description = "item 商品价格")
	private String groupGoodsPrice;

	/**
	 * @see OrderItemAfterSaleStatusEnum
	 */
	@Schema(hidden = true, description = "item 售后状态", allowableValues = "NOT_APPLIED(未申请),ALREADY_APPLIED(已申请),EXPIRED(已失效不允许申请售后)")
	private String groupAfterSaleStatus;

	/**
	 * @see OrderComplaintStatusEnum
	 */
	@Schema(hidden = true, description = "item 投诉状态")
	private String groupComplainStatus;

	/**
	 * @see CommentStatusEnum
	 */
	@Schema(hidden = true, description = "item 评价状态")
	private String groupCommentStatus;

	/**
	 * @see OrderTypeEnum
	 */
	@Schema(description = "订单类型")
	private String orderType;

	/**
	 * @see DeliverStatusEnum
	 */
	@Schema(description = "货运状态")
	private String deliverStatus;

	public List<OrderItemVO> getOrderItems() {
		if (StringUtil.isEmpty(groupGoodsId)) {
			return new ArrayList<>();
		}
		List<OrderItemVO> orderItemVOS = new ArrayList<>();
		String[] orderItemsSn = groupOrderItemsSn.split(",");
		String[] goodsId = groupGoodsId.split(",");
		String[] skuId = groupSkuId.split(",");
		String[] num = groupNum.split(",");
		String[] image = groupImages.split(",");
		String[] name = groupName.split(",");
		String[] afterSaleStatus = groupAfterSaleStatus.split(",");
		String[] complainStatus = groupComplainStatus.split(",");
		String[] commentStatus = groupCommentStatus.split(",");
		String[] goodsPrice = groupGoodsPrice.split(",");

		for (int i = 0; i < goodsId.length; i++) {
			orderItemVOS.add(
				new OrderItemVO(orderItemsSn[i], goodsId[i], skuId[i], num[i], image[i], name[i],
					afterSaleStatus[i], complainStatus[i], commentStatus[i],
					BigDecimal.parseBigDecimal()));
		}
		return orderItemVOS;

	}

	/**
	 * 初始化自身状态
	 */
	public AllowOperation getAllowOperationVO() {
		//设置订单的可操作状态
		return new AllowOperation(this);
	}


}
