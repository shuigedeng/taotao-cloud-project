package com.taotao.cloud.report.biz.entity.statistics.service;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 会员商品评价统计
 */
public interface MemberEvaluationStatisticsService extends IService<MemberEvaluation> {

    /**
     * 获取今天新增的评价数量
     *
     * @return 今日评价数量
     */
    long todayMemberEvaluation();

    /**
     * 获取等待回复评价数量
     *
     * @return 等待回复评价数量
     */
    long getWaitReplyNum();

}
