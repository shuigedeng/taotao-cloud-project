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

import org.mallfoundry.security.SubjectHolder;
import org.mallfoundry.store.Store;
import org.mallfoundry.store.StoreLifecycle;
import org.mallfoundry.store.StorePostService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public class AsyncStoreLifecycleExecutor {

    private final StoreLifecycle lifecycle;

    private final StorePostService storePostService;

    public AsyncStoreLifecycleExecutor(StoreLifecycle lifecycle,
                                       StorePostService storePostService) {
        this.lifecycle = lifecycle;
        this.storePostService = storePostService;
    }

    @Async
    @Transactional
    public void doInitialize(Store store) {
        SubjectHolder.switchTo().systemUser()
                .doRun(() -> {
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
        SubjectHolder.switchTo().systemUser()
                .doRun(() -> {
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
