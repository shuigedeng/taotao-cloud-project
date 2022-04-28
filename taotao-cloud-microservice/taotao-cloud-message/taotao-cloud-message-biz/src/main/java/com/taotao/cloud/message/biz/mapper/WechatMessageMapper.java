package com.taotao.cloud.message.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.message.biz.entity.WechatMessage;
import org.apache.ibatis.annotations.Delete;

/**
 * 微信消息 Dao层
 */
public interface WechatMessageMapper extends BaseMapper<WechatMessage> {

	/**
	 * 删除微信消息
	 */
	@Delete("delete from tt_wechat_message")
	void deleteAll();
}
