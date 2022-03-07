package com.taotao.cloud.core.heaven.response.respcode.impl;


import com.taotao.cloud.core.heaven.response.respcode.RespCode;

/**
 * 通用响应编码
 */
public enum CommonRespCode implements RespCode {
    /**
     * 系统级别通用返回码
     */
    SUCCESS("00000", "成功"),
    FAIL("00001", "失败"),
    TIME_OUT("00002", "超时"),

    /**
     * 参数校验
     */
    CEHCK_PARAM_FAIL("10001", "参数校验失败"),
    CEHCK_CHECKSUM_FAIL("10002", "签名校验失败"),

    /**
     * 数据库相关
     */
    DATABASE_INSERT_FAIL("20001", "数据库插入失败"),
    DATABASE_UPDATE_FAIL("20002", "数据库更新失败"),
    DATABASE_DELETE_FAIL("20003", "数据库删除失败"),
    DATABASE_SELECT_FAIL("20004", "数据库查询失败"),

    /**
     * 文件/流/编码 相关异常
     */
    FILE_NOT_EXISTS("30001", "指定文件不存在"),

    //流相关
    STREAM_HAS_BEEN_CLOSED("31001", "流已经关闭"),

    //编码相关
    CHARSET_NOT_EXISTS("32001", "指定编码不存在"),

    /**
     * 反射相关
     */
    REFLECT_NEW_INSTANCE_FAIL("40001", "反射新建对象失败"),

    ;

    /**
     * 编码
     */
    private final String code;

    /**
     * 消息
     */
    private final String msg;

    CommonRespCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

}
