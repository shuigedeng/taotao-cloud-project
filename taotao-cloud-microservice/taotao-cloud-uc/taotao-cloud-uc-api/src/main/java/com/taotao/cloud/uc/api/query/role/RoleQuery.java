package com.taotao.cloud.uc.api.query.role;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 角色分页查询query
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Schema(name = "RolePageQuery", description = "角色查询query")
public class RoleQuery implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;


	public RoleQuery() {
	}
}
