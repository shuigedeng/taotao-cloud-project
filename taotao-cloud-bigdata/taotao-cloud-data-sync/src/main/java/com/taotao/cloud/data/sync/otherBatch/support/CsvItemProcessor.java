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

import com.taotao.cloud.data.sync.otherBatch.domain.User;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.jspecify.annotations.NonNull;

/**
 * <p>
 * CsvItemProcessor
 * </p>
 *
 * @author livk
 */
@RequiredArgsConstructor
public class CsvItemProcessor implements ItemProcessor<User, User> {

    private final Validator<? super User> validator;

    @Override
    public User process(@NonNull User item) throws ValidationException {
        try {
            validator.validate(item);
            if (item.getSex().equals("男")) {
                item.setSex("1");
            } else {
                item.setSex("2");
            }
            item.setStatus(1);
            item.setCreateTime(new Date());
            item.setUpdateTime(new Date());
            return item;
        } catch (ValidationException e) {
            return null;
        }
    }
}
