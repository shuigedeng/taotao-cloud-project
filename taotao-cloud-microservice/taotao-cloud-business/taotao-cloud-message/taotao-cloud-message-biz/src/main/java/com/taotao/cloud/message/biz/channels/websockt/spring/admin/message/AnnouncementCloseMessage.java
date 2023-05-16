package com.taotao.cloud.message.biz.channels.websockt.spring.admin.message;

import com.taotao.cloud.websocket.ballcat.common.websocket.message.JsonWebSocketMessage;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Hccake 2021/1/7
 * @version 1.0
 */
@Getter
@Setter
public class AnnouncementCloseMessage extends JsonWebSocketMessage {

	public AnnouncementCloseMessage() {
		super("announcement-close");
	}

	/**
	 * ID
	 */
	private Long id;

}
