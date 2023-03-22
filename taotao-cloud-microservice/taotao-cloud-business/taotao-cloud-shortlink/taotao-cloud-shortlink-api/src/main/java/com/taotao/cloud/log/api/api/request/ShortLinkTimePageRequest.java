package com.taotao.cloud.log.api.api.request;

import com.taotao.cloud.log.api.api.common.PageRequest;
import com.taotao.cloud.log.api.api.common.Range;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This is Description
 *
 * @since 2022/05/04
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortLinkTimePageRequest extends PageRequest {

	/**
	 * 时间范围
	 */
	private Range<LocalDateTime> dateRange;

	/**
	 * 查询类型： 0（null） = 创建时间、1 = 更新时间
	 *
	 * @see BooleanEnum
	 */
	private Integer queryType;

	/**
	 * 倒序： 0（null） = 升序、1 = 降序
	 */
	private Integer desc;

}
