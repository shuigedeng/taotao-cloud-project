package com.taotao.cloud.goods.api.query;

import com.taotao.cloud.common.model.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;

/**
 * 商品品牌dto
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-19 20:59:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "商品品牌dto")
public class BrandPageQuery extends PageParam {

	@Serial
	private static final long serialVersionUID = 8906820486037326039L;

	@Schema(description = "品牌名称")
	private String name;
}
