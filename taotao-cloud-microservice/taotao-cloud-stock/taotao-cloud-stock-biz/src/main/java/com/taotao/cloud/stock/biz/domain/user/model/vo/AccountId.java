package com.taotao.cloud.stock.biz.domain.user.model.vo;

import com.xtoon.cloud.common.core.domain.ValueObject;
import org.apache.commons.lang3.StringUtils;

/**
 * 账号ID
 *
 * @author shuigedeng
 * @date 2021-02-08
 */
public class AccountId implements ValueObject<AccountId> {

    private String id;

    public AccountId(final String id) {
        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("账号id不能为空");
        }
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean sameValueAs(AccountId other) {
        return other != null && this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return id;
    }
}
