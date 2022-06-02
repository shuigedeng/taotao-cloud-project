package com.taotao.cloud.sys.api.vo.setting.payment;

import com.taotao.cloud.common.enums.ClientTypeEnum;
import com.taotao.cloud.sys.api.vo.setting.payment.dto.PaymentSupportForm;
import com.taotao.cloud.sys.api.vo.setting.payment.dto.PaymentSupportItem;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 支持的支付方式
 *
 */
@Data
@Accessors(chain = true)
public class PaymentSupportSetting {

    private List<PaymentSupportItem> paymentSupportItems;


    public PaymentSupportSetting() {

    }

    public PaymentSupportSetting(PaymentSupportForm paymentSupportForm) {
        List<PaymentSupportItem> paymentSupportItems = new ArrayList<>();

        for (ClientTypeEnum client : paymentSupportForm.getClients()) {
            PaymentSupportItem paymentSupportItem = new PaymentSupportItem();

            List<String> supports = new ArrayList<>();
            // for (PaymentMethodEnum payment : paymentSupportForm.getPayments()) {
            //     supports.add(payment.name());
            // }
            paymentSupportItem.setClient(client.name());
            paymentSupportItem.setSupports(supports);
            paymentSupportItems.add(paymentSupportItem);

        }
        this.paymentSupportItems = paymentSupportItems;
    }
}
