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
public class DictItemPageQuery extends PageQuery {

	private Long dictId;
	private String itemText;
	private String itemValue;
	private String description;
	private Integer status;
}
