package com.taotao.cloud.member.api.feign.fallback;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.core.model.SecurityUser;
import com.taotao.cloud.member.api.feign.RemoteMemberService;
import com.taotao.cloud.member.api.vo.MemberVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteMemberFallbackImpl
 *
 * @author dengtao
 * @date 2020/11/20 下午4:10
 * @since v1.0
 */
public class RemoteMemberFallbackImpl implements FallbackFactory<RemoteMemberService> {
	@Override
	public RemoteMemberService create(Throwable throwable) {
		return new RemoteMemberService() {
			@Override
			public Result<SecurityUser> getMemberSecurityUser(String getMemberSecurityUser) {
				LogUtil.error("调用getMemberSecurityUser异常：{}", throwable, getMemberSecurityUser);
				return Result.failed(null, 500);
			}

			@Override
			public Result<MemberVO> findMemberById(Long id) {
				LogUtil.error("调用findMemberById异常：{}", throwable, id);
				return Result.failed(null, 500);
			}
		};
	}
}
