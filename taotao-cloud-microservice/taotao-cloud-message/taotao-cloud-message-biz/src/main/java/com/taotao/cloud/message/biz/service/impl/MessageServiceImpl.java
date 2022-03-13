package com.taotao.cloud.message.biz.service.impl;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.message.api.vo.MessageVO;
import com.taotao.cloud.message.biz.entity.Message;
import com.taotao.cloud.message.biz.mapper.MessageMapper;
import com.taotao.cloud.message.biz.service.MessageService;
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
	public IPage<Message> getPage(MessageVO messageVO, PageVO pageVO) {
		return this.page(PageUtil.initPage(pageVO), messageVO.lambdaQueryWrapper());
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
