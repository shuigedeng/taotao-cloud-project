package com.taotao.cloud.member.biz.controller.feign;

import cn.hutool.core.date.DateTime;
import com.taotao.cloud.member.api.feign.IFeignMemberRechargeApi;
import com.taotao.cloud.member.api.model.vo.MemberRechargeVO;
import com.taotao.cloud.member.biz.service.business.IMemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.util.List;
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
@Tag(name = "内部调用端-会员充值API", description = "内部调用端-会员充值API")
public class FeignMemberRechargeController implements IFeignMemberRechargeApi {

	private final IMemberService memberService;


	@Override
	public Boolean paySuccess(String sn, String receivableNo, String paymentMethod) {
		return null;
	}

	@Override
	public MemberRechargeVO getRecharge(String sn) {
		return null;
	}

	@Override
	public MemberRechargeVO recharge(BigDecimal price) {
		return null;
	}

	@Override
	public List<MemberRechargeVO> list(DateTime dateTime) {
		return null;
	}

	@Override
	public Boolean rechargeOrderCancel(String sn) {
		return null;
	}
}
