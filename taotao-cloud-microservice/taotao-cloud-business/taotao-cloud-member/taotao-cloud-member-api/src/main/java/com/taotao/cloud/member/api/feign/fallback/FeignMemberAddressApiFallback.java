package com.taotao.cloud.member.api.feign.fallback;

import com.taotao.cloud.member.api.feign.IFeignMemberAddressApi;
import com.taotao.cloud.member.api.model.vo.MemberAddressVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteMemberFallbackImpl
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/20 下午4:10
 */
public class FeignMemberAddressApiFallback implements FallbackFactory<IFeignMemberAddressApi> {

	@Override
	public IFeignMemberAddressApi create(Throwable throwable) {
		return new IFeignMemberAddressApi() {
			@Override
			public MemberAddressVO getById(String shippingAddressId) {
				return null;
			}
		};
	}
}
