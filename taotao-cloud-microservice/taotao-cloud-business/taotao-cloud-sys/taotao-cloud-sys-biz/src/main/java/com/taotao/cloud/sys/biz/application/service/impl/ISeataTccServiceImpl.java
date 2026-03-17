package com.taotao.cloud.sys.biz.application.service.impl;

import com.taotao.cloud.sys.api.model.dto.QuartzJobDTO;
import com.taotao.cloud.sys.biz.controller.ISeataTccService;
import lombok.AllArgsConstructor;
import org.apache.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.stereotype.Service;

/**
 * ISeataTccServiceImpl
 *
 * @author shuigedeng
 * @version 2026.04
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
