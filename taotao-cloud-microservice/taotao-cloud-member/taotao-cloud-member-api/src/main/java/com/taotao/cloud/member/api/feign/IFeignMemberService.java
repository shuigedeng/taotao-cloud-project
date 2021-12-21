package com.taotao.cloud.member.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.member.api.feign.fallback.RemoteMemberFallbackImpl;
import com.taotao.cloud.member.api.vo.MemberVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 远程调用会员用户模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "IFeignMemberService", value = ServiceName.TAOTAO_CLOUD_MEMBER_CENTER, fallbackFactory = RemoteMemberFallbackImpl.class)
public interface IFeignMemberService {

	/**
	 * 通过用户名查询用户包括角色权限等o
	 *
	 * @param nicknameOrUserNameOrPhoneOrEmail 用户名
	 * @return com.taotao.cloud.common.model.Result<com.taotao.cloud.uc.api.dto.UserDetailsInfo>
	 * @author shuigedeng
	 * @since 2020/4/29 17:48
	 */
	@GetMapping(value = "/member/info/security")
	Result<SecurityUser> getMemberSecurityUser(String nicknameOrUserNameOrPhoneOrEmail);

	/**
	 * 根据id查询会员信息
	 *
	 * @param id id
	 * @return com.taotao.cloud.core.model.Result<com.taotao.cloud.member.api.vo.MemberVO>
	 * @author shuigedeng
	 * @since 2020/11/20 下午4:10
	 */
	@GetMapping("/member/info/id/{id:[0-9]*}")
	Result<MemberVO> findMemberById(@PathVariable(value = "id") Long id);
}

