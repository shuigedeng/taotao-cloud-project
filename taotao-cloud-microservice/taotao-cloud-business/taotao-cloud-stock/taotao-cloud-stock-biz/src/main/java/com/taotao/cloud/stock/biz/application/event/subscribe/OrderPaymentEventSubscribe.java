/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.stock.biz.application.event.subscribe;

import java.util.stream.Collectors;
import org.mallfoundry.finance.Payment;
import org.mallfoundry.finance.PaymentCapturedEvent;
import org.mallfoundry.finance.PaymentEvent;
import org.mallfoundry.finance.PaymentOrder;
import org.mallfoundry.finance.PaymentStartedEvent;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.event.EventListener;

@AutoConfiguration
public class OrderPaymentEventSubscribe {

    private final OrderService orderService;

    public OrderPaymentEventSubscribe(OrderService orderService) {
        this.orderService = orderService;
    }

    private OrderPaymentResult createPaymentResult(Payment payment) {
        //        var instrument = payment.getInstrument();
        //        return new DefaultOrderPaymentResult(payment.getId(), instrument.getType(),
        // payment.getStatus());
        return new DefaultOrderPaymentResult(payment.getId(), null, payment.getStatus());
    }

    @EventListener
    public void handlePending(PaymentStartedEvent event) {
        this.handlePaymentEvent(event);
    }

    @EventListener
    public void handleCaptured(PaymentCapturedEvent event) {
        this.handlePaymentEvent(event);
    }

    private void handlePaymentEvent(PaymentEvent event) {
        var payment = event.getPayment();
        var orderIds = payment.getOrders().stream().map(PaymentOrder::getId).collect(Collectors.toUnmodifiableSet());
        var paymentResult = this.createPaymentResult(payment);
        this.orderService.payOrders(orderIds, paymentResult);
    }
}
