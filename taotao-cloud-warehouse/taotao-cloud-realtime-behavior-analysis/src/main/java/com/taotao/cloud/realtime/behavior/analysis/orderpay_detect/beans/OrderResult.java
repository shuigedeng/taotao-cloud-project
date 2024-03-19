package com.taotao.cloud.realtime.behavior.analysis.orderpay_detect.beans;

public class OrderResult {
    private Long orderId;
    private String resultState;

    public OrderResult() {
    }

    public OrderResult(Long orderId, String resultState) {
        this.orderId = orderId;
        this.resultState = resultState;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getResultState() {
        return resultState;
    }

    public void setResultState(String resultState) {
        this.resultState = resultState;
    }

    @Override
    public String toString() {
        return "OrderResult{" +
                "orderId=" + orderId +
                ", resultState='" + resultState + '\'' +
                '}';
    }
}
