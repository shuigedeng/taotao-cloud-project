package com.taotao.cloud.member.api.model.query;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 联合登录查询dto
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-14 11:21:35
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "联合登录查询dto")
public class ConnectQuery implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

	@Schema(description = "租户id")
	private String userId;

	@Schema(description = "租户id")
	private String unionId;

	@Schema(description = "租户id")
	private String unionType;
}
