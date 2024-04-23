/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.member.facade.feign;

import com.taotao.cloud.member.api.feign.IFeignMemberRechargeApi;
import com.taotao.cloud.member.api.feign.response.FeignMemberRechargeResponse;
import com.taotao.cloud.member.application.service.IMemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import org.dromara.hutool.core.date.DateTime;
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
public class FeignMemberRechargeFacade implements IFeignMemberRechargeApi {

    private final IMemberService memberService;

    @Override
    public Boolean paySuccess(String sn, String receivableNo, String paymentMethod) {
        return null;
    }

    @Override
    public FeignMemberRechargeResponse getRecharge(String sn) {
        return null;
    }

    @Override
    public FeignMemberRechargeResponse recharge(BigDecimal price) {
        return null;
    }

    @Override
    public List<FeignMemberRechargeResponse> list(DateTime dateTime) {
        return null;
    }

    @Override
    public Boolean rechargeOrderCancel(String sn) {
        return null;
    }
}
