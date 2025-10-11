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

package com.taotao.cloud.message.biz.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.boot.common.utils.lang.StringUtils;
import com.taotao.cloud.message.api.model.vo.MemberMessageQueryVO;
import com.taotao.cloud.message.biz.mapper.MemberMessageMapper;
import com.taotao.cloud.message.biz.model.entity.MemberMessage;
import com.taotao.cloud.message.biz.service.business.MemberMessageService;
import java.util.List;
import org.springframework.stereotype.Service;

/** 会员接收消息业务层实现 */
@Service
public class MemberMessageServiceImpl extends ServiceImpl<MemberMessageMapper, MemberMessage>
        implements MemberMessageService {

    @Override
    public IPage<MemberMessage> getPage(MemberMessageQueryVO memberMessageQueryVO, PageQuery PageQuery) {
        QueryWrapper<MemberMessage> queryWrapper = new QueryWrapper<>();
        // 消息id
        queryWrapper.eq(
                StringUtils.isNotEmpty(memberMessageQueryVO.getMessageId()),
                "message_id",
                memberMessageQueryVO.getMessageId());
        // 消息标题
        queryWrapper.like(
                StringUtils.isNotEmpty(memberMessageQueryVO.getTitle()), "title", memberMessageQueryVO.getTitle());
        // 会员id
        queryWrapper.eq(
                StringUtils.isNotEmpty(memberMessageQueryVO.getMemberId()),
                "member_id",
                memberMessageQueryVO.getMemberId());
        // 消息状态
        queryWrapper.eq(
                StringUtils.isNotEmpty(memberMessageQueryVO.getStatus()), "status", memberMessageQueryVO.getStatus());
        // 倒序
        queryWrapper.orderByDesc("create_time");
        // 构建查询
        // return this.page(PageUtil.initPage(PageQuery), queryWrapper);
        return null;
    }

    @Override
    public Boolean editStatus(String status, String messageId) {
        // 查询消息是否存在
        MemberMessage memberMessage = this.getById(messageId);
        if (memberMessage != null) {
            memberMessage.setStatus(status);
            // 执行修改
            return this.updateById(memberMessage);
        }
        return false;
    }

    @Override
    public Boolean deleteMessage(String messageId) {
        // 查询消息是否存在
        MemberMessage memberMessage = this.getById(messageId);
        if (memberMessage != null) {
            // 执行删除
            return this.removeById(memberMessage);
        }
        return false;
    }

    @Override
    public Boolean save(List<MemberMessage> messages) {
        return saveBatch(messages);
    }
}
