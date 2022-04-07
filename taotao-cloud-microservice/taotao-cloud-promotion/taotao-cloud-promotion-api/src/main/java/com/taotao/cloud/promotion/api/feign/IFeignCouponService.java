/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.promotion.api.feign;

import com.taotao.cloud.coupon.api.feign.fallback.RemoteCouponFallbackImpl;
import com.taotao.cloud.common.constant.ServiceName;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 远程调用售后模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "remoteWithdrawService", value = ServiceName.TAOTAO_CLOUD_AFTERSALE_CENTER, fallbackFactory = RemoteCouponFallbackImpl.class)
public interface IFeignCouponService {

	///**
	// * 根据id查询提现申请信息
	// *
	// * @param id id
	// * @return com.taotao.cloud.core.model.Result<com.taotao.cloud.aftersale.api.vo.WithdrawVO>
	// * @author shuigedeng
	// * @since 2020/11/20 上午9:50
	// * @version 2022.03
	// */
	//@GetMapping(value = "/withdraw/info/id/{id:[0-9]*}")
	//Result<CouponVO> getMemberSecurityUser(@PathVariable(value = "id") Long id);
}

