package com.taotao.cloud.job.server.jobserver.consumer.entity;

public enum ResponseEnum {
    SUCCESS("success"),
    FLUSH_ERROR("flush to disk error");

    private final String v;


    public String getV() {
        return v;
    }

    ResponseEnum(String v) {
        this.v = v;
    }
}
