package com.taotao.cloud.java.javaweb.p12_myshop.entity;

public class WeiXin {

    private Result result;
    private String type;
    public Result getResult() {
        return result;
    }
    public void setResult(Result result) {
        this.result = result;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public WeiXin(Result result, String type) {
        super();
        this.result = result;
        this.type = type;
    }
    public WeiXin() {
        super();
    }
}
