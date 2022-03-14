package com.taotao.cloud.payment.biz.service;

import cn.lili.modules.payment.entity.RefundLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 退款日志 业务层
 *
 */
public interface RefundLogService extends IService<RefundLog> {
    /**
     * 根据售后sn查询退款日志
     * @param sn
     * @return
     */
    RefundLog queryByAfterSaleSn(String sn);
}
