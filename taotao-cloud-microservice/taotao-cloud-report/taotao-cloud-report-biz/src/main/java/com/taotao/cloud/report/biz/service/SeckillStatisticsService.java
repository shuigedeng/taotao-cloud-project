package com.taotao.cloud.report.biz.service;

import cn.lili.modules.promotion.entity.dos.Seckill;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 秒杀统计
 */
public interface SeckillStatisticsService extends IService<Seckill> {


    /**
     * 获取当前可参与的活动数量
     *
     * @return 可参与活动数量
     */
    long getApplyNum();

}
