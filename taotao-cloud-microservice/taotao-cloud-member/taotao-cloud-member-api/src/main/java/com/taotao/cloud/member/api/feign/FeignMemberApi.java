package com.taotao.cloud.member.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.member.api.feign.fallback.FeignMemberApiFallback;
import com.taotao.cloud.member.api.model.vo.MemberVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 远程调用会员用户模块
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:37:54
 */
@FeignClient(value = ServiceName.TAOTAO_CLOUD_MEMBER_CENTER, fallbackFactory = FeignMemberApiFallback.class)
public interface FeignMemberApi {

	/**
	 * 通过用户名查询用户包括角色权限等
	 *
	 * @param nicknameOrUserNameOrPhoneOrEmail 用户名
	 * @return 用户信息
	 * @since 2020/4/29 17:48
	 */
	@GetMapping(value = "/member/info/security")
	SecurityUser getMemberSecurityUser(String nicknameOrUserNameOrPhoneOrEmail);

	/**
	 * 根据id查询会员信息
	 *
	 * @param id id
	 * @return 会员信息
	 * @since 2020/11/20 下午4:10
	 */
	@GetMapping("/member/info/id/{id:[0-9]*}")
	MemberVO findMemberById(@PathVariable(value = "id") Long id);

	/**
	 * 更新成员点
	 *
	 * @param payPoint 支付点
	 * @param name     名字
	 * @param memberId 成员身份
	 * @param s        年代
	 * @return {@link Result }<{@link Boolean }>
	 * @since 2022-04-25 16:41:42
	 */
	@GetMapping(value = "/member/updateMemberPoint")
	Boolean updateMemberPoint(@RequestParam Long payPoint, @RequestParam String name, @RequestParam Long memberId, @RequestParam String s);

	@GetMapping(value = "/member/username")
	MemberVO findByUsername(@RequestParam String username);

	@GetMapping(value = "/member/memberId")
	MemberVO getById(@RequestParam Long memberId);

	/**
	 * new LambdaUpdateWrapper<Member>()
	 * .eq(Member::getId, member.getId())
	 * .set(Member::getHaveStore, true)
	 * .set(Member::getStoreId, store.getId())
	 */
	@GetMapping(value = "/member/memberId/storeId")
	Boolean update(@RequestParam Long memberId, @RequestParam Long storeId);

	@GetMapping(value = "/member")
	Boolean updateById(@RequestParam MemberVO member);

	@GetMapping(value = "/member/listFieldsByMemberIds")
	List<Map<String, Object>> listFieldsByMemberIds(@RequestParam String s, @RequestParam List<String> ids);
}

