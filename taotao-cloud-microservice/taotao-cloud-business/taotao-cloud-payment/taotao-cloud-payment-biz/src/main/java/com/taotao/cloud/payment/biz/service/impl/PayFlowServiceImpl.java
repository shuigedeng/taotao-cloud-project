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

package com.taotao.cloud.payment.biz.service.impl;

import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.dubbo.biz.entity.PayFlow;
import com.taotao.cloud.payment.biz.repository.PayFlowSuperRepository;
import com.taotao.cloud.payment.biz.service.IPayFlowService;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * @author shuigedeng
 * @since 2020/11/13 10:00
 * @version 2022.03
 */
@Service
public class PayFlowServiceImpl implements IPayFlowService {

    private final PayFlowSuperRepository payFlowRepository;

    public PayFlowServiceImpl(PayFlowSuperRepository payFlowRepository) {
        this.payFlowRepository = payFlowRepository;
    }

    @Override
    public PayFlow findPayFlowById(Long id) {
        Optional<PayFlow> optionalExpressCompany = payFlowRepository.findById(id);
        return optionalExpressCompany.orElseThrow(() -> new BusinessException(ResultEnum.PAY_FLOW_NOT_EXIST));
    }
}
