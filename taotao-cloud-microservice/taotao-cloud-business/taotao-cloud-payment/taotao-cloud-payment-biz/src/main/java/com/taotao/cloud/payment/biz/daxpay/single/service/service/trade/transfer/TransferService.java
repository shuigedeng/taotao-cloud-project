package com.taotao.cloud.payment.biz.daxpay.single.service.service.trade.transfer;

import cn.bootx.platform.starter.redis.delay.service.DelayJobService;
import cn.bootx.platform.core.util.ValidationUtil;
import com.taotao.cloud.payment.biz.daxpay.core.enums.TransferStatusEnum;
import com.taotao.cloud.payment.biz.daxpay.core.exception.TradeProcessingException;
import com.taotao.cloud.payment.biz.daxpay.core.param.trade.transfer.TransferParam;
import com.taotao.cloud.payment.biz.daxpay.core.result.trade.transfer.TransferResult;
import com.taotao.cloud.payment.biz.daxpay.service.bo.trade.TransferResultBo;
import com.taotao.cloud.payment.biz.daxpay.service.code.DaxPayCode;
import com.taotao.cloud.payment.biz.daxpay.service.dao.order.transfer.TransferOrderManager;
import com.taotao.cloud.payment.biz.daxpay.service.entity.order.transfer.TransferOrder;
import com.taotao.cloud.payment.biz.daxpay.service.service.notice.MerchantNoticeService;
import com.taotao.cloud.payment.biz.daxpay.service.service.record.flow.TradeFlowRecordService;
import com.taotao.cloud.payment.biz.daxpay.service.strategy.AbsTransferStrategy;
import com.taotao.cloud.payment.biz.daxpay.service.util.PaymentStrategyFactory;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 转账服务
 * @author xxm
 * @since 2024/3/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferAssistService transferAssistService;

    private final TransferOrderManager transferOrderManager;

    private final LockTemplate lockTemplate;
    private final MerchantNoticeService merchantNoticeService;
    private final TradeFlowRecordService tradeFlowRecordService;
    private final DelayJobService delayJobService;

    /**
     * 转账
     */
    public TransferResult transfer(TransferParam param) {
        // 参数校验
        ValidationUtil.validateParam(param);
        // 加锁
        LockInfo lock = lockTemplate.lock("payment:refund:" + param.getBizTransferNo(),10000,200);
        if (Objects.isNull(lock)){
            throw new TradeProcessingException("转账处理中，请勿重复操作");
        }
        try {
            // 判断是否是首次发起转账
            var transferOrder = transferOrderManager.findByBizTransferNo(param.getBizTransferNo(), param.getAppId());
            if (transferOrder.isPresent()){
                return this.repeatTransfer(transferOrder.get(),param);
            } else {
                return this.firstTransfer(param);
            }
        } finally {
            lockTemplate.releaseLock(lock);
        }

    }

    /**
     * 首次转账发起
     */
    private TransferResult firstTransfer(TransferParam param) {
        // 获取策略
        AbsTransferStrategy transferStrategy = PaymentStrategyFactory.create(param.getChannel(), AbsTransferStrategy.class);
        // 检查转账参数
        transferStrategy.doValidateParam(param);
        // 创建转账订单
        TransferOrder order = transferAssistService.createOrder(param);
        transferStrategy.setTransferOrder(order);
        // 执行预处理
        transferStrategy.doBeforeHandler();
        TransferResultBo transferResultBo;
        try {
            // 执行转账策略
            transferResultBo = transferStrategy.doTransferHandler();
        } catch (Exception e) {
            log.error("转账出现错误", e);
            // 更新转账失败的记录
            order.setStatus(TransferStatusEnum.FAIL.getCode()).setErrorMsg(e.getMessage());
            transferOrderManager.updateById(order);
            return transferAssistService.buildResult(order);
        }
        SpringUtil.getBean(this.getClass()).successHandler(order, transferResultBo);
        return transferAssistService.buildResult(order);
    }

    /**
     * 重复发起转账
     */
    private TransferResult repeatTransfer(TransferOrder order, TransferParam param) {
        // 只有转账失败才可可以重新发起
        if (!Objects.equals(TransferStatusEnum.FAIL.getCode(), order.getStatus())){
            throw new TradeProcessingException("只有失败状态的才可以重新发起转账");
        }
        // 获取策略
        AbsTransferStrategy transferStrategy = PaymentStrategyFactory.create(param.getChannel(), AbsTransferStrategy.class);
        // 检查转账参数
        transferStrategy.doValidateParam(param);
        transferStrategy.setTransferOrder(order);
        // 执行预处理
        transferStrategy.doBeforeHandler();
        TransferResultBo transferResultBo;
        try {
            // 执行转账策略
            transferResultBo = transferStrategy.doTransferHandler();
        } catch (Exception e) {
            log.error("重现转账出现错误", e);
            // 更新转账失败的记录
            order.setStatus(TransferStatusEnum.FAIL.getCode()).setErrorMsg(e.getMessage());
            transferOrderManager.updateById(order);
            return transferAssistService.buildResult(order);
        }
        SpringUtil.getBean(this.getClass()).successHandler(order, transferResultBo);
        return transferAssistService.buildResult(order);
    }

    /**
     * 成功处理
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void successHandler(TransferOrder order, TransferResultBo transferInfo){
        order.setStatus(transferInfo.getStatus().getCode())
                .setFinishTime(transferInfo.getFinishTime())
                .setOutTransferNo(transferInfo.getOutTransferNo());
        // 是否直接返回转账成功
        if (Objects.equals(transferInfo.getStatus(), TransferStatusEnum.SUCCESS)){
            // 发送转账订单通知消息
            merchantNoticeService.registerTransferNotice(order);
            tradeFlowRecordService.saveTransfer(order);
        } else {
            // 注册延时同步事件
            delayJobService.registerByTransaction(order.getId(), DaxPayCode.Event.MERCHANT_TRANSFER_SYNC, 2*60*1000L);
        }
        transferOrderManager.updateById(order);
    }
}
