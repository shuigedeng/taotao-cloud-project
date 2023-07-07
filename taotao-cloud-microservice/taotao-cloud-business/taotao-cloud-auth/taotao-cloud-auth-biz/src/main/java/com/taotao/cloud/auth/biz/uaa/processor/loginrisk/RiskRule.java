package com.taotao.cloud.auth.biz.uaa.processor.loginrisk;

import lombok.Data;

@Data
public class RiskRule {

    private Integer id;

    /**
     * 风险名称
     */
    private String riskName;

    /**
     * 白名单ip
     */
    private String acceptIp;

    /**
     * 触发次数
     */
    private Integer triggerNumber;

    /**
     * 触发时间
     */
    private Integer triggerTime;

    /**
     * 触发时间类型
     */
    private Integer triggerTimeType;

    /**
     * 异常登录时间 （json）
     */
    private String unusualLoginTime;

    /**
     * 采取的操作措施 1：提示 2：发送短信  3：阻断登录  4：封号
     */
    private Integer operate;

}
