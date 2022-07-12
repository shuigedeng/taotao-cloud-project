//package com.taotao.cloud.wechat.biz.mapper;
//
//import com.baomidou.mybatisplus.core.conditions.Wrapper;
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.baomidou.mybatisplus.core.toolkit.Constants;
//import com.taotao.cloud.wechat.biz.model.entity.MemberStatisticsData;
//import org.apache.ibatis.annotations.Param;
//import org.apache.ibatis.annotations.Select;
//
//import java.util.List;
//
///**
// * 会员统计数据处理层
// */
//public interface MemberStatisticsMapper extends BaseMapper<MemberStatisticsData> {
//
//    /**
//     * 获取会员统计数量
//     *
//     * @param queryWrapper 查询条件
//     * @return 会员统计数量
//     */
//    @Select("SELECT  COUNT(0)  FROM tt_member  ${ew.customSqlSegment}")
//    long customSqlQuery(@Param(Constants.WRAPPER) Wrapper queryWrapper);
//
//
//    /**
//     * 获取会员分布列表
//     * @return 会员分布列表
//     */
//    @Select("select client_enum,count(0) as num from tt_member group by client_enum")
//    List<MemberDistributionVO> distribution();
//}
