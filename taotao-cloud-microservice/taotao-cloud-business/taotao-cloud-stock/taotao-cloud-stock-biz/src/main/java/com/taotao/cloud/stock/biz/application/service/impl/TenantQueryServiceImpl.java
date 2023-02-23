package com.taotao.cloud.stock.biz.application.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 租户查询服务实现类
 *
 * @author shuigedeng
 * @date 2021-05-10
 */
@Service
public class TenantQueryServiceImpl implements TenantQueryService {

    @Autowired
    private SysTenantMapper sysTenantMapper;

    @Override
    public Page queryPage(Map<String, Object> params) {
        IPage<SysTenantDO> page = sysTenantMapper.queryPage(new Query().getPage(params), params);
        return PageAssembler.toPage(page);
    }
}
