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

package com.taotao.cloud.member.biz.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.security.spring.model.SecurityUser;
import com.taotao.boot.common.utils.id.IdGeneratorUtils;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.cloud.member.api.enums.DepositServiceTypeEnum;
import com.taotao.cloud.member.api.feign.MemberWalletApi;
import com.taotao.cloud.member.sys.model.dto.MemberWalletUpdateDTO;
import com.taotao.cloud.member.biz.mapper.IMemberRechargeMapper;
import com.taotao.cloud.member.biz.model.entity.MemberRecharge;
import com.taotao.cloud.member.biz.service.business.IMemberRechargeService;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import com.taotao.cloud.order.api.model.page.recharge.RechargePageQuery;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 预存款业务层实现
 *
 * @author shuigedeng
 * @version 2023.01
 * @since 2023-02-01 13:48:28
 */
@Service
public class MemberRechargeServiceImpl extends ServiceImpl<IMemberRechargeMapper, MemberRecharge>
        implements IMemberRechargeService {

    /** 会员预存款 */
    @Autowired
    private MemberWalletApi feignMemberWalletApi;

    @Override
    public MemberRecharge recharge(BigDecimal price) {
        // 获取当前登录的会员
        SecurityUser authUser = SecurityUtils.getCurrentUser();
        // 构建sn
        String sn = "Y" + IdGeneratorUtils.getId();
        // 整合充值订单数据
        MemberRecharge recharge = new MemberRecharge(sn, authUser.getUserId(), authUser.getUsername(), price);
        // 添加预存款充值账单
        this.save(recharge);
        // 返回预存款
        return recharge;
    }

    @Override
    public IPage<MemberRecharge> rechargePage(RechargePageQuery rechargePageQuery) {
        // 构建查询条件
        QueryWrapper<MemberRecharge> queryWrapper = new QueryWrapper<>();
        // 会员名称
        queryWrapper.like(
                !CharSequenceUtil.isEmpty(rechargePageQuery.getMemberName()),
                "member_name",
                rechargePageQuery.getMemberName());
        // 充值订单号
        queryWrapper.eq(
                !CharSequenceUtil.isEmpty(rechargePageQuery.getRechargeSn()),
                "recharge_sn",
                rechargePageQuery.getRechargeSn());
        // 会员id
        queryWrapper.eq(
                !CharSequenceUtil.isEmpty(rechargePageQuery.getMemberId()),
                "member_id",
                rechargePageQuery.getMemberId());
        // 支付时间 开始时间和结束时间
        if (!CharSequenceUtil.isEmpty(rechargePageQuery.getStartDate())
                && !CharSequenceUtil.isEmpty(rechargePageQuery.getEndDate())) {
            Date start = DateUtil.parse(rechargePageQuery.getStartDate());
            Date end = date.DateUtil.parse(rechargePageQuery.getEndDate());
            queryWrapper.between("pay_time", start, end);
        }
        queryWrapper.orderByDesc("create_time");
        // 查询返回数据
        return this.page(rechargePageQuery.buildMpPage(), queryWrapper);
    }

    @Override
    public void paySuccess(String sn, String receivableNo, String paymentMethod) {
        // 根据sn获取支付账单
        MemberRecharge recharge = this.getOne(new QueryWrapper<MemberRecharge>().eq("recharge_sn", sn));
        // 如果支付账单不为空则进行一下逻辑
        if (recharge != null && !recharge.getPayStatus().equals(PayStatusEnum.PAID.name())) {
            // 将此账单支付状态更改为已支付
            recharge.setPayStatus(PayStatusEnum.PAID.name());
            recharge.setReceivableNo(receivableNo);
            recharge.setPayTime(LocalDateTime.now());
            recharge.setRechargeWay(paymentMethod);
            // 执行保存操作
            this.updateById(recharge);
            // 增加预存款余额
            feignMemberWalletApi.increase(new MemberWalletUpdateDTO(
                    recharge.getRechargeMoney(),
                    recharge.getMemberId(),
                    "会员余额充值，充值单号为：" + recharge.getRechargeSn(),
                    DepositServiceTypeEnum.WALLET_RECHARGE.name()));
        }
    }

    @Override
    public MemberRecharge getRecharge(String sn) {
        MemberRecharge recharge = this.getOne(new QueryWrapper<MemberRecharge>().eq("recharge_sn", sn));
        if (recharge != null) {
            return recharge;
        }
        throw new BusinessException(ResultEnum.ORDER_NOT_EXIST);
    }

    @Override
    public void rechargeOrderCancel(String sn) {
        MemberRecharge recharge = this.getOne(new QueryWrapper<MemberRecharge>().eq("recharge_sn", sn));
        if (recharge != null) {
            recharge.setPayStatus(PayStatusEnum.CANCEL.name());
            this.updateById(recharge);
        }
    }
}
