package com.taotao.cloud.member.api.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会员地址DTO
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-14 11:26:08
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "租户id")
public class MemberAddressDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

	@NotEmpty(message = "收货人姓名不能为空")
	@Schema(description = "收货人姓名")
	private String consigneeName;

	//@Phone
	@Schema(description = "手机号码")
	private String consigneeMobile;

	@NotBlank(message = "地址不能为空")
	@Schema(description = "地址名称， '，'分割")
	private String consigneeAddressPath;

	@NotBlank(message = "地址不能为空")
	@Schema(description = "地址id，'，'分割 ")
	private String consigneeAddressIdPath;

	@NotEmpty(message = "详细地址不能为空")
	@Schema(description = "详细地址")
	private String consigneeDetail;
}
