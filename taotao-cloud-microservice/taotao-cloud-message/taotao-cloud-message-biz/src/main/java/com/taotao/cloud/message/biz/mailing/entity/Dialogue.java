
package com.taotao.cloud.message.biz.mailing.entity;
import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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
 *  私信对话
 * <p>
 * 本质是一张冗余表，作为中间桥梁连接私信联系和私信对话详情。同时保存对话的最新一条信息，方便展示。
 */
@Schema(name = "私信对话")
@Entity
@Table(name = "msg_dialogue", indexes = {
	@Index(name = "msg_dialogue_id_idx", columnList = "dialogue_id")})
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = MessageConstants.REGION_MESSAGE_DIALOGUE)
public class Dialogue extends BaseEntity {

	@Schema(name = "对话ID")
	@Id
	@UuidGenerator
	@Column(name = "dialogue_id", length = 64)
	private String dialogueId;

	@Schema(name = "最新内容")
	@Column(name = "latest_news", columnDefinition = "TEXT")
	private String latestNews;

	public String getDialogueId() {
		return dialogueId;
	}

	public void setDialogueId(String dialogueId) {
		this.dialogueId = dialogueId;
	}

	public String getLatestNews() {
		return latestNews;
	}

	public void setLatestNews(String latestNews) {
		this.latestNews = latestNews;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("dialogueId", dialogueId)
			.add("latestNews", latestNews)
			.toString();
	}
}
