package com.taotao.cloud.data.jpa.tenancy;

import com.taotao.cloud.common.context.TenantContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: 租户选择器 </p>
 * <p>
 * 选择具体使用哪个租户
 */
public class MultiTenancyIdentifierResolver implements CurrentTenantIdentifierResolver {

	private static final Logger log = LoggerFactory.getLogger(MultiTenancyIdentifierResolver.class);

	@Override
	public String resolveCurrentTenantIdentifier() {
		String currentTenantId = TenantContextHolder.getTenant();
		String result = StringUtils.isNotBlank(currentTenantId) ? currentTenantId : "tenant_id";
		log.debug("[Herodotus] |- Resolve Current Tenant Identifier is : [{}]", result);
		return result;
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}
}
