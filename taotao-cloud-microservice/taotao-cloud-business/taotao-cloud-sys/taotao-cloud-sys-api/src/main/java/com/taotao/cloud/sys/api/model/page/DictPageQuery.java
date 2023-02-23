package com.taotao.cloud.sys.api.model.page;

import com.taotao.cloud.common.model.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class DictPageQuery extends PageQuery {

	private String dictName;
	private String dictCode;
	private String description;
	private String remark;
}
