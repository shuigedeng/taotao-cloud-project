package com.taotao.cloud.java.javaee.s1.c10_ssm.java.bean;

public class BaseResultBean {


    private boolean status;

    private String message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
