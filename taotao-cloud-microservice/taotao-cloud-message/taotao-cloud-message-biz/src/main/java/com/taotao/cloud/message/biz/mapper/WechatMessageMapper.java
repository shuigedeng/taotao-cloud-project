package com.taotao.cloud.message.biz.mapper;

import com.taotao.cloud.message.biz.model.entity.WechatMessage;
import com.taotao.cloud.web.base.mapper.BaseSuperMapper;
import org.apache.ibatis.annotations.Delete;

/**
 * 微信消息 Dao层
 */
public interface WechatMessageMapper extends BaseSuperMapper<WechatMessage, Long> {

	/**
	 * 删除微信消息
	 */
	@Delete("delete from tt_wechat_message")
	void deleteAll();
}
