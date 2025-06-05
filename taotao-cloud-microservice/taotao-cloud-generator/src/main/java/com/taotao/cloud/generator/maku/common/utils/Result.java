package com.taotao.cloud.generator.maku.common.utils;

import lombok.Data;
import com.taotao.cloud.generator.maku.common.exception.ErrorCode;

/**
 * 响应数据
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
public class Result<T> {
    // 编码 0表示成功，其他值表示失败
    private int code = 0;
    // 消息内容
    private String msg = "success";
    // 响应数据
    private T data;

    public static <T> Result<T> ok() {
        return ok(null);
    }

    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error() {
        return error(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    public static <T> Result<T> error(String msg) {
        return error(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), msg);
    }

    public static <T> Result<T> error(ErrorCode errorCode) {
        return error(errorCode.getCode(), errorCode.getMsg());
    }

    public static <T> Result<T> error(int code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
