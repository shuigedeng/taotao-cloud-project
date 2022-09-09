package com.taotao.cloud.member.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberBrowseDTO {
	private Long memberId;

	private Long goodsId;

	private Long skuId;
}