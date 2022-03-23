package com.taotao.cloud.sys.biz.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 服务订阅消息
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = ServiceNotice.TABLE_NAME)
@TableName(ServiceNotice.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = ServiceNotice.TABLE_NAME, comment = "服务订阅消息表")
public class ServiceNotice extends BaseSuperEntity<ServiceNotice, Long> {

	public static final String TABLE_NAME = "tt_service_notice";

	@Column(name = "store_id", nullable = false, columnDefinition = "varchar(255) not null default '' comment '商家id，为-1时，代表是平台发布的消息'")
	private String storeId;

	@Column(name = "banner_image", nullable = false, columnDefinition = "varchar(255) not null default '' comment 'banner图'")
	private String bannerImage;

	@Column(name = "title", nullable = false, columnDefinition = "varchar(255) not null default '' comment '标题'")
	private String title;

	@Column(name = "sub_title", nullable = false, columnDefinition = "varchar(255) not null default '' comment '副标题'")
	private String subTitle;

	@Column(name = "to_url", nullable = false, columnDefinition = "varchar(255) not null default '' comment '点击跳转（此内容与站内信内容只能有一个生效）'")
	private String toUrl;

	@Column(name = "content", nullable = false, columnDefinition = "varchar(255) not null default '' comment '站内信内容(富文本框编辑，可以上传图片的html)'")
	private String content;
}
