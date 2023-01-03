
package com.taotao.cloud.message.biz.mailing.entity;

import com.google.common.base.MoreObjects;
import com.taotao.cloud.message.biz.mailing.base.BaseSenderEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;

/**
 *  站内通知
 */
@Schema(name = "系统公告")
@Entity
@Table(name = "msg_announcement", indexes = {
	@Index(name = "msg_announcement_id_idx", columnList = "announcement_id")})
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = MessageConstants.REGION_MESSAGE_ANNOUNCEMENT)
public class Announcement extends BaseSenderEntity {

	@Schema(name = "公告ID")
	@Id
	@UuidGenerator
	@Column(name = "announcement_id", length = 64)
	private String announcementId;

	@Schema(name = "公告标题")
	@Column(name = "title", length = 128)
	private String title;

	@Schema(name = "公告内容")
	@Column(name = "content", columnDefinition = "TEXT")
	private String content;

	public String getAnnouncementId() {
		return announcementId;
	}

	public void setAnnouncementId(String announcementId) {
		this.announcementId = announcementId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("announcementId", announcementId)
			.add("title", title)
			.add("content", content)
			.toString();
	}
}
