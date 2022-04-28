package com.taotao.cloud.message.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.message.biz.entity.WechatMPMessage;
import org.apache.ibatis.annotations.Delete;

/**
 * 微信小程序消息订阅 Dao层
 */
public interface WechatMPMessageMapper extends BaseMapper<WechatMPMessage> {

	/**
	 * 删除微信服务消息
	 */
	@Delete("delete from tt_wechat_mp_message")
	void deleteAll();
}
