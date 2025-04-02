package com.taotao.cloud.job.server.consumer.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Response {
    public ResponseEnum getRes() {
        return res;
    }

    public void setRes(ResponseEnum res) {
        this.res = res;
    }

    ResponseEnum res;

}
