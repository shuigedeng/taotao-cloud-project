package com.taotao.cloud.store.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收藏数量变化DTO
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-14 11:21:10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "收藏数量变化DTO")
public class CollectionDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

	@Schema(description = "变化的模型id 商品id/店铺id")
	private String id;

	@Schema(description = "变化的数量 -1 减少1 / +1 增加1")
	private Integer num;
}
