package com.taotao.cloud.operation.biz.entity;

import cn.hutool.http.HtmlUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.common.enums.ClientTypeEnum;
import com.taotao.cloud.operation.api.enums.PageEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 页面数据DO
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = PageData.TABLE_NAME)
@TableName(PageData.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = PageData.TABLE_NAME, comment = "页面数据表")
public class PageData extends BaseSuperEntity<PageData, Long> {

	public static final String TABLE_NAME = "li_page_data";

	@Schema(description = "页面名称")

	private String name;

	@Schema(description = "页面数据")
	private String pageData;

	/**
	 * @see SwitchEnum
	 */
	@Schema(description = "页面开关状态", allowableValues = "OPEN,CLOSE")
	private String pageShow;

	/**
	 * @see PageEnum
	 */
	@Schema(description = "页面类型", allowableValues = "INDEX,STORE,SPECIAL")
	private String pageType;

	/**
	 * @see ClientTypeEnum
	 */
	@Schema(description = "客户端类型", allowableValues = "PC,H5,WECHAT_MP,APP")
	private String pageClientType;

	@Schema(description = "值")
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
		if (StringUtil.isNotEmpty(pageData)) {
			return HtmlUtil.unescape(pageData);
		}
		return pageData;
	}
}
