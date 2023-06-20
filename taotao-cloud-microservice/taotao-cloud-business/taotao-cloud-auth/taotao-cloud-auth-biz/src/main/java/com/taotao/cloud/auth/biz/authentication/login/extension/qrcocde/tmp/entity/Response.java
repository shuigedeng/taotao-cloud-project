package com.taotao.cloud.auth.biz.authentication.login.extension.qrcocde.tmp.entity;

import lombok.Data;

@Data
public class Response {

    private String code;

    private String msg;

    private Object data;

    public Response(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Response createResponse(String msg, Object data) {
        return new Response("200", msg, data);
    }

    public static Response createErrorResponse(String msg) {
        return new Response("500", msg, null);
    }
}
