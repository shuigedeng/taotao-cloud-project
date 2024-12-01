package com.taotao.cloud.job.server.jobserver.extension.lock.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class DBLockService extends ServiceImpl<DistributedLockMapper, DistributedLock> implements LockService {
    private String ownerIp;

    @Autowired
    DistributedLockMapper distributedLockMapper;


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
