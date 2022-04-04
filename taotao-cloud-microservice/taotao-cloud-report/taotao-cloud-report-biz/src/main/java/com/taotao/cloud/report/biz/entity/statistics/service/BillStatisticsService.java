package com.taotao.cloud.report.biz.entity.statistics.service;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 结算单统计
 */
public interface BillStatisticsService extends IService<Bill> {

    /**
     * 商家待结算数量
     *
     * @param billStatusEnum 结算单类型
     * @return 待结算商家数量
     */
    long billNum(BillStatusEnum billStatusEnum);
}
