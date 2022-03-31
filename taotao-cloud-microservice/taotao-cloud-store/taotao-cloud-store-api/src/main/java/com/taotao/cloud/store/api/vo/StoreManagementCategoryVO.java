package com.taotao.cloud.store.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 店铺经营范围
 *
 * 
 * @since 2020/12/11 16:18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺经营范围")
//public class StoreManagementCategoryVO extends Category {
public class StoreManagementCategoryVO {

	@Schema(description = "已选择")
	private Boolean selected;

	//public StoreManagementCategoryVO(Category category) {
	//	BeanUtil.copyProperties(this, category);
	//}

}
