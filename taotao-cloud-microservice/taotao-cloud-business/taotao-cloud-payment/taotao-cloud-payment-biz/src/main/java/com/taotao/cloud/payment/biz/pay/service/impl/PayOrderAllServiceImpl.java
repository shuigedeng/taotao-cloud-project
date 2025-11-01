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

package com.taotao.cloud.payment.biz.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhuicloud.common.data.ttl.XHuiCommonThreadLocalHolder;
import com.xhuicloud.common.zero.base.IDGenerate;
import com.xhuicloud.pay.dto.PayOrderDto;
import com.xhuicloud.pay.entity.PayOrderAll;
import com.xhuicloud.pay.mapper.PayOrderAllMapper;
import com.xhuicloud.pay.service.PayOrderAllService;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PayOrderAllServiceImpl extends ServiceImpl<PayOrderAllMapper, PayOrderAll> implements PayOrderAllService {

    private final IDGenerate defaultSnowflakeIDGenerate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayOrderAll create(PayOrderDto payOrderDto) {
        PayOrderAll payOrderAll = new PayOrderAll();
        BeanUtil.copyProperties(payOrderDto, payOrderAll);
        payOrderAll.setOrderNo(String.valueOf(defaultSnowflakeIDGenerate.get()));
        payOrderAll.setTenantId(XHuiCommonThreadLocalHolder.getTenant());
        payOrderAll.setState(0);
        save(payOrderAll);
        return payOrderAll;
    }
}
