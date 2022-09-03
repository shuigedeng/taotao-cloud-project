package com.taotao.cloud.common.model.query.entity;

import io.swagger.v3.oas.annotations.media.Schema;

/**   
*
*/
@Schema(title = "查询排序")
public class QueryOrder {

    @Schema(description= "设置排序字段")
    private String sortField;

    @Schema(description= "是否升序")
    private boolean asc = true;

    @Schema(description= "参数名称是否需要转换成下划线命名")
    private boolean underLine = true;


	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public boolean isAsc() {
		return asc;
	}

	public void setAsc(boolean asc) {
		this.asc = asc;
	}

	public boolean isUnderLine() {
		return underLine;
	}

	public void setUnderLine(boolean underLine) {
		this.underLine = underLine;
	}
}
