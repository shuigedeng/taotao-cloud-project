package com.taotao.cloud.log.api.api.request;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is Description
 *
 * @since 2022/05/03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortLinkCreateRequest implements Serializable {

	/**
	 * 用户ID
	 */
	private Long accountId;

	/**
	 * 组
	 */
	private Long groupId;

	/**
	 * 短链标题
	 */
	private String title;

	/**
	 * 原生url
	 */
	private String originalUrl;

	/**
	 * 域名id
	 */
	private Long domainId;

	/**
	 * 域名类型
	 *
	 * @see ShortLinkDomainTypeEnum
	 */
	private Integer domainType;

	/**
	 * 过期时间
	 */
	private LocalDate expired;

}
