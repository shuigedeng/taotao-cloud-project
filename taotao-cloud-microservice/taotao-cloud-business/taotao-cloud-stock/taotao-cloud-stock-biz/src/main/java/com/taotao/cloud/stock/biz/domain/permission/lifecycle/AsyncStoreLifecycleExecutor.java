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

package com.taotao.cloud.stock.biz.domain.permission.lifecycle;

import org.mallfoundry.security.SubjectHolder;
import org.mallfoundry.store.Store;
import org.mallfoundry.store.StoreLifecycle;
import org.mallfoundry.store.StorePostService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public class AsyncStoreLifecycleExecutor {

    private final StoreLifecycle lifecycle;

    private final StorePostService storePostService;

    public AsyncStoreLifecycleExecutor(StoreLifecycle lifecycle, StorePostService storePostService) {
        this.lifecycle = lifecycle;
        this.storePostService = storePostService;
    }

    @Async
    @Transactional
    public void doInitialize(Store store) {
        SubjectHolder.switchTo().systemUser().doRun(() -> {
            var progress = StoreProgressResources.getStoreProgress(store.toId());
            try {
                progress.initializing();
                this.lifecycle.doInitialize(store);
                this.storePostService.initializeStore(store);
                progress.initialized();
            } catch (Exception e) {
                progress.failed();
                throw e;
            }
        });
    }

    @Async
    @Transactional
    public void doClose(Store store) {
        SubjectHolder.switchTo().systemUser().doRun(() -> {
            var progress = StoreProgressResources.getStoreProgress(store.toId());
            try {
                progress.closing();
                this.lifecycle.doClose(store);
                this.storePostService.closeStore(store);
                progress.closed();
            } catch (Exception e) {
                progress.failed();
                throw e;
            }
        });
    }
}
