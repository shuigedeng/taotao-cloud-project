package com.taotao.cloud.member.api.feign.fallback;

import com.taotao.cloud.member.api.feign.IFeignMemberWalletApi;
import com.taotao.cloud.member.api.model.dto.MemberWalletUpdateDTO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteMemberFallbackImpl
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/20 下午4:10
 */
public class FeignMemberWalletApiFallback implements FallbackFactory<IFeignMemberWalletApi> {

	@Override
	public IFeignMemberWalletApi create(Throwable throwable) {
		return new IFeignMemberWalletApi() {
			@Override
			public boolean increase(MemberWalletUpdateDTO memberWalletUpdateDTO) {
				return false;
			}

			@Override
			public boolean save(Long id, String username) {
				return false;
			}
		};
	}
}
