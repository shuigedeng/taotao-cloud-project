package com.taotao.cloud.report.biz.entity.statistics.service;


import java.util.List;

/**
 * 首页统计数据业务层
 */
public interface IndexStatisticsService {

    /**
     * 获取首页统计数据
     *
     * @return 运营后台首页统计数据
     */
    IndexStatisticsVO indexStatistics();

    /**
     * 商家首页统计数据
     *
     * @return 商家后台首页统计数据
     */
    StoreIndexStatisticsVO storeIndexStatistics();

    /**
     * 消息通知
     *
     * @return 通知内容
     */
    IndexNoticeVO indexNotice();

    /**
     * 查询热卖商品TOP10
     *
     * @param statisticsQueryParam 商品统计查询参数
     * @return 热卖商品TOP10
     */
    List<GoodsStatisticsDataVO> goodsStatistics(GoodsStatisticsQueryParam statisticsQueryParam);

    /**
     * 查询热卖店铺TOP10
     * @param statisticsQueryParam 统计查询参数
     *
     * @return 当月的热卖店铺TOP10
     */
    List<StoreStatisticsDataVO> storeStatistics(StatisticsQueryParam statisticsQueryParam);


}
