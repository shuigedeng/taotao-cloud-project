/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.tenant.api.rpc.dto.command;

import com.taotao.boot.common.model.ddd.types.Command;
import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;

/**
 * 公司查询VO
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 16:31:52
 */
@RecordBuilder
@Schema(description = "公司查询VO")
public record TenantRpcCommand(Long id) implements Command {

	@Serial
	private static final long serialVersionUID = -4132785717179910025L;

}
