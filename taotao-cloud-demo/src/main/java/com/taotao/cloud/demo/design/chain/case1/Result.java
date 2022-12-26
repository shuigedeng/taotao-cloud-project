package com.taotao.cloud.demo.design.chain.case1;

import lombok.Data;

@Data
public class Result<T> {
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 响应码
     */
    private String code;
    /**
     * 消息
     */
    private String msg;
    /**
     * 数据
     */
    private T data;

    /**
     * success
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> success() {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(ErrorCode.SUCCESS.getCode());
        return r;
    }

    /**
     * success
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(ErrorCode.SUCCESS.getCode());
        r.setData(data);
        return r;
    }

    /**
     * failure
     *
     * @param code
     * @param <T>
     * @return
     */
    public static <T> Result<T> failure(ErrorCode code) {
        return failure(code.getCode(), code.getDesc());
    }

    /**
     * failure
     *
     * @param code
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> failure(String code, String message) {
        return failure(code, message, null);
    }

    /**
     * failure
     *
     * @param code
     * @param msg
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> failure(String code, String msg, T data) {
        Result<T> r = new Result<T>();
        r.setSuccess(false);
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

}
