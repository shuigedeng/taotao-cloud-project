package com.taotao.cloud.order.api.vo.cart;

import com.taotao.cloud.common.enums.ClientTypeEnum;
import com.taotao.cloud.order.api.dto.cart.StoreRemarkDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 交易参数
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "交易参数")
public class TradeParams implements Serializable {

	@Serial
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
