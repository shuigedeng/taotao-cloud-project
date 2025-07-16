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

package com.taotao.cloud.data.sync.otherBatch.support;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.lang.NonNull;

/**
 * <p>
 * CsvBeanValidator
 * </p>
 *
 * @author livk
 */
public class CsvBeanValidator<T> implements org.springframework.batch.item.validator.Validator<T> {

    private final Validator validator;

    public CsvBeanValidator() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            this.validator = validatorFactory.usingContext().getValidator();
        }
    }

    @Override
    public void validate(@NonNull T value) throws ValidationException {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(value);
        if (!constraintViolations.isEmpty()) {
            StringBuilder message = new StringBuilder();
            constraintViolations.forEach(
                    constraintViolation ->
                            message.append(constraintViolation.getMessage()).append("\n"));
            throw new ValidationException(message.toString());
        }
    }
}
