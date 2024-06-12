package com.taotao.cloud.rpc.common.common.config.component;

/**
 * 凭证信息
 * 针对安全的处理，最后关心。
 * @author shuigedeng
 * @since 2024.06
 */
public class Credential {

    /**
     * 用户名
     * @since 2024.06
     */
    private String username;

    /**
     * 密码
     * @since 2024.06
     */
    private String password;

    public String username() {
        return username;
    }

    public Credential username(String username) {
        this.username = username;
        return this;
    }

    public String password() {
        return password;
    }

    public Credential password(String password) {
        this.password = password;
        return this;
    }
}
