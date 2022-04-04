package com.taotao.cloud.report.biz.entity.statistics.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 退款订单统计业务层
 */
public interface RefundOrderStatisticsService extends IService<StoreFlow> {


    /**
     * 查询订单统计分页
     *
     * @param statisticsQueryParam 查询参数
     * @param pageVO               分页
     * @return 退款统计
     */
    IPage<RefundOrderStatisticsDataVO> getRefundOrderStatisticsData(PageVO pageVO, StatisticsQueryParam statisticsQueryParam);

    /**
     * 查询退款订单统计金额
     *
     * @param statisticsQueryParam 查询参数
     * @return 退款统计金额
     */
    BigDecimal getRefundOrderStatisticsPrice(StatisticsQueryParam statisticsQueryParam);
}
