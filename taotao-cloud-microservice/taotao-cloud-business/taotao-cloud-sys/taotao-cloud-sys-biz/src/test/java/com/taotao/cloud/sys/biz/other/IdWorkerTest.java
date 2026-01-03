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

package com.taotao.cloud.sys.biz.other;

import com.taotao.boot.common.utils.log.LogUtils;
import org.junit.jupiter.api.Test;
import shorturl.server.server.application.util.IdWorker;
import shorturl.server.server.application.util.IdWorkerInstance;

/**
 * IdWorkerTest
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class IdWorkerTest {

    IdWorker idWorker = IdWorkerInstance.INSTANCE.IdWorkerInstance();

    @Test
    public void nextId() {
        for (int i = 0; i < 10; i++) {
            long id = idWorker.generate();
            LogUtils.info("生成id为:" + id);
        }
    }
}
