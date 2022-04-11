package com.taotao.cloud.sys.api.vo.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.*;

/**
 * 字典查询对象
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "字典查询对象")
public class DictQueryVO implements Serializable {

	@Serial
	private static final long serialVersionUID = -4132785717179910025L;

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
