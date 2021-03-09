package com.taotao.cloud.java.javaee.s2.c5_redis.web.java.bean;

/**
 * 前端页面用到的所有ajax返回结果统一封装
 */
public class AjaxMessage {
    private boolean status;
    private String message;
    private Object result;

    public AjaxMessage() {
    }

    public AjaxMessage(boolean status) {
        this.status = status;
    }

    public AjaxMessage(Boolean status, String message, Object result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }

    public AjaxMessage(Boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public boolean getStatus() {
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

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
