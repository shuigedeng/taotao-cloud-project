package com.taotao.cloud.store.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 店铺经营范围
 *
 * 
 * @since 2020/12/11 16:18
 */
@Schema(description = "店铺经营范围")
//public class StoreManagementCategoryVO extends Category {
public class StoreManagementCategoryVO {

	@Schema(description = "已选择")
	private Boolean selected;

	//public StoreManagementCategoryVO(Category category) {
	//	BeanUtil.copyProperties(this, category);
	//}

}
