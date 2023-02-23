package com.taotao.cloud.promotion.api.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


/**
 * 砍价活动参与实体类
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "砍价活动参与记录查询对象")
public class KanjiaActivityPageQuery implements Serializable {

	private static final long serialVersionUID = -1583030890805926292L;

	@Schema(description = "货品名称")
	private String goodsName;

	@Schema(description = "会员id", hidden = true)
	private String memberId;

	// public <T> QueryWrapper<T> wrapper() {
	//     QueryWrapper<T> queryWrapper = new QueryWrapper<>();
	//
	//     if (CharSequenceUtil.isNotEmpty(goodsName)) {
	//         queryWrapper.like("goods_name", goodsName);
	//     }
	//     if (memberId != null) {
	//         queryWrapper.eq("member_id", memberId);
	//     }
	//     queryWrapper.eq("delete_flag", false);
	//     queryWrapper.orderByDesc("create_time");
	//     return queryWrapper;
	// }
}
