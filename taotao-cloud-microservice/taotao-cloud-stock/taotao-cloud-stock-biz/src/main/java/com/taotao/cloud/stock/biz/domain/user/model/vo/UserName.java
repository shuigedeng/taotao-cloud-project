package com.taotao.cloud.stock.biz.domain.user.model.vo;

import com.xtoon.cloud.common.core.domain.ValueObject;
import org.apache.commons.lang3.StringUtils;

/**
 * 用户名
 *
 * @author shuigedeng
 * @date 2021-02-08
 **/
public class UserName implements ValueObject<UserName> {

    /**
     * 用户名
     */
    private String name;

    public UserName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean sameValueAs(UserName other) {
        return other != null && this.name.equals(other.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
