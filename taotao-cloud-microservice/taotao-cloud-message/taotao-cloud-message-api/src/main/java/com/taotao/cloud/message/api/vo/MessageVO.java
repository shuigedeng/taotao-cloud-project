package com.taotao.cloud.message.api.vo;

import cn.hutool.core.util.StrUtil;
import cn.lili.modules.message.entity.dos.Message;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 消息
 */
@Data
@Schema(description = "消息")
public class MessageVO {

	private static final long serialVersionUID = 1L;

	@Schema(description = "标题")
	private String title;

	@Schema(description = "内容")
	private String content;

	public LambdaQueryWrapper<Message> lambdaQueryWrapper() {
		LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
		if (StrUtil.isNotEmpty(title)) {
			queryWrapper.like(Message::getTitle, title);
		}
		if (StrUtil.isNotEmpty(content)) {
			queryWrapper.like(Message::getContent, content);
		}
		queryWrapper.orderByDesc(Message::getCreateTime);
		return queryWrapper;
	}
}
