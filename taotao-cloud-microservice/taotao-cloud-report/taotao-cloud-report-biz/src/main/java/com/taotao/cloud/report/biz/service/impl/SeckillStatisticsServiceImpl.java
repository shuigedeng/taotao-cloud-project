package com.taotao.cloud.report.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 秒杀活动统计
 *
 */
@Service
public class SeckillStatisticsServiceImpl extends ServiceImpl<SeckillStatisticsMapper, Seckill> implements SeckillStatisticsService {


    @Override
    public long getApplyNum() {
        QueryWrapper<Seckill> queryWrapper = Wrappers.query();
        //秒杀申请时间未超过当前时间
        queryWrapper.ge("apply_end_time", cn.hutool.core.date.DateUtil.date());
        queryWrapper.and(PromotionTools.queryPromotionStatus(PromotionsStatusEnum.START));
        return this.count(queryWrapper);
    }

}
