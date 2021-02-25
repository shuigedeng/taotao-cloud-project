package com.taotao.cloud.portal.util;

import lombok.Data;

/**
 * @author xiaoming
 * @since 2020/3/9
 * @blame xiaoming
 */
@Data
public class ResponseBase {

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 返回数据
     */
    private Object data;

    public ResponseBase(){}

    public ResponseBase(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
