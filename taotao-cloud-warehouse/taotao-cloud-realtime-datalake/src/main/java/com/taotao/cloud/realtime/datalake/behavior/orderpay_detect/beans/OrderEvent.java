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
 * OrderEvent
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class OrderEvent {

    private Long orderId;
    private String eventType;
    private String txId;
    private Long timestamp;

    public OrderEvent() {
    }

    public OrderEvent( Long orderId, String eventType, String txId, Long timestamp ) {
        this.orderId = orderId;
        this.eventType = eventType;
        this.txId = txId;
        this.timestamp = timestamp;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId( Long orderId ) {
        this.orderId = orderId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType( String eventType ) {
        this.eventType = eventType;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId( String txId ) {
        this.txId = txId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp( Long timestamp ) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "OrderEvent{"
                + "orderId="
                + orderId
                + ", eventType='"
                + eventType
                + '\''
                + ", txId='"
                + txId
                + '\''
                + ", timestamp="
                + timestamp
                + '}';
    }
}
