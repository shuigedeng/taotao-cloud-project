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

package com.taotao.cloud.realtime.datalake.behavior.orderpay_detect.beans;

/**
 * OrderResult
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class OrderResult {

    private Long orderId;
    private String resultState;

    public OrderResult() {
    }

    public OrderResult( Long orderId, String resultState ) {
        this.orderId = orderId;
        this.resultState = resultState;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId( Long orderId ) {
        this.orderId = orderId;
    }

    public String getResultState() {
        return resultState;
    }

    public void setResultState( String resultState ) {
        this.resultState = resultState;
    }

    @Override
    public String toString() {
        return "OrderResult{" + "orderId=" + orderId + ", resultState='" + resultState + '\'' + '}';
    }
}
