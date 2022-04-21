package com.taotao.cloud.report.biz.entity.statistics.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.enums.UserEnum;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.stereotype.Service;

/**
 * 交易投诉业务层实现
 */
@Service
public class OrderComplaintStatisticsServiceImpl extends ServiceImpl<OrderComplaintStatisticsMapper, OrderComplaint> implements OrderComplaintStatisticsService {

    @Override
    public long waitComplainNum() {
        QueryWrapper queryWrapper = Wrappers.query();
        queryWrapper.ne("complain_status", ComplaintStatusEnum.COMPLETE.name());
        queryWrapper.eq(StringUtils.equals(UserContext.getCurrentUser().getRole().name(), UserEnum.STORE.name()),
                "store_id", UserContext.getCurrentUser().getStoreId());
        return this.count(queryWrapper);
    }


}
