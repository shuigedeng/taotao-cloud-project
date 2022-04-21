package com.taotao.cloud.stock.biz.domain.user.model.vo;

import com.xtoon.cloud.common.core.domain.ValueObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码
 *
 * @author shuigedeng
 * @date 2021-02-08
 */
public class Password implements ValueObject<Password> {

    public static final String DEFAULT = "123456";

    /**
     * 密码
     */
    private String password;

    public Password(String password) {
        if (StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("密码不能为空");
        }
        this.password = password;
    }

    public static Password create(String passwordStr) {
        String password = new BCryptPasswordEncoder().encode(passwordStr);
        return new Password(password);
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean sameValueAs(Password other) {
        return other != null && this.password.equals(other.password);
    }

    @Override
    public String toString() {
        return "Password{" +
                "password='" + password + '\'' +
                '}';
    }
}
