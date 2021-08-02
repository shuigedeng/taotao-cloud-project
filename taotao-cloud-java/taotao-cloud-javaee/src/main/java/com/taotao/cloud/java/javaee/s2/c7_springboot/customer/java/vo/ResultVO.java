package com.taotao.cloud.java.javaee.s2.c7_springboot.customer.java.vo;

public class ResultVO {

    private Boolean status;  // true

    private String message;

    private Object result;

    public ResultVO(Boolean status, String message) {
        this.status = status;
        this.message = message;
    }
}
