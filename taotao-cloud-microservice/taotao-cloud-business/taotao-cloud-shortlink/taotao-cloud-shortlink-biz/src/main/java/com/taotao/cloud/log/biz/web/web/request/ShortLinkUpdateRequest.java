package com.taotao.cloud.log.biz.web.web.request;

import java.io.Serializable;
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
public class ShortLinkUpdateRequest implements Serializable {

	/**
	 * ID
	 */
	private Long id;

//    /**
//     * 分组ID
//     */
//    private Long groupId;
//
//    /**
//     * 短链标题
//     */
//    private String title;

	/**
	 * 原始URL
	 */
	private String originUrl;

//    /**
//     * 短链域名
//     */
//    private String domain;

	/**
	 * 短链码
	 */
	private String code;

//    /**
//     * 账户编码
//     */
//    private Long accountNo;
//
//    /**
//     * 状态：0=无效、1=有效
//     *
//     * @see com.zc.shortlink.api.enums.BooleanEnum
//     */
//    private Integer state;
//
//    /**
//     * 过期时间
//     */
//    private LocalDate expired;


}
