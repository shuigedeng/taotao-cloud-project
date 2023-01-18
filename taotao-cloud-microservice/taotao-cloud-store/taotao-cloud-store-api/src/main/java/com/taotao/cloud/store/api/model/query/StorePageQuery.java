package com.taotao.cloud.store.api.model.query;

import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.store.api.enums.StoreStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * 店铺搜索参数VO
 *
 * @since 2020-03-07 17:02:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺搜索参数VO")
public class StorePageQuery extends PageQuery {

	@Serial
	private static final long serialVersionUID = 6916054310764833369L;

	@Schema(description = "会员名称")
	private String memberName;

	@Schema(description = "店铺名称")
	private String storeName;
	/**
	 * @see StoreStatusEnum
	 */
	@Schema(description = "店铺状态")
	private String storeDisable;

	@Schema(description = "开始时间")
	private String startDate;

	@Schema(description = "结束时间")
	private String endDate;

}
