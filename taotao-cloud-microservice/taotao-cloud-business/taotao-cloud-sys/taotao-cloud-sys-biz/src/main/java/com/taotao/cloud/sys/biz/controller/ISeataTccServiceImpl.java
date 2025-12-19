package com.taotao.cloud.sys.biz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.job.quartz.enums.QuartzJobCode;
import com.taotao.cloud.sys.api.model.dto.QuartzJobDTO;
import com.taotao.cloud.sys.biz.task.job.quartz.entity.QuartzJob;
import com.taotao.cloud.sys.biz.task.job.quartz.mapper.QuartzJobMapper;
import org.apache.seata.core.context.RootContext;
import lombok.*;
import org.apache.seata.rm.tcc.api.BusinessActionContext;
import org.apache.seata.rm.tcc.api.BusinessActionContextParameter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * ISeataTccServiceImpl
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Service
@AllArgsConstructor
public class ISeataTccServiceImpl implements ISeataTccService {

    private final QuartzJobMapper quartzJobMapper;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public String tryInsert( @BusinessActionContextParameter(paramName = "quartzJobDTO") QuartzJobDTO quartzJobDTO,
            @BusinessActionContextParameter(paramName = "jobId") Long jobId ) {
        LogUtils.info("try------------------> xid = " + RootContext.getXID());

        QuartzJob quartzJobEntity = BeanUtil.copyProperties(quartzJobDTO, QuartzJob.class);
        quartzJobEntity.setState(QuartzJobCode.STOP);

        quartzJobMapper.insert(quartzJobEntity);

        return "success";
    }


    @Override
    public boolean commitTcc( BusinessActionContext context ) {
        LogUtils.info("xid = " + context.getXid() + "提交成功");
        return true;
    }


    @Override
    public boolean cancel( BusinessActionContext context ) {
        // 获取下单时的提交参数
        Integer jobId = (Integer) context.getActionContext("jobId");

        // 进行分支事务扣掉的金额回滚
        LambdaQueryWrapper<QuartzJob> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(QuartzJob::getId, jobId);
        quartzJobMapper.delete(lambdaQueryWrapper);

        LogUtils.info("xid = " + context.getXid() + "进行回滚操作");
        return true;
    }

}
