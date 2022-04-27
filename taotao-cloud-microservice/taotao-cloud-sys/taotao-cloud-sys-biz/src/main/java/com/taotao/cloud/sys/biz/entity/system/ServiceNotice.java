package com.taotao.cloud.sys.biz.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;


/**
 * 服务订阅消息
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = ServiceNotice.TABLE_NAME)
@TableName(ServiceNotice.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = ServiceNotice.TABLE_NAME, comment = "服务订阅消息表")
public class ServiceNotice extends BaseSuperEntity<ServiceNotice, Long> {

	public static final String TABLE_NAME = "tt_service_notice";

	@Column(name = "store_id", columnDefinition = "varchar(255) not null default '' comment '商家id，为-1时，代表是平台发布的消息'")
	private String storeId;

	@Column(name = "banner_image", columnDefinition = "varchar(255) not null default '' comment 'banner图'")
	private String bannerImage;

	@Column(name = "title", columnDefinition = "varchar(255) not null default '' comment '标题'")
	private String title;

	@Column(name = "sub_title", columnDefinition = "varchar(255) not null default '' comment '副标题'")
	private String subTitle;

	@Column(name = "to_url", columnDefinition = "varchar(255) not null default '' comment '点击跳转（此内容与站内信内容只能有一个生效）'")
	private String toUrl;

	@Column(name = "content", columnDefinition = "varchar(255) not null default '' comment '站内信内容(富文本框编辑，可以上传图片的html)'")
	private String content;

	@Override
	public boolean equals(Object o) {
				if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		ServiceNotice that = (ServiceNotice) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
