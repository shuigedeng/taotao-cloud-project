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

import org.dromara.hutoolcore.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.enums.SwitchEnum;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.cloud.message.api.enums.MessageStatusEnum;
import com.taotao.cloud.message.api.enums.NoticeMessageParameterEnum;
import com.taotao.cloud.message.api.model.dto.NoticeMessageDTO;
import com.taotao.cloud.message.biz.mapper.NoticeMessageTemplateMapper;
import com.taotao.cloud.message.biz.model.entity.MemberMessage;
import com.taotao.cloud.message.biz.model.entity.NoticeMessage;
import com.taotao.cloud.message.biz.service.business.MemberMessageService;
import com.taotao.cloud.message.biz.service.business.NoticeMessageService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 通知类消息模板业务层实现 */
@Service
public class NoticeMessageServiceImpl extends ServiceImpl<NoticeMessageTemplateMapper, NoticeMessage>
        implements NoticeMessageService {

    @Autowired
    private MemberMessageService memberMessageService;

    @Override
    public IPage<NoticeMessage> getMessageTemplate(PageQuery PageQuery, String type) {
        // 构建查询参数
        QueryWrapper<NoticeMessage> messageTemplateQueryWrapper = new QueryWrapper<>();
        // 消息模板类型
        messageTemplateQueryWrapper.eq(!CharSequenceUtil.isEmpty(type), "type", type);
        messageTemplateQueryWrapper.orderByDesc("create_time");
        // 查询数据返回
        // return this.page(PageUtil.initPage(PageQuery), messageTemplateQueryWrapper);
        return null;
    }

    @Override
    public void noticeMessage(NoticeMessageDTO noticeMessageDTO) {
        if (noticeMessageDTO == null) {
            return;
        }
        try {
            NoticeMessage noticeMessage = this.getOne(new LambdaQueryWrapper<NoticeMessage>()
                    .eq(
                            NoticeMessage::getNoticeNode,
                            noticeMessageDTO
                                    .getNoticeMessageNodeEnum()
                                    .getDescription()
                                    .trim()));
            // 如果通知类站内信开启的情况下
            if (noticeMessage != null && noticeMessage.getNoticeStatus().equals(SwitchEnum.OPEN.name())) {
                MemberMessage memberMessage = new MemberMessage();
                // memberMessage.setMemberId(noticeMessageDTO.getMemberId());
                memberMessage.setTitle(noticeMessage.getNoticeTitle());
                memberMessage.setContent(noticeMessage.getNoticeContent());
                // 参数不为空，替换内容
                if (noticeMessageDTO.getParameter() != null) {
                    memberMessage.setContent(
                            replaceNoticeContent(noticeMessage.getNoticeContent(), noticeMessageDTO.getParameter()));
                } else {
                    memberMessage.setContent(noticeMessage.getNoticeContent());
                }
                memberMessage.setStatus(MessageStatusEnum.UN_READY.name());
                // 添加站内信
                memberMessageService.save(memberMessage);
            }
        } catch (Exception e) {
            log.error("站内信发送失败：", e);
        }
    }

    /**
     * 替换站内信内容
     *
     * @param noticeContent 站内信内容
     * @param parameter 参数
     * @return 替换后站内信内容
     */
    String replaceNoticeContent(String noticeContent, Map<String, String> parameter) {
        for (Map.Entry<String, String> entry : parameter.entrySet()) {
            String description = NoticeMessageParameterEnum.getValueByType(entry.getKey());
            if (description != null && entry.getValue() != null) {
                noticeContent = noticeContent.replace("#{" + description + "}".trim(), entry.getValue());
            }
        }
        return noticeContent;
    }
}
