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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.cloud.message.api.model.vo.MessageVO;
import com.taotao.cloud.message.biz.mapper.MessageMapper;
import com.taotao.cloud.message.biz.model.entity.Message;
import com.taotao.cloud.message.biz.service.business.MessageService;
import com.taotao.cloud.stream.framework.rocketmq.RocketmqSendCallbackBuilder;
import com.taotao.cloud.stream.framework.rocketmq.tags.OtherTagsEnum;
import com.taotao.cloud.stream.properties.RocketmqCustomProperties;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 管理端发送消息内容业务层实现 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private RocketmqCustomProperties rocketmqCustomProperties;

    @Override
    public IPage<Message> getPage(MessageVO messageVO, PageQuery PageQuery) {
        // LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
        // if (StrUtil.isNotEmpty(title)) {
        // 	queryWrapper.like(Message::getTitle, title);
        // }
        // if (StrUtil.isNotEmpty(content)) {
        // 	queryWrapper.like(Message::getContent, content);
        // }
        // queryWrapper.orderByDesc(Message::getCreateTime);
        //
        // return this.page(PageUtil.initPage(PageQuery), queryWrapper);
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean sendMessage(Message message) {
        // 保存站内信信息
        this.save(message);
        // 发送站内信消息提醒
        String noticeSendDestination =
                rocketmqCustomProperties.getNoticeSendTopic() + ":" + OtherTagsEnum.MESSAGE.name();
        rocketMQTemplate.asyncSend(noticeSendDestination, message, RocketmqSendCallbackBuilder.commonCallback());
        return true;
    }

    @Override
    public Boolean deleteMessage(String id) {
        // 只有查询到此记录才真实删除，未找到记录则直接返回true即可
        Message message = this.getById(id);
        if (message != null) {
            return this.removeById(id);
        }
        return true;
    }
}
