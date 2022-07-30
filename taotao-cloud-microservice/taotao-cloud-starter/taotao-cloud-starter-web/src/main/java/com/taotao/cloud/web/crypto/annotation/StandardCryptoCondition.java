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
package com.taotao.cloud.web.crypto.annotation;

import com.taotao.cloud.common.utils.common.PropertyUtil;
import com.taotao.cloud.web.crypto.CryptoStrategy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * <p>Description: 标准算法策略条件 </p>
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-30 11:30:41
 */
public class StandardCryptoCondition implements Condition {

    private static final Logger log = LoggerFactory.getLogger(StandardCryptoCondition.class);

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String property = PropertyUtil.getProperty("taotao.cloud.crypto.crypto-strategy",CryptoStrategy.STANDARD.name());
        boolean result = StringUtils.isNotBlank(property) && StringUtils.equalsIgnoreCase(property, CryptoStrategy.STANDARD.name());
        log.debug("Condition [Standard Crypto] value is [{}]", result);
        return result;
    }
}
