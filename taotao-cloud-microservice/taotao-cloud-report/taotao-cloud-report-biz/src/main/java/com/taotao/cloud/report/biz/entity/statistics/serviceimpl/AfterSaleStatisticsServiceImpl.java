package com.taotao.cloud.report.biz.entity.statistics.serviceimpl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.enums.UserEnum;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * 售后统计业务层实现
 */
@Service
public class AfterSaleStatisticsServiceImpl extends ServiceImpl<AfterSaleStatisticsMapper, AfterSale> implements AfterSaleStatisticsService {


    @Override
    public long applyNum(String serviceType) {
        AuthUser authUser = Objects.requireNonNull(UserContext.getCurrentUser());
        LambdaQueryWrapper<AfterSale> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(AfterSale::getServiceStatus, AfterSaleStatusEnum.APPLY.name());
        queryWrapper.eq(CharSequenceUtil.isNotEmpty(serviceType), AfterSale::getServiceType, serviceType);
        queryWrapper.eq(CharSequenceUtil.equals(authUser.getRole().name(), UserEnum.STORE.name()),
                AfterSale::getStoreId, authUser.getStoreId());
        return this.count(queryWrapper);
    }


    @Override
    public IPage<AfterSale> getStatistics(StatisticsQueryParam statisticsQueryParam, PageVO pageVO) {

        LambdaQueryWrapper<AfterSale> queryWrapper = new LambdaQueryWrapper<>();
        Date[] dates = StatisticsDateUtil.getDateArray(statisticsQueryParam);
        queryWrapper.between(AfterSale::getCreateTime, dates[0], dates[1]);
        queryWrapper.eq(CharSequenceUtil.isNotEmpty(statisticsQueryParam.getStoreId()), AfterSale::getStoreId, statisticsQueryParam.getStoreId());

        return this.page(PageUtil.initPage(pageVO), queryWrapper);
    }

}
