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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.cloud.member.biz.mapper.IMemberAddressMapper;
import com.taotao.cloud.member.biz.model.entity.MemberAddress;
import com.taotao.cloud.member.biz.service.business.IMemberAddressService;
import com.taotao.cloud.sys.api.dubbo.UserRpcService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 收货地址业务层实现
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-05-31 13:55:30
 */
@Service
public class MemberAddressServiceImpl extends ServiceImpl<IMemberAddressMapper, MemberAddress>
        implements IMemberAddressService {

    @DubboReference
    private UserRpcService userRpc;

    @Override
    public IPage<MemberAddress> queryPage(PageQuery page, Long memberId) {
        LambdaQueryWrapper<MemberAddress> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MemberAddress::getMemberId, memberId);
        return this.page(page.buildMpPage(), lambdaQueryWrapper);
    }

    @Override
    public MemberAddress getMemberAddress(Long id) {
        LambdaQueryWrapper<MemberAddress> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MemberAddress::getMemberId, SecurityUtils.getUserId());
        lambdaQueryWrapper.eq(MemberAddress::getId, id);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public MemberAddress getDefaultMemberAddress() {
        LambdaQueryWrapper<MemberAddress> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MemberAddress::getMemberId, SecurityUtils.getUserId());
        lambdaQueryWrapper.eq(MemberAddress::getDefaulted, true);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveMemberAddress(MemberAddress memberAddress) {
        // 判断当前地址是否为默认地址，如果为默认需要将其他的地址修改为非默认
        removeDefaultAddress(memberAddress);

        // 添加会员地址
        return this.save(memberAddress);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateMemberAddress(MemberAddress memberAddress) {
        MemberAddress originalMemberAddress = this.getMemberAddress(memberAddress.getId());

        if (originalMemberAddress != null && originalMemberAddress.getMemberId().equals(SecurityUtils.getUserId())) {
            if (memberAddress.getDefaulted() == null) {
                memberAddress.setDefaulted(false);
            }

            // 判断当前地址是否为默认地址，如果为默认需要将其他的地址修改为非默认
            removeDefaultAddress(memberAddress);
            this.saveOrUpdate(memberAddress);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeMemberAddress(Long id) {
        LambdaQueryWrapper<MemberAddress> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MemberAddress::getId, id);
        return this.remove(lambdaQueryWrapper);
    }

    /**
     * 修改会员默认收件地址
     *
     * @param memberAddress 收件地址
     */
    private void removeDefaultAddress(MemberAddress memberAddress) {
        // 如果不是默认地址不需要处理
        if (Boolean.TRUE.equals(memberAddress.getDefaulted())) {
            // 将会员的地址修改为非默认地址
            LambdaUpdateWrapper<MemberAddress> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
            lambdaUpdateWrapper.set(MemberAddress::getDefaulted, false);
            lambdaUpdateWrapper.eq(MemberAddress::getMemberId, memberAddress.getMemberId());
            this.update(lambdaUpdateWrapper);
        }
    }
}
