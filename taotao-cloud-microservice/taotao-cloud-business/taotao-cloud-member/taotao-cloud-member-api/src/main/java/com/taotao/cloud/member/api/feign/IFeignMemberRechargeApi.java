package com.taotao.cloud.member.api.feign;

import cn.hutool.core.date.DateTime;
import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.member.api.feign.fallback.FeignMemberRechargeApiFallback;
import com.taotao.cloud.member.api.model.vo.MemberRechargeVO;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用会员用户模块
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:37:54
 */
@FeignClient(value = ServiceName.TAOTAO_CLOUD_MEMBER, fallbackFactory = FeignMemberRechargeApiFallback.class)
public interface IFeignMemberRechargeApi {

	@GetMapping(value = "/member/feign/recharge/paySuccess")
	Boolean paySuccess(@RequestParam String sn, @RequestParam String receivableNo,
		@RequestParam String paymentMethod);

	@GetMapping(value = "/member/feign/recharge/getRecharge")
	MemberRechargeVO getRecharge(@RequestParam String sn);

	@GetMapping(value = "/member/feign/recharge/recharge")
	MemberRechargeVO recharge(@RequestParam BigDecimal price);

	/**
	 * LambdaQueryWrapper<Recharge> queryWrapper = new LambdaQueryWrapper<>();
	 * queryWrapper.eq(Recharge::getPayStatus, PayStatusEnum.UNPAID.name()); //充值订单创建时间 <= 订单自动取消时间
	 * queryWrapper.le(Recharge::getCreateTime, cancelTime);
	 *
	 * @return
	 */
	@GetMapping(value = "/member/feign/recharge/list")
	List<MemberRechargeVO> list(@RequestParam DateTime dateTime);

	@GetMapping(value = "/member/feign/recharge/rechargeOrderCancel")
	Boolean rechargeOrderCancel(@RequestParam String sn);
}

