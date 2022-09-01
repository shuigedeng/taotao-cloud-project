package com.taotao.cloud.member.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.member.api.feign.IFeignMemberService;
import com.taotao.cloud.member.api.model.vo.MemberVO;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteMemberFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/11/20 下午4:10
 * @version 2022.03
 */
public class FeignMemberServiceFallback implements FallbackFactory<IFeignMemberService> {
	@Override
	public IFeignMemberService create(Throwable throwable) {
		return new IFeignMemberService() {
			@Override
			public Result<SecurityUser> getMemberSecurityUser(String getMemberSecurityUser) {
				LogUtils.error("调用getMemberSecurityUser异常：{}", throwable, getMemberSecurityUser);
				return Result.fail(null, 500);
			}

			@Override
			public Result<MemberVO> findMemberById(Long id) {
				LogUtils.error("调用findMemberById异常：{}", throwable, id);
				return Result.fail(null, 500);
			}

			@Override
			public Result<Boolean> updateMemberPoint(Long payPoint, String name, Long memberId,
				String s) {
				return null;
			}

			@Override
			public MemberVO findByUsername(String username) {
				return null;
			}

			@Override
			public MemberVO getById(Long memberId) {
				return null;
			}

			@Override
			public Boolean update(Long memberId, Long sotreId) {
			return false;
			}

			@Override
			public Boolean updateById(MemberVO member) {
				return false;
			}

			@Override
			public List<Map<String, Object>> listFieldsByMemberIds(String s, List<String> ids) {
				return null;
			}
		};
	}
}
