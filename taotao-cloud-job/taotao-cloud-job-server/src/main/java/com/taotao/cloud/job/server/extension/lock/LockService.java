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

package com.taotao.cloud.job.server.extension.lock;

public interface LockService {

    /**
     * 上锁（获取锁），立即返回，不会阻塞等待锁
     * @param name 锁名称
     * @param maxLockTime 最长持有锁的时间，单位毫秒（ms）
     * @return true -> 获取到锁，false -> 未获取到锁
     */
    boolean tryLock(String name, long maxLockTime);

    /**
     * 释放锁
     * @param name 锁名称
     */
    void unlock(String name);
}
