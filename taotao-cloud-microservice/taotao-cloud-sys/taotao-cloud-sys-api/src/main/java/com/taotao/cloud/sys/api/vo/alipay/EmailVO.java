/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.api.vo.alipay;


import java.util.List;

import lombok.*;

/**
 * EmailVo
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-22 09:25:30
 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class EmailVO {

	/**
	 * 收件人，支持多个收件人
	 */
	private List<String> tos;
	private String subject;
	private String content;
}
