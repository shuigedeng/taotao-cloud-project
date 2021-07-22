package com.atguigu.orderpay_detect.beans;/**
 * Copyright (c) 2018-2028 尚硅谷 All Rights Reserved
 * <p>
 * Project: UserBehaviorAnalysis
 * Package: com.atguigu.orderpay_detect.beans
 * Version: 1.0
 * <p>
 * Created by wushengran on 2020/11/18 10:40
 */

/**
 * @ClassName: OrderResult
 * @Description:
 * @Author: wushengran on 2020/11/18 10:40
 * @Version: 1.0
 */
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
