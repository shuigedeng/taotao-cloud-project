package com.taotao.cloud.goods.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品参数vo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsParamsGroupVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1450550797436233753L;
	@Schema(description = "参数组关联的参数集合")
	private List<GoodsParamsVO> params;
	@Schema(description = "参数组名称")
	private String groupName;
	@Schema(description = "参数组id")
	private String groupId;


}
