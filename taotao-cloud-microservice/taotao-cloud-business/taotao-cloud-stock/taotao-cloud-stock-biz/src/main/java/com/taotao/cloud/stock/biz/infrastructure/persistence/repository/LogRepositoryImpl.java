package com.taotao.cloud.stock.biz.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * 日志-Repository实现类
 *
 * @author shuigedeng
 * @date 2021-02-02
 */
@Repository
public class LogRepositoryImpl extends ServiceImpl<SysLogMapper, SysLogDO> implements LogRepository, IService<SysLogDO> {

    @Override
    public void store(Log log) {
        SysLogDO sysLogDO = LogConverter.fromLog(log);
        if (sysLogDO.getId() == null) {
            this.save(sysLogDO);
        } else {
            this.updateById(sysLogDO);
        }
    }
}
