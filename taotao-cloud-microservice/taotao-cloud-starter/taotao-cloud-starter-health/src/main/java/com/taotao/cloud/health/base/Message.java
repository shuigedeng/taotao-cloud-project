package com.taotao.cloud.health.base;

import com.yh.csx.bsf.core.base.BsfExceptionType;
import com.yh.csx.bsf.core.base.BsfLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 报警消息
 * @author: chejiangyi
 * @version: 2019-07-24 13:44
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message{
    private EnumWarnType warnType;
    private String title;
    private String content;
    private BsfLevel levelType=BsfLevel.LOW;
    private BsfExceptionType exceptionType=BsfExceptionType.BE;
    private String  exceptionCode;
    private String bizScope;

    public Message(EnumWarnType warnType, String title, String content) {
        this.warnType = warnType;
        this.title = title;
        this.content = content;
    }
    public Message(String title, String content,BsfLevel enumLevelType) {
        this.warnType = EnumWarnType.ERROR;
        this.title = title;
        this.content = content;
        this.levelType=enumLevelType;
    }
    public Message(String title, String content,BsfExceptionType BsfExceptionType) {
        this.warnType = EnumWarnType.ERROR;
        this.title = title;
        this.content = content;
        this.exceptionType=BsfExceptionType;
    }
    public Message(String title, String content) {
        this.warnType = EnumWarnType.ERROR;
        this.title = title;
        this.content = content;
    }
    public Message(String title, String content,String exceptionCode) {
        this.warnType = EnumWarnType.ERROR;
        this.title = title;
        this.content = content;
        this.exceptionCode=exceptionCode;
    }
}
