package com.taotao.cloud.common.model.query.entity;

import io.swagger.v3.oas.annotations.media.Schema;

/**   
* Between 类型参数
*/
@Schema(title = "Between 类型参数")
public class QueryBetweenParam {

    @Schema(description= "开始参数")
    private Object start;

    @Schema(description= "结束参数")
    private Object end;

	public Object getStart() {
		return start;
	}

	public void setStart(Object start) {
		this.start = start;
	}

	public Object getEnd() {
		return end;
	}

	public void setEnd(Object end) {
		this.end = end;
	}
}
