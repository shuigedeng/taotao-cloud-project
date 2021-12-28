/*
 * Copyright (C) 2019-2020 the original author or authors.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.taotao.cloud.stock.biz.domain.permission.lifecycle;

import org.mallfoundry.store.Store;
import org.mallfoundry.store.StoreId;
import org.mallfoundry.store.StoreLifecycleManager;
import org.mallfoundry.store.StoreProgress;

import java.util.Objects;
import java.util.Optional;

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
