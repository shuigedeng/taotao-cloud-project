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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.boot.common.utils.lang.StringUtils;
import com.taotao.cloud.member.sys.model.vo.MemberPointsHistoryVO;
import com.taotao.cloud.member.biz.mapper.IMemberPointsHistoryMapper;
import com.taotao.cloud.member.biz.model.entity.Member;
import com.taotao.cloud.member.biz.model.entity.MemberPointsHistory;
import com.taotao.cloud.member.biz.service.business.IMemberPointsHistoryService;
import com.taotao.cloud.member.biz.service.business.IMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会员积分历史业务层实现
 *
 * @author shuigedeng
 * @version 2023.01
 * @since 2023-02-01 13:48:04
 */
@Service
public class MemberPointsHistoryServiceImpl extends ServiceImpl<IMemberPointsHistoryMapper, MemberPointsHistory>
        implements IMemberPointsHistoryService {

    @Autowired
    private IMemberService memberService;

    @Override
    public MemberPointsHistoryVO getMemberPointsHistoryVO(Long memberId) {
        // 获取会员积分历史
        Member member = memberService.getById(memberId);

        MemberPointsHistoryVO memberPointsHistoryVO = new MemberPointsHistoryVO();
        if (member != null) {
            memberPointsHistoryVO.setPoint(member.getPoint());
            memberPointsHistoryVO.setTotalPoint(member.getTotalPoint());
            return memberPointsHistoryVO;
        }
        return new MemberPointsHistoryVO();
    }

    @Override
    public IPage<MemberPointsHistory> pageQuery(PageQuery pageQuery) {
        LambdaQueryWrapper<MemberPointsHistory> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MemberPointsHistory::getMemberId, SecurityUtils.getUserId());
        queryWrapper.orderByDesc(MemberPointsHistory::getCreateTime);
        return this.page(pageQuery.buildMpPage(), queryWrapper);
    }

    @Override
    public IPage<MemberPointsHistory> memberPointsHistoryPageQuery(
            PageQuery pageQuery, Long memberId, String memberName) {
        LambdaQueryWrapper<MemberPointsHistory> lambdaQueryWrapper = new LambdaQueryWrapper<MemberPointsHistory>()
                .eq(memberId != null, MemberPointsHistory::getMemberId, memberId)
                .like(memberName != null, MemberPointsHistory::getMemberName, memberName);

        // 如果排序为空，则默认创建时间倒序
        if (StringUtils.isNotBlank(pageQuery.getSort())) {
            pageQuery.setSort("createTime");
            pageQuery.setOrder("desc");
        }
        return this.page(pageQuery.buildMpPage(), lambdaQueryWrapper);
    }
}
