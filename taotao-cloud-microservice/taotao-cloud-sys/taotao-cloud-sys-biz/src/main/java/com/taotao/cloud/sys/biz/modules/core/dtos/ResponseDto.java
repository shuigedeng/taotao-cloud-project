package com.taotao.cloud.sys.biz.modules.core.dtos;

import lombok.Data;

import java.io.Serializable;

/**
 * 普通消息返回
 * @param <T>
 */
@Data
public class ResponseDto<T> implements Serializable {
    private String code = "0";
    private String message;
    private T data;

    public ResponseDto() {
        this.message = "ok";
    }

    public ResponseDto(T data) {
        this();
        this.data = data;
    }

    public static ResponseDto ok() {
        return new ResponseDto();
    }

    public static ResponseDto warn(String message){
        ResponseDto responseDto = new ResponseDto();
        responseDto.setCode("1");
        responseDto.setMessage(message);
        return responseDto;
    }

    public static ResponseDto info(String message){
        ResponseDto responseDto = new ResponseDto();
        responseDto.setCode("2");
        responseDto.setMessage(message);
        return responseDto;
    }

    public static ResponseDto err(String returnCode) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.code = returnCode;
        responseDto.message = "fail";
        return responseDto;
    }

    public static ResponseDto err() {
        return err("-1");
    }

    public ResponseDto message(String msg) {
        this.message = msg;
        return this;
    }

    public ResponseDto data(T data) {
        this.data = data;
        return this;
    }

}
