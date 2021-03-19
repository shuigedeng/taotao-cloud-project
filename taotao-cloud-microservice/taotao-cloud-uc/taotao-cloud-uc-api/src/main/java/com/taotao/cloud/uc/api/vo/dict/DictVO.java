package com.taotao.cloud.uc.api.vo.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 字典VO
 *
 * @author dengtao
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "DictVO", description = "字典VO")
public class DictVO implements Serializable {

	private static final long serialVersionUID = 5126530068827085130L;

	@Schema(description = "id")
	private Long id;
	@Schema(description = "字典名称")
	private String dictName;
	@Schema(description = "字典编码")
	private String dictCode;
	@Schema(description = "描述")
	private String description;
	@Schema(description = "排序值")
	private Integer dictSort;
	@Schema(description = "备注信息")
	private String remark;
	@Schema(description = "创建时间")
	private LocalDateTime createTime;
	@Schema(description = "最后修改时间")
	private LocalDateTime lastModifiedTime;
}
