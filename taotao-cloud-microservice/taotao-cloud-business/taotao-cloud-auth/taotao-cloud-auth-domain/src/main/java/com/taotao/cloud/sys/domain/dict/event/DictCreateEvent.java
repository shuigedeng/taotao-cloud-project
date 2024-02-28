

package com.taotao.cloud.sys.domain.dict.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;


/**
 * 
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "OperateLogEvent", description = "操作日志事件")
public class DictCreateEvent {

	@Serial
	private static final long serialVersionUID = -6523521638764501311L;

	@Schema(name = "name", description = "操作名称")
	private String name;



}
