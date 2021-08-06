package com.taotao.cloud.member.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.member.api.feign.RemoteMemberService;
import com.taotao.cloud.member.api.vo.MemberVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteMemberFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/11/20 下午4:10
 * @version 1.0.0
 */
public class RemoteMemberFallbackImpl implements FallbackFactory<RemoteMemberService> {
	@Override
	public RemoteMemberService create(Throwable throwable) {
		return new RemoteMemberService() {
			@Override
			public Result<SecurityUser> getMemberSecurityUser(String getMemberSecurityUser) {
				LogUtil.error("调用getMemberSecurityUser异常：{0}", throwable, getMemberSecurityUser);
				return Result.fail(null, 500);
			}

			@Override
			public Result<MemberVO> findMemberById(Long id) {
				LogUtil.error("调用findMemberById异常：{0}", throwable, id);
				return Result.fail(null, 500);
			}
		};
	}
}
