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

package com.taotao.cloud.payment.biz.jeepay.pay.service;

import com.taotao.cloud.payment.biz.jeepay.core.exception.BizException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * 通用 Validator
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021/6/8 17:47
 */
@Service
public class ValidateService {

    @Autowired
    private Validator validator;

    public void validate(Object obj) {

        Set<ConstraintViolation<Object>> resultSet = validator.validate(obj);
        if (resultSet == null || resultSet.isEmpty()) {
            return;
        }
        resultSet.stream().forEach(item -> {
            throw new BizException(item.getMessage());
        });
    }
}
