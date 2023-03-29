package com.taotao.cloud.tenant.api.feign.fallback;

import com.taotao.cloud.tenant.api.feign.TenantServiceApi;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignTenantFallbackImpl implements
	FallbackFactory<TenantServiceApi> {

	@Override
	public TenantServiceApi create(Throwable throwable) {
		return new TenantServiceApi() {

			@Override
			public void validTenant(Long id) {

			}
		};
	}
}
