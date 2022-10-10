package com.taotao.cloud.message.biz.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.message.biz.model.entity.WechatMPMessage;

/**
 * 微信小程序消息订阅 业务层
 */
public interface WechatMPMessageService extends IService<WechatMPMessage> {

	/**
	 * 初始化微信消息订阅模版
	 */
	void init();
}
