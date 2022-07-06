/*
 * Copyright (c) ©2015-2021 Jaemon. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.dingtalk.enums;


import static com.taotao.cloud.dingtalk.model.DingerDefinitionHandler.WETALK_AT_ALL;
import static com.taotao.cloud.dingtalk.enums.ExceptionEnum.DINGER_UNSUPPORT_MESSAGE_TYPE_EXCEPTION;

import com.taotao.cloud.dingtalk.entity.DingerRequest;
import com.taotao.cloud.dingtalk.entity.MsgType;
import com.taotao.cloud.dingtalk.entity.DingFeedCard;
import com.taotao.cloud.dingtalk.entity.DingLink;
import com.taotao.cloud.dingtalk.entity.DingMarkDown;
import com.taotao.cloud.dingtalk.entity.DingText;
import com.taotao.cloud.dingtalk.entity.Message;
import com.taotao.cloud.dingtalk.exception.DingerException;
import com.taotao.cloud.dingtalk.entity.WeMarkdown;
import com.taotao.cloud.dingtalk.entity.WeNews;
import com.taotao.cloud.dingtalk.entity.WeText;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * 消息体定义子类型
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 15:21:06
 */
public enum MessageSubType {
	/**
	 * Text类型
	 */
	TEXT(true) {
		@Override
		public MsgType msgType(DingerType dingerType, DingerRequest request) {
			String content = request.getContent();
			boolean atAll = request.isAtAll();
			List<String> phones = request.getPhones();
			if (dingerType == DingerType.DINGTALK) {
				Message message = new DingText(new DingText.Text(content));

				if (atAll) {
					message.setAt(new Message.At(true));
				} else if (phones != null && !phones.isEmpty()) {
					message.setAt(new Message.At(phones));
				}

				return message;
			} else {
				WeText.Text text = new WeText.Text(content);
				WeText weText = new WeText(text);
				if (atAll) {
					text.setMentioned_mobile_list(List.of(WETALK_AT_ALL));
				} else if (phones != null && !phones.isEmpty()) {
					text.setMentioned_mobile_list(phones);
				}
				return weText;
			}
		}
	},

	/**
	 * Markdown类型
	 */
	MARKDOWN(true) {
		@Override
		public MsgType msgType(DingerType dingerType, DingerRequest request) {
			String content = request.getContent();
			String title = request.getTitle();
			List<String> phones = request.getPhones();
			if (dingerType == DingerType.DINGTALK) {
				Message message = new DingMarkDown(new DingMarkDown.MarkDown(title, content));

				if (!phones.isEmpty()) {
					message.setAt(new Message.At(phones));
				}

				return message;
			} else {
				WeMarkdown.Markdown markdown = new WeMarkdown.Markdown(content);
				WeMarkdown weMarkdown = new WeMarkdown(markdown);
				return weMarkdown;
			}
		}
	},

	/**
	 * 图文类型
	 */
	IMAGETEXT(false) {
		@Override
		public MsgType msgType(DingerType dingerType, DingerRequest request) {
			if (dingerType == DingerType.DINGTALK) {
				return new DingFeedCard(new ArrayList<>());
			} else {
				return new WeNews(new ArrayList<>());
			}
		}
	},

	/**
	 * link类型, 只支持 {@link DingerType#DINGTALK}
	 */
	LINK(false) {
		@Override
		public MsgType msgType(DingerType dingerType, DingerRequest request) {
			if (dingerType == DingerType.DINGTALK) {
				return new DingLink();
			} else {
				throw new DingerException(DINGER_UNSUPPORT_MESSAGE_TYPE_EXCEPTION, dingerType,
					this.name());
			}
		}
	};

	/**
	 * 是否支持显示设置消息子类型调用
	 */
	private boolean support;

	MessageSubType(boolean support) {
		this.support = support;
	}

	public boolean isSupport() {
		return support;
	}

	/**
	 * 获取指定消息类型
	 *
	 * @param dingerType Dinger类型 {@link DingerType}
	 * @param request    消息请求体 {@link  DingerRequest}
	 * @return 消息体 {@link MsgType}
	 */
	public abstract MsgType msgType(DingerType dingerType, DingerRequest request);

	public static boolean contains(String value) {
		return Arrays.stream(MessageSubType.values()).filter(e -> Objects.equals(e.name(), value))
			.count() > 0;
	}
}
