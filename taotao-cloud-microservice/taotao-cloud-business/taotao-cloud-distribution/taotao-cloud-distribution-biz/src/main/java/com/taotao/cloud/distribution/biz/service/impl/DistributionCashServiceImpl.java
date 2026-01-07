/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.distribution.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.utils.id.IdGeneratorUtils;
import com.taotao.boot.common.utils.number.CurrencyUtils;
import com.taotao.cloud.distribution.api.enums.DistributionStatusEnum;
import com.taotao.cloud.distribution.api.model.vo.DistributionCashSearchVO;
import com.taotao.cloud.distribution.biz.mapper.DistributionCashMapper;
import com.taotao.cloud.distribution.biz.model.entity.Distribution;
import com.taotao.cloud.distribution.biz.model.entity.DistributionCash;
import com.taotao.cloud.distribution.biz.service.DistributionCashService;
import com.taotao.cloud.distribution.biz.service.DistributionService;
import com.taotao.cloud.member.api.enums.DepositServiceTypeEnum;
import com.taotao.cloud.member.api.enums.MemberWithdrawalDestinationEnum;
import com.taotao.cloud.member.api.enums.WithdrawStatusEnum;
import com.taotao.cloud.member.api.feign.MemberWalletApi;
import com.taotao.cloud.member.api.model.dto.MemberWalletUpdateDTO;
import com.taotao.cloud.stream.framework.rocketmq.RocketmqSendCallbackBuilder;
import com.taotao.cloud.stream.framework.rocketmq.tags.MemberTagsEnum;
import com.taotao.cloud.stream.message.MemberWithdrawalMessage;
import com.taotao.cloud.stream.properties.RocketmqCustomProperties;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 分销佣金业务层实现 */
@Service
public class DistributionCashServiceImpl extends ServiceImpl<DistributionCashMapper, DistributionCash>
        implements DistributionCashService {

    /** 分销员 */
    @Autowired
    private DistributionService distributionService;
    /** 会员余额 */
    @Autowired
    private MemberWalletApi memberWalletApi;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private RocketmqCustomProperties rocketmqCustomProperties;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cash(BigDecimal applyMoney) {

        // 检查分销功能开关
        distributionService.checkDistributionSetting();

        // 获取分销员
        Distribution distribution = distributionService.getDistribution();
        // 如果未找到分销员或者分销员状态不是已通过则无法申请提现
        if (distribution != null && distribution.getDistributionStatus().equals(DistributionStatusEnum.PASS.name())) {
            // 校验分销佣金是否大于提现金额
            if (distribution.getCanRebate().compareTo(applyMoney) < 0) {
                throw new BusinessException(ResultEnum.WALLET_WITHDRAWAL_INSUFFICIENT);
            }
            // 将提现金额存入冻结金额,扣减可提现金额
            distribution.setCanRebate(CurrencyUtils.sub(distribution.getCanRebate(), applyMoney));
            distributionService.updateById(distribution);
            // 提现申请记录
            DistributionCash distributionCash = new DistributionCash(
                    "D" + IdGeneratorUtils.getId(), distribution.getId(), applyMoney, distribution.getMemberName());
            boolean result = this.save(distributionCash);
            if (result) {
                // 发送提现消息
                MemberWithdrawalMessage memberWithdrawalMessage = new MemberWithdrawalMessage();
                memberWithdrawalMessage.setMemberId(distribution.getMemberId());
                memberWithdrawalMessage.setPrice(applyMoney);
                memberWithdrawalMessage.setDestination(MemberWithdrawalDestinationEnum.WALLET.name());
                memberWithdrawalMessage.setStatus(WithdrawStatusEnum.APPLY.name());
                String destination =
                        rocketmqCustomProperties.getMemberTopic() + ":" + MemberTagsEnum.MEMBER_WITHDRAWAL.name();
                rocketMQTemplate.asyncSend(
                        destination, memberWithdrawalMessage, RocketmqSendCallbackBuilder.commonCallback());
                return true;
            }
            return false;
        }
        throw new BusinessException(ResultEnum.DISTRIBUTION_NOT_EXIST);
    }

    @Override
    public IPage<DistributionCash> getDistributionCash(PageVO page) {
        Distribution distribution = distributionService.getDistribution();
        QueryWrapper<DistributionCash> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("distribution_id", distribution.getId());
        return this.page(PageUtil.initPage(page), queryWrapper);
    }

    @Override
    public IPage<DistributionCash> getDistributionCash(DistributionCashSearchVO distributionCashSearchVO) {

        return this.page(PageUtil.initPage(distributionCashSearchVO), distributionCashSearchVO.queryWrapper());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DistributionCash audit(String id, String result) {

        // 检查分销功能开关
        distributionService.checkDistributionSetting();

        // 获取分销佣金信息
        DistributionCash distributorCash = this.getById(id);
        // 只有分销员和分销佣金记录存在的情况才可以审核
        if (distributorCash != null) {
            // 获取分销员
            Distribution distribution = distributionService.getById(distributorCash.getDistributionId());
            if (distribution != null
                    && distribution.getDistributionStatus().equals(DistributionStatusEnum.PASS.name())) {
                MemberWithdrawalMessage memberWithdrawalMessage = new MemberWithdrawalMessage();
                // 审核通过
                if (result.equals(WithdrawStatusEnum.VIA_AUDITING.name())) {
                    memberWithdrawalMessage.setStatus(WithdrawStatusEnum.VIA_AUDITING.name());
                    // 分销记录操作
                    distributorCash.setDistributionCashStatus(WithdrawStatusEnum.VIA_AUDITING.name());
                    distributorCash.setPayTime(new Date());
                    // 提现到余额
                    memberWalletApi.increase(new MemberWalletUpdateDTO(
                            distributorCash.getPrice(),
                            distribution.getMemberId(),
                            "分销[" + distributorCash.getSn() + "]佣金提现到余额[" + distributorCash.getPrice() + "]",
                            DepositServiceTypeEnum.WALLET_COMMISSION.name()));
                } else {
                    memberWithdrawalMessage.setStatus(WithdrawStatusEnum.FAIL_AUDITING.name());
                    // 分销员可提现金额退回
                    distribution.setCanRebate(
                            CurrencyUtils.add(distribution.getCanRebate(), distributorCash.getPrice()));
                    distributorCash.setDistributionCashStatus(WithdrawStatusEnum.FAIL_AUDITING.name());
                }
                // 分销员金额相关处理
                distributionService.updateById(distribution);
                // 修改分销提现申请
                boolean bool = this.updateById(distributorCash);
                if (bool) {
                    // 组织会员提现审核消息
                    memberWithdrawalMessage.setMemberId(distribution.getMemberId());
                    memberWithdrawalMessage.setPrice(distributorCash.getPrice());
                    memberWithdrawalMessage.setDestination(MemberWithdrawalDestinationEnum.WALLET.name());
                    String destination =
                            rocketmqCustomProperties.getMemberTopic() + ":" + MemberTagsEnum.MEMBER_WITHDRAWAL.name();
                    rocketMQTemplate.asyncSend(
                            destination, memberWithdrawalMessage, RocketmqSendCallbackBuilder.commonCallback());
                }
                return distributorCash;
            }
            throw new BusinessException(ResultEnum.DISTRIBUTION_NOT_EXIST);
        }
        throw new BusinessException(ResultEnum.DISTRIBUTION_CASH_NOT_EXIST);
    }
}
