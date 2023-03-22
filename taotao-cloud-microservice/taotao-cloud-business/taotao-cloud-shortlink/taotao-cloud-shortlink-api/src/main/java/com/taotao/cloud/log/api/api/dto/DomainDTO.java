package com.taotao.cloud.log.api.api.dto;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DOMAIN - DTO
 *
 * @since 2022/05/03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DomainDTO implements Serializable {

	/**
	 * id
	 */
	private Long id;

	/**
	 * 用户自己绑定的域名
	 */
	private Long accountNo;

	/**
	 * 域名类型，0=系统自带, 1=用户自建
	 *
	 * @see com.zc.shortlink.api.enums.ShortLinkDomainTypeEnum
	 */
	private Boolean domainType;

	/**
	 * value
	 */
	private String value;
}
