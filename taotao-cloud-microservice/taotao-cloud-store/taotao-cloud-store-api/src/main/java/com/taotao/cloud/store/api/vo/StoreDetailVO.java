package com.taotao.cloud.store.api.vo;

import com.taotao.cloud.store.api.dto.StoreEditDTO;
import groovy.transform.EqualsAndHashCode;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 店铺详细VO
 *
 * 
 * @since 2020-03-09 21:53:20
 */
@Schema(description = "店铺详细VO")
public class StoreDetailVO extends StoreEditDTO {

	@Schema(description = "会员名称")
	private String memberName;

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
}
