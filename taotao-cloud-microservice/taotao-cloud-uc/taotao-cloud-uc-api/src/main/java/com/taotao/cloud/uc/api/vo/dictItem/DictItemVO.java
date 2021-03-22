package com.taotao.cloud.uc.api.vo.dictItem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 字典项VO
 *
 * @author dengtao
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "DictItemVO", description = "字典项VO")
public class DictItemVO implements Serializable {

	private static final long serialVersionUID = 5126530068827085130L;
	@Schema(description = "id")
	private Long id;
	@Schema(description = "字典id")
	private Long dictId;
	@Schema(description = "字典项文本")
	private String itemText;
	@Schema(description = "字典项值")
	private String itemValue;
	@Schema(description = "描述")
	private String description;
	@Schema(description = "状态(1不启用 2启用)")
	private Integer status;
	@Schema(description = "创建时间")
	private LocalDateTime createTime;
	@Schema(description = "最后修改时间")
	private LocalDateTime lastModifiedTime;
}
