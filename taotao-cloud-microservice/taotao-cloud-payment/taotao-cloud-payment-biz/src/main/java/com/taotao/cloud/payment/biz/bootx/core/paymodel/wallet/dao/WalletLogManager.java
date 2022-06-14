package com.taotao.cloud.payment.biz.bootx.core.paymodel.wallet.dao;

import cn.bootx.common.core.rest.param.PageParam;
import cn.bootx.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.common.mybatisplus.impl.BaseManager;
import cn.bootx.common.mybatisplus.util.MpUtil;
import cn.bootx.payment.core.paymodel.wallet.entity.WalletLog;
import cn.bootx.payment.param.paymodel.wallet.WalletLogQueryParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
* 钱包日志
* @author xxm
* @date 2020/12/8
*/
@Repository
@RequiredArgsConstructor
public class WalletLogManager extends BaseManager<WalletLogMapper,WalletLog> {

    /**
     * 按付款查找优先
     */
    public Optional<WalletLog> findFirstByPayment(Long paymentId) {
        return lambdaQuery().eq(WalletLog::getPaymentId,paymentId)
                .orderByDesc(MpBaseEntity::getId)
                .last("limit 1")
                .oneOpt();
    }

    /**
     * 分页查询指定用户的钱包日志
     */
    public Page<WalletLog> pageByUserId(PageParam pageParam, WalletLogQueryParam param, Long userId) {
        Page<WalletLog> mpPage = MpUtil.getMpPage(pageParam, WalletLog.class);

        return this.lambdaQuery()
                .orderByDesc(MpBaseEntity::getId)
                .eq(WalletLog::getUserId,userId)
                .page(mpPage);
    }

    /**
     * 分页查询
     */
    public Page<WalletLog> page(PageParam pageParam, WalletLogQueryParam param) {
        Page<WalletLog> mpPage = MpUtil.getMpPage(pageParam, WalletLog.class);
        return this.lambdaQuery()
                .orderByDesc(MpBaseEntity::getId)
                .page(mpPage);
    }
    /**
     * 分页查询 根据钱包id
     */
    public Page<WalletLog> pageByWalletId(PageParam pageParam, WalletLogQueryParam param) {
        Page<WalletLog> mpPage = MpUtil.getMpPage(pageParam, WalletLog.class);
        return this.lambdaQuery()
                .orderByDesc(MpBaseEntity::getId)
                .eq(WalletLog::getWalletId,param.getWalletId())
                .page(mpPage);
    }
}
