package com.taotao.cloud.sys.biz.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 服务订阅消息
 *
 * 
 * @since 2020-02-25 14:10:16
 */
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

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getBannerImage() {
		return bannerImage;
	}

	public void setBannerImage(String bannerImage) {
		this.bannerImage = bannerImage;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getToUrl() {
		return toUrl;
	}

	public void setToUrl(String toUrl) {
		this.toUrl = toUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
