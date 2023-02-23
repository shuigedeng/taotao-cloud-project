
package com.taotao.cloud.message.biz.mailing.entity;

import com.google.common.base.MoreObjects;
import com.taotao.cloud.message.biz.mailing.base.BaseSenderEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
/**
 *  私信联系
 * <p>
 * 私信双相关系存储。
 */
@Schema(name = "私信联系")
@Entity
@Table(name = "msg_dialogue_contact", indexes = {
	@Index(name = "msg_dialogue_contact_id_idx", columnList = "contact_id"),
	@Index(name = "msg_dialogue_contact_sid_idx", columnList = "sender_id"),
})
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = MessageConstants.REGION_MESSAGE_DIALOGUE_CONTACT)
public class DialogueContact extends BaseSenderEntity {

	@Schema(name = "联系ID")
	@Id
	@UuidGenerator
	@Column(name = "contact_id", length = 64)
	private String contactId;

	@Schema(name = "接收人ID")
	@Column(name = "receiver_id", length = 64)
	private String receiverId;

	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = MessageConstants.REGION_MESSAGE_DIALOGUE)
	@Schema(title = "对话ID")
	@ManyToOne
	@JoinColumn(name = "dialogue_id", nullable = false)
	private Dialogue dialogue;

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public Dialogue getDialogue() {
		return dialogue;
	}

	public void setDialogue(Dialogue dialogue) {
		this.dialogue = dialogue;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("contactId", contactId)
			.add("receiverId", receiverId)
			.add("dialogue", dialogue)
			.toString();
	}
}
