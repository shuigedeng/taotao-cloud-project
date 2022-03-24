package com.taotao.cloud.member.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.member.api.feign.IFeignMemberService;
import com.taotao.cloud.member.api.vo.MemberVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteMemberFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/11/20 下午4:10
 * @version 2022.03
 */
public class RemoteMemberFallbackImpl implements FallbackFactory<IFeignMemberService> {
	@Override
	public IFeignMemberService create(Throwable throwable) {
		return new IFeignMemberService() {
			@Override
			public Result<SecurityUser> getMemberSecurityUser(String getMemberSecurityUser) {
				LogUtil.error("调用getMemberSecurityUser异常：{}", throwable, getMemberSecurityUser);
				return Result.fail(null, 500);
			}

			@Override
			public Result<MemberVO> findMemberById(Long id) {
				LogUtil.error("调用findMemberById异常：{}", throwable, id);
				return Result.fail(null, 500);
			}
		};
	}
}
