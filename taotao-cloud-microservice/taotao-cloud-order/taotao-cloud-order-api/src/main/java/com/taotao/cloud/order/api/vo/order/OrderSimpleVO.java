package com.taotao.cloud.order.api.vo.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taotao.cloud.common.enums.ClientTypeEnum;
import com.taotao.cloud.order.api.enums.order.CommentStatusEnum;
import com.taotao.cloud.order.api.enums.order.DeliverStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderComplaintStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderItemAfterSaleStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderTypeEnum;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 订单简略信息 用于订单列表查看
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "订单简略信息 用于订单列表查看")
public class OrderSimpleVO {

	@ApiModelProperty("sn")
	private String sn;

	@Schema(description = "总价格")
	private BigDecimal flowPrice;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(description = "创建时间")
	private Date createTime;

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
	private Date paymentTime;

	@Schema(description = "用户名")
	private String memberName;

	@Schema(description = "店铺名称")
	private String storeName;

	@Schema(description = "店铺ID")
	private String storeId;

	/**
	 * @see ClientTypeEnum
	 */
	@Schema(description = "订单来源")
	private String clientType;

	/**
	 * 子订单信息
	 */
	private List<OrderItemVO> orderItems;

	@ApiModelProperty(hidden = true, value = "item goods_id")
	@Setter
	private String groupGoodsId;

	@ApiModelProperty(hidden = true, value = "item sku id")
	@Setter
	private String groupSkuId;

	@ApiModelProperty(hidden = true, value = "item 数量")
	@Setter
	private String groupNum;

	@ApiModelProperty(hidden = true, value = "item 图片")
	@Setter
	private String groupImages;

	@ApiModelProperty(hidden = true, value = "item 名字")
	@Setter
	private String groupName;

	@ApiModelProperty(hidden = true, value = "item 编号")
	@Setter
	private String groupOrderItemsSn;

	@ApiModelProperty(hidden = true, value = "item 商品价格")
	@Setter
	private String groupGoodsPrice;
	/**
	 * @see OrderItemAfterSaleStatusEnum
	 */
	@ApiModelProperty(hidden = true, value = "item 售后状态", allowableValues = "NOT_APPLIED(未申请),ALREADY_APPLIED(已申请),EXPIRED(已失效不允许申请售后)")
	@Setter
	private String groupAfterSaleStatus;

	/**
	 * @see OrderComplaintStatusEnum
	 */
	@ApiModelProperty(hidden = true, value = "item 投诉状态")
	@Setter
	private String groupComplainStatus;

	/**
	 * @see CommentStatusEnum
	 */
	@ApiModelProperty(hidden = true, value = "item 评价状态")
	@Setter
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
		if (StringUtils.isEmpty(groupGoodsId)) {
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
					BigDecimal.parseBigDecimal(goodsPrice[i])));
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
