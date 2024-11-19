package com.taotao.cloud.payment.biz.daxpay.single.service.dao.record.callback;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.query.generator.QueryGenerator;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import com.taotao.cloud.payment.biz.daxpay.service.entity.record.callback.TradeCallbackRecord;
import com.taotao.cloud.payment.biz.daxpay.service.param.record.TradeCallbackRecordQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author xxm
 * @since 2024/7/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TradeCallbackRecordManager extends BaseManager<TradeCallbackRecordMapper, TradeCallbackRecord> {

    /**
     * 分页
     */
    public Page<TradeCallbackRecord> page(PageParam pageParam, TradeCallbackRecordQuery query){
        Page<TradeCallbackRecord> mpPage = MpUtil.getMpPage(pageParam, TradeCallbackRecord.class);
        QueryWrapper<TradeCallbackRecord> generator = QueryGenerator.generator(query);
        return page(mpPage, generator);
    }
}
