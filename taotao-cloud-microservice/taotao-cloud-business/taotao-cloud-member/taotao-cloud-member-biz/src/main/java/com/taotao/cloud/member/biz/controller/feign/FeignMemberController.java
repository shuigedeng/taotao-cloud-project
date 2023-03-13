package com.taotao.cloud.member.biz.controller.feign;

import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.member.api.feign.IFeignMemberApi;
import com.taotao.cloud.member.api.model.vo.MemberVO;
import com.taotao.cloud.member.biz.service.business.IMemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;


/**
 * 店铺端,管理员API
 *
 * @since 2020/11/16 10:57
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "内部调用端-会员API", description = "内部调用端-会员API")
public class FeignMemberController implements IFeignMemberApi {

	private final IMemberService memberService;


	@Override
	public SecurityUser getMemberSecurityUser(String nicknameOrUserNameOrPhoneOrEmail) {
		return null;
	}

	@Override
	public MemberVO findMemberById(Long id) {
		return null;
	}

	@Override
	public Boolean updateMemberPoint(Long payPoint, String name, Long memberId, String s) {
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
	public Boolean update(Long memberId, Long storeId) {
		return null;
	}

	@Override
	public Boolean updateById(MemberVO member) {
		return null;
	}

	@Override
	public List<Map<String, Object>> listFieldsByMemberIds(String s, List<String> ids) {
		return null;
	}
}
