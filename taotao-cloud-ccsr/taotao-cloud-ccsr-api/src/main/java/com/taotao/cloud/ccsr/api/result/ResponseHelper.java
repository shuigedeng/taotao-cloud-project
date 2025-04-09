package com.taotao.cloud.ccsr.api.result;

import com.google.protobuf.Any;
import com.google.protobuf.Empty;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import org.ohara.msc.common.enums.ResponseCode;

public class ResponseHelper {

    public static Response error(int code, String msg, Any data) {
        return Response.newBuilder()
                .setSuccess(false)
                .setData(data)
                .setCode(code)
                .setMsg(msg)
                .build();
    }

    public static Response error(int code, String msg) {
        return error(code, msg, Any.pack(Empty.newBuilder().build()));
    }

    public static Response success(String msg, Any data) {
        return Response.newBuilder()
                .setSuccess(true)
                .setData(data)
                .setCode(ResponseCode.SUCCESS.getCode())
                .setMsg(msg)
                .build();
    }

    public static Response success(Any data) {
        return success(ResponseCode.SUCCESS.getMsg(), data);
    }

    public static Response success(String msg) {
        return success(msg, Any.pack(Empty.newBuilder().build()));
    }

    public static Response success() {
        return success(ResponseCode.SUCCESS.getMsg(), Any.pack(Empty.newBuilder().build()));
    }

}
