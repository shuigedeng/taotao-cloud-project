package com.taotao.cloud.payment.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.payment.biz.entity.RefundLog;
import com.taotao.cloud.payment.biz.mapper.RefundLogMapper;
import com.taotao.cloud.payment.biz.service.RefundLogService;
import org.springframework.stereotype.Service;

/**
 * 退款日志 业务实现
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-05-30 16:47:38
 */
@Service
public class RefundLogServiceImpl extends ServiceImpl<RefundLogMapper, RefundLog> implements
	RefundLogService {

    @Override
    public RefundLog queryByAfterSaleSn(String sn) {
        return this.getOne(new LambdaUpdateWrapper<RefundLog>().eq(RefundLog::getAfterSaleNo, sn));
    }
}
