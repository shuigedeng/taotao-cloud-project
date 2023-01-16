package com.taotao.cloud.workflow.api.common.constant.model;

/**
 * 类功能
 *
 */
public class MCode {

    /**
     * 提示信息类型
     */
    private final String type;

    /**
     * 错误编码
     */
    private final String code;

    /**
     * description 描述
     */
    private final String desc;


    public MCode(String type, String desc){
        this.type = type;
        this.code = this.getClass().getName();
        this.desc = desc;
    }

    public String get(){
        return desc;
    }

    public String getMsg(){
        return type + ":" + code + " " + desc;
    }

}
