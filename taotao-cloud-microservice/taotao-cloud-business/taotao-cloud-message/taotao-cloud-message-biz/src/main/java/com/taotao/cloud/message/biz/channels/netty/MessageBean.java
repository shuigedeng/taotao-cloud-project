package com.taotao.cloud.message.biz.channels.netty;

import lombok.*;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageBean {
	/**
	 * 数据长度
	 */
	private Integer len;
	/**
	 * 通讯数据
	 */
	private byte[] content;

	public MessageBean(Object object) {
		content = JSONUtil.toJsonStr(object).getBytes(StandardCharsets.UTF_8);
		len = content.length;
	}
}

