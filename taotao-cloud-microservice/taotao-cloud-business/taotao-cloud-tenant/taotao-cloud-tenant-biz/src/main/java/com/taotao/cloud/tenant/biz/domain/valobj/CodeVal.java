package com.taotao.cloud.tenant.biz.domain.valobj;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class CodeVal {

	@Column(name = "`apply_no`", columnDefinition = "varchar(255) not null comment '单号'")
	private String applyNo;
}
