package com.taotao.cloud.data.mybatisplus.tenant;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import net.sf.jsqlparser.expression.ValueListExpression;

/**
 *  [初始创建]
 */
public interface MultiTenantLineHandler extends TenantLineHandler {
    /**
     * 获取租户 ID 值表达式，支持多个 ID 值
     * <p>
     *
     * @return 租户 ID 值表达式
     */
    ValueListExpression getTenantIdList();
}
