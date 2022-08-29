package com.taotao.cloud.operation.biz.model.entity;

import cn.hutool.http.HtmlUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.common.enums.ClientTypeEnum;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.operation.api.enums.PageEnum;
import com.taotao.cloud.operation.api.enums.SwitchEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;


/**
 * 页面数据DO
 */
@Getter
@Setter
@ToString(callSuper = true)
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = PageData.TABLE_NAME)
@TableName(PageData.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = PageData.TABLE_NAME, comment = "页面数据表")
public class PageData extends BaseSuperEntity<PageData, Long> {

	public static final String TABLE_NAME = "tt_page_data";
	/**
	 * 页面名称
	 */
	@Column(name = "name", columnDefinition = "varchar(255) not null comment '页面名称 '")
	private String name;

	/**
	 * 页面数据
	 */
	@Column(name = "page_data", columnDefinition = "varchar(255) not null comment '页面数据 '")
	private String pageData;

	/**
	 * 页面开关状态 OPEN,CLOSE
	 *
	 * @see SwitchEnum
	 */
	@Column(name = "page_show", columnDefinition = "varchar(255) not null comment '页面开关状态 OPEN,CLOSE '")
	private String pageShow;

	/**
	 * 页面类型 INDEX,STORE,SPECIAL
	 *
	 * @see PageEnum
	 */
	@Column(name = "page_type", columnDefinition = "varchar(255) not null comment '页面类型 INDEX,STORE,SPECIAL '")
	private String pageType;

	/**
	 * 客户端类型 PC,H5,WECHAT_MP,AP
	 *
	 * @see ClientTypeEnum
	 */
	@Column(name = "page_client_type", columnDefinition = "varchar(255) not null comment '客户端类型 PC,H5,WECHAT_MP,AP '")
	private String pageClientType;
	/**
	 * 值
	 */
	@Column(name = "num", columnDefinition = "varchar(255) not null comment '值 '")
	private String num;

	public PageData(String name, String pageClientType, String pageData, String num) {
		this.name = name;
		this.pageClientType = pageClientType;
		this.pageData = pageData;
		this.num = num;
		this.pageShow = SwitchEnum.CLOSE.name();
		this.pageType = PageEnum.STORE.name();
	}

	public String getPageData() {
		if (StringUtils.isNotEmpty(pageData)) {
			return HtmlUtil.unescape(pageData);
		}
		return pageData;
	}

	@Override
	public boolean equals(Object o) {
				if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		PageData pageData = (PageData) o;
		return getId() != null && Objects.equals(getId(), pageData.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
