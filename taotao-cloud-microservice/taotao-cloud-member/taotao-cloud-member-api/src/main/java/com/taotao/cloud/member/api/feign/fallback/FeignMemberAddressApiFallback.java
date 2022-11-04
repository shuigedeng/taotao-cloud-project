package com.taotao.cloud.member.api.feign.fallback;

import com.taotao.cloud.member.api.feign.FeignMemberAddressApi;
import com.taotao.cloud.member.api.model.vo.MemberAddressVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteMemberFallbackImpl
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/20 下午4:10
 */
public class FeignMemberAddressApiFallback implements FallbackFactory<FeignMemberAddressApi> {

	@Override
	public FeignMemberAddressApi create(Throwable throwable) {
		return new FeignMemberAddressApi() {
			@Override
			public MemberAddressVO getById(String shippingAddressId) {
				return null;
			}
		};
	}
}
