package com.taotao.cloud.workflow.api.common.base;

import lombok.Data;

/**
 *
 */
@Data
public class MailAccount {
    /**
     * pop3服务
     */
    private String pop3Host;
    /**
     * pop3端口
     */
    private int pop3Port;
    /**
     * smtp服务
     */
    private String smtpHost;
    /**
     * smtp端口
     */
    private int smtpPort;
    /**
     * 账户
     */
    private String account;
    /**
     * 账户名称
     */
    private String accountName;
    /**
     * 密码
     */
    private String password;
    /**
     * SSL
     */
    private Boolean ssl;;
}
