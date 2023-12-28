package com.taotao.cloud.tenant.biz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.tenant.api.model.dto.TenantDTO;
import com.taotao.cloud.tenant.biz.convert.TenantConvert;
import com.taotao.cloud.tenant.biz.dao.TenantMapper;
import com.taotao.cloud.tenant.biz.entity.Tenant;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ISeataTccServiceImpl implements ISeataTccService {

    private final TenantMapper tenantMapper;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public String tryInsert(@BusinessActionContextParameter(paramName = "tenantDTO") TenantDTO tenantDTO,
                            @BusinessActionContextParameter(paramName = "tenantId") Long tenantId) {
        LogUtils.info("try------------------> xid = " + RootContext.getXID());

        Tenant tenantDO = TenantConvert.INSTANCE.convert(tenantDTO);
        tenantMapper.insert(tenantDO);

        return "success";
    }


    @Override
    public boolean commitTcc(BusinessActionContext context) {
        LogUtils.info("xid = " + context.getXid() + "提交成功");
        return true;
    }


    @Override
    public boolean cancel(BusinessActionContext context) {
        // 获取下单时的提交参数
        Integer tenantId = (Integer) context.getActionContext("tenantId");

        // 进行分支事务扣掉的金额回滚
        LambdaQueryWrapper<Tenant> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Tenant::getId, tenantId);
        tenantMapper.delete(lambdaQueryWrapper);

        LogUtils.info("xid = " + context.getXid() + "进行回滚操作");
        return true;
    }

}
