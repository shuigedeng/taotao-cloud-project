package com.taotao.cloud.log.api.api.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * link_group - DTO
 *
 * @since 2022/05/03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkGroupDTO implements Serializable {

	/**
	 * id
	 */
	private Long id;

	/**
	 * 分组名
	 */
	private String title;

	/**
	 * 账号唯一编号
	 */
	private Long accountNo;

	private static final long serialVersionUID = 1L;
}
