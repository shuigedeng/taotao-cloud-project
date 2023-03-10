package com.taotao.cloud.open.platform.openapiclient;


import com.taotao.cloud.openapi.client.annotation.OpenApiMethod;
import com.taotao.cloud.openapi.client.annotation.OpenApiRef;
import com.taotao.cloud.openapi.client.model.Role;

/**
 *
 */
@OpenApiRef("roleApi")
public interface RoleApiClient {

	@OpenApiMethod("getRoleById")
	Role getRoleById(Long id);

}
