package com.taotao.cloud.member.api.feign.fallback;

import cn.hutool.core.date.DateTime;
import com.taotao.cloud.member.api.feign.FeignMemberRechargeApi;
import com.taotao.cloud.member.api.feign.FeignMemberWalletApi;
import com.taotao.cloud.member.api.model.dto.MemberWalletUpdateDTO;
import com.taotao.cloud.member.api.model.vo.MemberRechargeVO;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteMemberFallbackImpl
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/20 下午4:10
 */
public class FeignMemberWalletApiFallback implements FallbackFactory<FeignMemberWalletApi> {

	@Override
	public FeignMemberWalletApi create(Throwable throwable) {
		return new FeignMemberWalletApi() {
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
