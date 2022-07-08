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
package com.taotao.cloud.dubbo.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Objects;

/**
 * 允许价值验证器
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:22:16
 */
public class AllowedValueValidator implements ConstraintValidator<AllowedValue, Long> {
 
    private long[] allowedValues;
 
    @Override
    public void initialize(AllowedValue constraintAnnotation) {
        this.allowedValues = constraintAnnotation.value();
    }
 
    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (allowedValues.length == 0) {
            return true;
        }
        return Arrays.stream(allowedValues).anyMatch(o -> Objects.equals(o, value));
    }
}
