package com.taotao.cloud.message.biz.mapper;

import com.taotao.cloud.message.biz.entity.WechatMPMessage;
import com.taotao.cloud.web.base.mapper.BaseSuperMapper;
import org.apache.ibatis.annotations.Delete;

/**
 * 微信小程序消息订阅 Dao层
 */
public interface WechatMPMessageMapper extends BaseSuperMapper<WechatMPMessage, Long> {

	/**
	 * 删除微信服务消息
	 */
	@Delete("delete from tt_wechat_mp_message")
	void deleteAll();
}
