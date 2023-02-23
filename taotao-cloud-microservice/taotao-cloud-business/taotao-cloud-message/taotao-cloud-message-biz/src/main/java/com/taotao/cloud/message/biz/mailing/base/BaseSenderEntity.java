
package com.taotao.cloud.message.biz.mailing.base;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

/**
 *  基础发送者实体
 */
@MappedSuperclass
public abstract class BaseSenderEntity extends BaseEntity {

	@Schema(name = "发送人ID")
	@Column(name = "sender_id", length = 64)
	private String senderId;

	@Schema(name = "发送人名称", title = "冗余信息，增加该字段减少重复查询")
	@Column(name = "sender_name", length = 50)
	private String senderName;

	@Schema(name = "发送人头像")
	@Column(name = "sender_avatar", length = 1000)
	private String senderAvatar;

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderAvatar() {
		return senderAvatar;
	}

	public void setSenderAvatar(String senderAvatar) {
		this.senderAvatar = senderAvatar;
	}
}
