package com.taotao.cloud.goods.api.vo;


import com.taotao.cloud.goods.api.dto.GoodsParamsDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import lombok.experimental.SuperBuilder;

/**
 * 商品关联参数的VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:34:13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsParamsVO extends GoodsParamsDTO {

	@Serial
	private static final long serialVersionUID = -4904700751774005326L;

	@Schema(description = "1 输入项   2 选择项")
	private Integer paramType;
	@Schema(description = " 选择项的内容获取值，使用optionList")
	private String options;
	@Schema(description = "是否必填是  1    否   0")
	private Integer required;
	//@Schema(description = "参数组id")
	//private String groupId;
	@Schema(description = "是否可索引  1 可以   0不可以")
	private Integer isIndex;

	private String[] optionList;

	public void setOptionList(String[] optionList) {
		this.optionList = optionList;
	}

	public String[] getOptionList() {
		if (options != null) {
			return options.replaceAll("\r|\n", "").split(",");
		}
		return optionList;
	}


}
