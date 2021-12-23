package com.taotao.cloud.order.api.vo.cart;

import cn.lili.common.enums.ClientTypeEnum;
import cn.lili.modules.order.cart.entity.dto.StoreRemarkDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;

/**
 * 交易参数
 *
 * @since 2021/2/23
 **/
@Schema(description = "交易参数")
public class TradeParams implements Serializable {

	private static final long serialVersionUID = -8383072817737513063L;

	@Schema(description = "购物车购买：CART/立即购买：BUY_NOW/拼团购买：PINTUAN / 积分购买：POINT")
	private String way;

	/**
	 * @see ClientTypeEnum
	 */
	@Schema(description = "客户端：H5/移动端 PC/PC端,WECHAT_MP/小程序端,APP/移动应用端")
	private String client;

	@Schema(description = "店铺备注")
	private List<StoreRemarkDTO> remark;

	@Schema(description = "是否为其他订单下的订单，如果是则为依赖订单的sn，否则为空")
	private String parentOrderSn;


}
