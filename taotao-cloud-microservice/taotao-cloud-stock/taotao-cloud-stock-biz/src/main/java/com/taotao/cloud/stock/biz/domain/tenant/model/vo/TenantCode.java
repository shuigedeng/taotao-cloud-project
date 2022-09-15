package com.taotao.cloud.stock.biz.domain.tenant.model.vo;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 租户编码
 *
 * @author shuigedeng
 * @date 2021-02-08
 */
public class TenantCode implements ValueObject<TenantCode> {

    private String code;

    private static final Pattern VALID_PATTERN = Pattern.compile("^[A-Za-z0-9]+$");


    public TenantCode(final String code) {
        if (StringUtils.isEmpty(code)) {
            throw new IllegalArgumentException("租户编码不能为空");
        }
        Validate.isTrue(VALID_PATTERN.matcher(code).matches(),
                "编码格式不正确");
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean sameValueAs(TenantCode other) {
        return other != null && this.code.equals(other.code);
    }

    @Override
    public String toString() {
        return code;
    }
}
