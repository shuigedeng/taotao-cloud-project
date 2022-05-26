package com.taotao.cloud.goods.api.vo;


import com.taotao.cloud.goods.api.dto.GoodsParamsDTO;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 商品关联参数的VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:34:13
 */
@RecordBuilder
public record GoodsParamsVO(

	@Schema(description = "1 输入项   2 选择项")
	Integer paramType,
	@Schema(description = " 选择项的内容获取值，使用optionList")
	String options,
	@Schema(description = "是否必填是  1    否   0")
	Integer required,
	//@Schema(description = "参数组id")
	// String groupId,
	@Schema(description = "是否可索引  1 可以   0不可以")
	Integer isIndex,

	String[] optionList,

	GoodsParamsDTO goodsParams
) implements Serializable {

	@Serial
	private static final long serialVersionUID = -4904700751774005326L;

	public String[] getOptionList() {
		if (options != null) {
			return options.replaceAll("\r|\n", "").split(",");
		}
		return optionList;
	}


}
