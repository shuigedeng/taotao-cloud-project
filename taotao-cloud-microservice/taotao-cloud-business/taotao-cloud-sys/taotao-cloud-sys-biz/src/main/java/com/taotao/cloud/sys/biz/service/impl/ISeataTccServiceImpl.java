package com.taotao.cloud.sys.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.sys.api.model.dto.QuartzJobDTO;
import com.taotao.cloud.sys.biz.controller.ISeataTccService;
import org.apache.seata.core.context.RootContext;
import org.apache.seata.rm.tcc.api.BusinessActionContext;
import org.apache.seata.spring.annotation.GlobalTransactional;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    @Override
    public String tryInsert( QuartzJobDTO quartzJobDTO, Long jobId ) {
        return "";
    }

    @Override
    public boolean commitTcc( BusinessActionContext context ) {
        return false;
    }

    @Override
    public boolean cancel( BusinessActionContext context ) {
        return false;
    }

}
