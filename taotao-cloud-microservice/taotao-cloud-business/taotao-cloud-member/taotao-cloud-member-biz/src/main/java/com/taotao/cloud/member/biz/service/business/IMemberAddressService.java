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

package com.taotao.cloud.member.biz.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.cloud.member.biz.model.entity.MemberAddress;

/** 收货地址业务层 */
public interface IMemberAddressService extends IService<MemberAddress> {

    /**
     * 根据会员获取会员地址分页列表
     *
     * @param page 分页条件
     * @param memberId 会员ID
     * @return 会员地址分页列表
     */
    IPage<MemberAddress> queryPage(PageQuery page, Long memberId);

    /**
     * 根据地址ID获取当前会员地址信息
     *
     * @param id 地址ID
     * @return 当前会员的地址信息
     */
    MemberAddress getMemberAddress(Long id);

    /**
     * 根据地址ID获取当前会员地址信息
     *
     * @return 当前会员的地址信息
     */
    MemberAddress getDefaultMemberAddress();

    /**
     * 添加会员收货地址
     *
     * @param memberAddress 收货地址
     * @return 操作状态
     */
    Boolean saveMemberAddress(MemberAddress memberAddress);

    /**
     * 修改会员收货地址信息
     *
     * @param memberAddress 收货地址
     * @return 操作状态
     */
    Boolean updateMemberAddress(MemberAddress memberAddress);

    /**
     * 删除会员收货地址信息
     *
     * @param id 收货地址ID
     * @return 操作状态
     */
    Boolean removeMemberAddress(Long id);
}
