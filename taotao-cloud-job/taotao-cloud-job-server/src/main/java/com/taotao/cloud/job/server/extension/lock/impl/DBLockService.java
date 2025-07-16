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

package com.taotao.cloud.job.server.extension.lock.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.job.server.extension.lock.LockService;
import com.taotao.cloud.job.server.persistence.domain.DistributedLock;
import com.taotao.cloud.job.server.persistence.mapper.DistributedLockMapper;
import jakarta.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DBLockService extends ServiceImpl<DistributedLockMapper, DistributedLock>
        implements LockService {
    private String ownerIp;

    @Autowired DistributedLockMapper distributedLockMapper;

    @PostConstruct
    public void init() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        ownerIp = inetAddress.getHostAddress();
    }

    @Override
    public boolean tryLock(String name, long maxLockTime) {

        Date now = new Date();
        long fiveSecondsLaterInMillis = now.getTime() + 5000;
        Date expirationTime = new Date(fiveSecondsLaterInMillis); // 将ms转换为纳秒

        // 先查询是否已有锁存在且未过期
        QueryWrapper<DistributedLock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lock_name", name);
        DistributedLock existingLock = getOne(queryWrapper, false);

        // todo
        if (existingLock != null && existingLock.getExpirationTime().after(now)) {
            // 锁已存在且未过期，无法获取锁
            return false;
        }

        // 获取锁，使用行锁
        DistributedLock lock = Optional.ofNullable(existingLock).orElse(new DistributedLock());
        lock.setLockName(name);
        lock.setLockOwner(ownerIp); // 设置当前线程为锁持有者
        lock.setExpirationTime(expirationTime);

        // 插入或更新锁信息
        saveOrUpdate(lock);
        return true;
    }

    @Override
    public void unlock(String name) {

        QueryWrapper<DistributedLock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lock_name", name);
        DistributedLock lock = getOne(queryWrapper, false);

        if (lock != null) {
            // 只有持有锁的线程才能解锁
            if (ownerIp.equals(lock.getLockOwner())) {
                // 删除锁
                removeById(lock.getId());
            }
        }
    }
}
