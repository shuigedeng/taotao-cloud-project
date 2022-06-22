package com.taotao.cloud.sys.biz.model.entity.config.payment;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支持的支付方式
 */
@Data
@Accessors(chain = true)
public class PaymentSupportSetting {

	private List<PaymentSupportItem> paymentSupportItems;


	public PaymentSupportSetting() {

	}

	//public PaymentSupportSetting(PaymentSupportForm paymentSupportForm) {
	//
	//	List<PaymentSupportItem> paymentSupportItems = new ArrayList<>();
	//
	//	for (ClientTypeEnum client : paymentSupportForm.getClients()) {
	//		PaymentSupportItem paymentSupportItem = new PaymentSupportItem();
	//
	//		List<String> supports = new ArrayList<>();
	//		for (PaymentMethodEnum payment : paymentSupportForm.getPayments()) {
	//			supports.add(payment.name());
	//		}
	//		paymentSupportItem.setClient(client.name());
	//		paymentSupportItem.setSupports(supports);
	//		paymentSupportItems.add(paymentSupportItem);
	//
	//	}
	//	this.paymentSupportItems = paymentSupportItems;
	//}
}
