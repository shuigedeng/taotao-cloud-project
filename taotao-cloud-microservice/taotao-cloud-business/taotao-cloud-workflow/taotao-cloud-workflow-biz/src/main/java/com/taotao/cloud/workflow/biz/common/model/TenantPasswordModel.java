package com.taotao.cloud.workflow.biz.common.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 */
@Data
public class TenantPasswordModel {
    /**
     * 手机号
     */
    @JSONField(name = "Mobile")
    private String mobile;
    /**
     * 短信验证码
     */
    @JSONField(name = "SmsCode")
    private String smsCode;
    /**
     * 密码
     */
    @JSONField(name = "Password")
    private String password;
    /**
     * 公司名
     */
    @JSONField(name = "CompanyName")
    private String companyName;
    /**
     * 姓名
     */
    @JSONField(name = "Name")
    private String name;
}
