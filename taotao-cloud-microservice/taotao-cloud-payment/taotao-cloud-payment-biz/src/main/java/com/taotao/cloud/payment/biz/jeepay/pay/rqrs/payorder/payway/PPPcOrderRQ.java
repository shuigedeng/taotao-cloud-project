package com.taotao.cloud.payment.biz.jeepay.pay.rqrs.payorder.payway;

import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.payorder.CommonPayDataRQ;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * none.
 *
 * @author 陈泉
 * @package com.jeequan.jeepay.pay.rqrs.payorder.payway
 * @create 2021/11/15 17:52
 */
@Data
public class PPPcOrderRQ extends CommonPayDataRQ {

	/**
	 * 商品描述信息
	 **/
	@NotBlank(message = "取消支付返回站点")
	private String cancelUrl;

	public PPPcOrderRQ() {
		this.setWayCode(CS.PAY_WAY_CODE.PP_PC);
	}
}
