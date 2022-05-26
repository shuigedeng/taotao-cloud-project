package com.taotao.cloud.goods.api.vo;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 商品参数vo
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:34:09
 */
@RecordBuilder
public record GoodsParamsGroupVO(

	@Schema(description = "参数组关联的参数集合")
	List<GoodsParamsVO> params,
	@Schema(description = "参数组名称")
	String groupName,
	@Schema(description = "参数组id")
	String groupId

) implements Serializable {

	@Serial
	private static final long serialVersionUID = 1450550797436233753L;

}
