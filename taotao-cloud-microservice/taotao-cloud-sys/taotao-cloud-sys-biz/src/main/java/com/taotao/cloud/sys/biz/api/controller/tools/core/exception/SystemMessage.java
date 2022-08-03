package com.taotao.cloud.sys.biz.api.controller.tools.core.exception;


import com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.ResponseDto;

import java.text.MessageFormat;

public enum  SystemMessage implements ExceptionCause<BusinessException> {
    OK(0,"成功"),
    ARGS_NULL(4000,"参数错误,必填参数 [{0}]"),
    ARGS_ERROR(4001,"参数错误, 参数:{0}={1}; 额外消息:{2}"),
    ARGS_ERROR2(4001,"参数错误,原因:{0}"),
    NOT_LOGIN(4002,"未登录或 session 失效"),
    PERMISSION_DENIED(4003,"没有权限"),
    DATA_PERMISSION_DENIED(4004,"无数据权限"),
    SIGN_ERROR(4005,"签名错误,你的签名串为 [{0}]"),
    CHANNEL_NOT_SUPPORT(4006,"非法访问"),
    ACCESS_DENIED(4007,"禁止访问"),
    ACCESS_DENIED_ARGS(4007,"禁止访问,{0}"),
    LOGIN_FAIL(4009,"登录失败 {0}"),
    DELETE_TOKEN(4010,"本次访问无权限,请重新登录"),

    SERVICE_CALL_FAIL(5000,"后台服务异常"),
    NETWORK_ERROR(5001,"网络错误"),
    DATA_TO_LARGE(5002,"数据过大"),
    REPEAT_DATA(5003,"数据重复 {0}"),
    NOT_SUPPORT_OPERATOR(5004,"不支持的操作"),
    NOT_SUPPORT_MIME(5005,"不支持的 MIME类型,当前类型为:{0}"),
    POOL_OBJECT_NOT_ENOUGH(5006,"对象池[{0}]对象不足"),
    CALL_MODUL_FAIL(5007,"{0} 模块调用错误"),
    FILE_NOT_FOUND(5008,"文件读写错误"),
    CONNECT_ERROR(5009,"连接失败"),
    SERVICE_ERROR(5010,"服务不可达"),

    FILE_INCOMPLETE(5901,"文件不完整，上传失败"),
    FILE_CORRUPTED(5902,"文件损坏，上传失败"),
    FILE_TOO_LARGE(5903,"文件过大，当前文件大小:{0}，超过最大上传大小:{1}"),
    FILE_TYPE_NOT_ALLOW(5904,"文件类型不被支持，当前文件类型:{0},支持的文件类型为:{1}"),
    ;
    ResponseDto responseDto = new ResponseDto();
    public MessageFormat messageFormat;

    private SystemMessage(int returnCode,String message){
        responseDto.setCode(returnCode+"");
        responseDto.setMessage(message);
        messageFormat = new MessageFormat(message);
    }

    @Override
    public BusinessException exception(Object...args) {
        return BusinessException.create(this,args);
    }

    @Override
    public MessageFormat getMessageFormat() {
        return messageFormat;
    }

    @Override
    public ResponseDto result() {
        return responseDto;
    }

    /**
     * 自定义消息的结果返回
     * @param args
     * @return
     */
    public ResponseDto result(Object ... args){
        String message = responseDto.getMessage();
        responseDto.setMessage(MessageFormat.format(message,args));
        return responseDto;
    }

    public String getReturnCode(){
        return responseDto.getCode();
    }
}
