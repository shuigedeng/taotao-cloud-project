package com.taotao.cloud.member.api.feign;

import cn.hutool.core.date.DateTime;
import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.member.api.feign.fallback.FeignMemberServiceFallback;
import com.taotao.cloud.member.api.web.vo.MemberRechargeVO;
import org.springframework.cloud.openfeign.FeignClient;

import java.math.BigDecimal;
import java.util.List;

/**
 * 远程调用会员用户模块
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:37:54
 */
@FeignClient(value = ServiceName.TAOTAO_CLOUD_MEMBER_CENTER, fallbackFactory = FeignMemberServiceFallback.class)
public interface IFeignMemberRechargeService {


	Result<Boolean> paySuccess(String sn, String receivableNo, String paymentMethod);

	Result<MemberRechargeVO> getRecharge(String sn);

	Result<MemberRechargeVO> recharge(BigDecimal price);

	/**
	 * 	LambdaQueryWrapper<Recharge> queryWrapper = new LambdaQueryWrapper<>();
	 * 			queryWrapper.eq(Recharge::getPayStatus, PayStatusEnum.UNPAID.name());
	 * 			//充值订单创建时间 <= 订单自动取消时间
	 * 			queryWrapper.le(Recharge::getCreateTime, cancelTime);
	 * @return
	 */
	Result<List<MemberRechargeVO>> list(DateTime dateTime);


	Result<Boolean> rechargeOrderCancel(String sn);
}

