
package com.taotao.cloud.message.biz.mailing.entity;

import com.google.common.base.MoreObjects;
import com.taotao.cloud.message.biz.mailing.base.BaseSenderEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
/**
 *  私信对话详情
 */
@Schema(name = "私信对话详情")
@Entity
@Table(name = "msg_dialogue_detail", indexes = {
	@Index(name = "msg_dialogue_detail_id_idx", columnList = "detail_id"),
	@Index(name = "msg_dialogue_detail_sid_idx", columnList = "sender_id"),
	@Index(name = "msg_dialogue_detail_rid_idx", columnList = "receiver_id"),
	@Index(name = "msg_dialogue_detail_did_idx", columnList = "dialogue_id")
})
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = MessageConstants.REGION_MESSAGE_DIALOGUE_DETAIL)
public class DialogueDetail extends BaseSenderEntity {

	@Schema(name = "对话详情ID")
	@Id
	@UuidGenerator
	@Column(name = "detail_id", length = 64)
	private String detailId;

	@Schema(name = "接收人ID")
	@Column(name = "receiver_id", length = 64)
	private String receiverId;

	@Schema(name = "接收人名称", title = "冗余信息，增加该字段减少重复查询")
	@Column(name = "receiver_name", length = 50)
	private String receiverName;

	@Schema(name = "发送人头像")
	@Column(name = "receiver_avatar", length = 1000)
	private String receiverAvatar;

	@Schema(name = "公告内容")
	@Column(name = "content", columnDefinition = "TEXT")
	private String content;

	@Schema(name = "对话ID")
	@Column(name = "dialogue_id", length = 64)
	private String dialogueId;

	public String getDetailId() {
		return detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverAvatar() {
		return receiverAvatar;
	}

	public void setReceiverAvatar(String receiverAvatar) {
		this.receiverAvatar = receiverAvatar;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDialogueId() {
		return dialogueId;
	}

	public void setDialogueId(String dialogueId) {
		this.dialogueId = dialogueId;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("detailId", detailId)
			.add("receiverId", receiverId)
			.add("receiverName", receiverName)
			.add("receiverAvatar", receiverAvatar)
			.add("content", content)
			.add("dialogueId", dialogueId)
			.toString();
	}
}
