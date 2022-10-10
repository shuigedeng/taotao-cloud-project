package com.taotao.cloud.message.biz.service.business.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.model.PageParam;
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

/**
 * 管理端发送消息内容业务层实现
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements
	MessageService {

	@Autowired
	private RocketMQTemplate rocketMQTemplate;
	@Autowired
	private RocketmqCustomProperties rocketmqCustomProperties;


	@Override
	public IPage<Message> getPage(MessageVO messageVO, PageParam PageParam) {
		// LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
		// if (StrUtil.isNotEmpty(title)) {
		// 	queryWrapper.like(Message::getTitle, title);
		// }
		// if (StrUtil.isNotEmpty(content)) {
		// 	queryWrapper.like(Message::getContent, content);
		// }
		// queryWrapper.orderByDesc(Message::getCreateTime);
		//
		// return this.page(PageUtil.initPage(PageParam), queryWrapper);
		return null;
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean sendMessage(Message message) {
		//保存站内信信息
		this.save(message);
		//发送站内信消息提醒
		String noticeSendDestination =
			rocketmqCustomProperties.getNoticeSendTopic() + ":" + OtherTagsEnum.MESSAGE.name();
		rocketMQTemplate.asyncSend(noticeSendDestination, message,
			RocketmqSendCallbackBuilder.commonCallback());
		return true;
	}

	@Override
	public Boolean deleteMessage(String id) {
		//只有查询到此记录才真实删除，未找到记录则直接返回true即可
		Message message = this.getById(id);
		if (message != null) {
			return this.removeById(id);
		}
		return true;
	}
}
