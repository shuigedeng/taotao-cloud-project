package com.taotao.cloud.payment.biz.daxpay.single.service.service.constant;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import com.taotao.cloud.payment.biz.daxpay.service.dao.constant.MerchantNotifyConstManager;
import com.taotao.cloud.payment.biz.daxpay.service.param.constant.MerchantNotifyConstQuery;
import com.taotao.cloud.payment.biz.daxpay.service.result.constant.MerchantNotifyConstResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 商户订阅通知类型
 * @author xxm
 * @since 2024/8/5
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantNotifyConstService {
    private final MerchantNotifyConstManager manager;

    /**
     * 分页
     */
    public PageResult<MerchantNotifyConstResult> page(PageParam pageParam, MerchantNotifyConstQuery query) {
        return MpUtil.toPageResult(manager.page(pageParam, query));
    }
}