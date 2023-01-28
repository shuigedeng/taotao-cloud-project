package com.taotao.cloud.workflow.biz.common.model.login;

import lombok.Data;

/**
 */
@Data
public class LoginForm {
    private String account;
    private String password;
    /**
     * 验证码标识
     */
    private String timestamp;
    /**
     * 验证码
     */
    private String code;

    public LoginForm() {
    }

    public LoginForm(String account, String password) {
        this.account = account;
        this.password = password;
    }
}
