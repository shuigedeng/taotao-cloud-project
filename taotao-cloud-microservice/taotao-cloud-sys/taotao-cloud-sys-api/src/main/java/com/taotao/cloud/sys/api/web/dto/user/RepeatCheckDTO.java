package com.taotao.cloud.sys.api.web.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

import lombok.*;

/**
 * 重复校验DTO
 *
 * @author shuigedeng
 * @since 2020/5/2 16:40
 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "重复检查DTO")
public class RepeatCheckDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -4132785717179910025L;

	@Schema(description = "字段值 邮箱 手机号 用户名", required = true)
	private String fieldVal;

	@Schema(description = "指用户id 主要作用编辑情况过滤自己的校验", required = true)
	private Integer dataId;
}
