package com.taotao.cloud.member.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会员收货地址DTO
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 14:55:28
 */
@Schema(description = "会员收货地址DTO")
public record MemberAddressVO(@Schema(description = "会员ID") String memberId,
                              @Schema(description = "收货人姓名") String name,
                              @Schema(description = "手机号码") String mobile,
                              @Schema(description = "地址名称，逗号分割") String consigneeAddressPath,
                              @Schema(description = "地址id,逗号分割") String consigneeAddressIdPath,
                              @Schema(description = "省") String province,
                              @Schema(description = "市") String city,
                              @Schema(description = "区县") String area,
                              @Schema(description = "省code") String provinceCode,
                              @Schema(description = "市code") String cityCode,
                              @Schema(description = "区县code") String areaCode,
                              @Schema(description = "街道地址") String address,
                              @Schema(description = "详细地址") String detail,
                              @Schema(description = "是否为默认收货地址") Boolean defaulted,
                              @Schema(description = "地址别名") String alias,
                              @Schema(description = "经度") String lon,
                              @Schema(description = "纬度") String lat,
                              @Schema(description = "邮政编码") String postalCode,
                              @Schema(description = "创建时间") LocalDateTime createTime,
                              @Schema(description = "最后修改时间") LocalDateTime lastModifiedTime) implements
	Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

}
