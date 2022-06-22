package com.taotao.cloud.sys.biz.model.entity.config;

import java.io.Serializable;
import lombok.Data;

/**
 * 基础设置
 */
@Data
public class BaseSetting implements Serializable {

	private static final long serialVersionUID = -3138023944444671722L;
	/**
	 * 站点名称
	 */
	private String siteName;
	/**
	 * icp
	 */
	private String icp;
	/**
	 * 后端logo
	 */
	private String domainLogo;
	/**
	 * 买家端logo
	 */
	private String buyerSideLogo;
	/**
	 * 商家端logo
	 */
	private String storeSideLogo;
	/**
	 * 站点地址
	 */
	private String staticPageAddress;
	/**
	 * wap站点地址
	 */
	private String staticPageWapAddress;
}
