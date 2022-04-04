package com.taotao.cloud.report.biz.entity.statistics.service;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 交易投诉统计
 **/
public interface OrderComplaintStatisticsService extends IService<OrderComplaint> {

    /**
     * 待处理投诉数量
     *
     * @return 待处理投诉数量
     */
    long waitComplainNum();
}
