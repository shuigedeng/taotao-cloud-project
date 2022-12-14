package com.taotao.cloud.wechat.biz.service;//package com.taotao.cloud.wechat.biz.service;
//
//import com.baomidou.mybatisplus.extension.service.IService;
//
//import java.util.List;
//
///**
// * 平台PV统计
// */
//public interface PlatformViewService extends IService<PlatformViewData> {
//
//
//    /**
//     * 当前在线人数
//     *
//     * @return
//     */
//    Long online();
//
//    /**
//     * 会员分布
//     *
//     * @return
//     */
//    List<MemberDistributionVO> memberDistribution();
//
//    /**
//     * 在线人数记录
//     *
//     * @return
//     */
//    List<OnlineMemberVO> statisticsOnline();
//
//    /**
//     * 数据查询
//     *
//     * @param queryParam
//     * @return
//     */
//    List<PlatformViewVO> list(StatisticsQueryParam queryParam);
//
//    /**
//     * 查询累计访客数
//     *
//     * @param queryParam
//     * @return
//     */
//    Integer countUv(StatisticsQueryParam queryParam);
//}
