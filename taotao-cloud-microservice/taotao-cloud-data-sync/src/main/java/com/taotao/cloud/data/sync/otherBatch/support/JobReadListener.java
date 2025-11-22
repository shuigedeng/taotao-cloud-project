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
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * <p>
 * JobReadListener
 * </p>
 *
 * @author livk
 */
@Slf4j
public class JobReadListener implements ItemReadListener<User> {

    @Override
    public void beforeRead() {
        log.info("spring batch start read");
    }

    @Override
    public void afterRead(@Nullable User item) {
        log.info("spring batch is read, data:{}", item);
    }

    @Override
    public void onReadError(@NonNull Exception ex) {
        log.error("spring batch read an error occurred ,message:{}", ex.getMessage(), ex);
    }
}
