package com.taotao.cloud.message.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.message.biz.entity.WechatMessage;

/**
 * 微信消息 业务层
 */
public interface WechatMessageService extends IService<WechatMessage> {

	/**
	 * 初始化微信消息模版
	 */
	void init();
}
