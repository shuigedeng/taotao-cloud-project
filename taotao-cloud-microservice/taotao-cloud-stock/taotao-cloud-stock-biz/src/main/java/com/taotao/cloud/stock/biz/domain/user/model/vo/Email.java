package com.taotao.cloud.stock.biz.domain.user.model.vo;

import com.xtoon.cloud.common.core.domain.ValueObject;
import org.apache.commons.lang.Validate;

import java.util.regex.Pattern;

/**
 * 邮箱
 *
 * @author shuigedeng
 * @date 2021-02-08
 */
public final class Email implements ValueObject<Email> {

    private String email;

    /**
     * 有效性正则
     */
    private static final Pattern VALID_PATTERN = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");

    /**
     * Constructor.
     *
     * @param email
     */
    public Email(final String email) {
        if (email != null) {
            Validate.isTrue(VALID_PATTERN.matcher(email).matches(),
                    "邮箱格式不正确");
        }
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean sameValueAs(Email other) {
        return other != null && this.email.equals(other.email);
    }

    @Override
    public String toString() {
        return email;
    }
}
