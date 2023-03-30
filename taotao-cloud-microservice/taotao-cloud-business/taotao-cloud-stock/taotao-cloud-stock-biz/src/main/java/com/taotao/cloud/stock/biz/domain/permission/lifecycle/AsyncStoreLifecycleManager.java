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

import java.util.Objects;
import java.util.Optional;
import org.mallfoundry.store.Store;
import org.mallfoundry.store.StoreId;
import org.mallfoundry.store.StoreLifecycleManager;
import org.mallfoundry.store.StoreProgress;

public class AsyncStoreLifecycleManager implements StoreLifecycleManager {

    private final AsyncStoreLifecycleExecutor lifecycleExecutor;

    public AsyncStoreLifecycleManager(AsyncStoreLifecycleExecutor lifecycleExecutor) {
        this.lifecycleExecutor = lifecycleExecutor;
    }

    @Override
    public StoreProgress initializeStore(Store store) {
        var storeId = store.toId();
        StoreProgressResources.removeStoreProgress(storeId);
        StoreProgressResources.addStoreProgress(storeId, new DefaultStoreProgress());
        this.lifecycleExecutor.doInitialize(store);
        StoreProgress nullableProgress = null;
        while (Objects.isNull(nullableProgress)) {
            nullableProgress = StoreProgressResources.getStoreProgress(storeId);
        }
        return nullableProgress;
    }

    @Override
    public StoreProgress closeStore(Store store) {
        var storeId = store.toId();
        StoreProgressResources.removeStoreProgress(storeId);
        StoreProgressResources.addStoreProgress(storeId, new DefaultStoreProgress());
        this.lifecycleExecutor.doClose(store);
        StoreProgress nullableProgress = null;
        while (Objects.isNull(nullableProgress)) {
            nullableProgress = StoreProgressResources.getStoreProgress(storeId);
        }
        return nullableProgress;
    }

    @Override
    public Optional<StoreProgress> findStoreProgress(StoreId storeId) {
        return Optional.ofNullable(StoreProgressResources.getStoreProgress(storeId));
    }
}
