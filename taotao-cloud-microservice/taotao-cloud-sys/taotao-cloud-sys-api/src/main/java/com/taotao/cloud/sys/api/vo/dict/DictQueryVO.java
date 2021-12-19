package com.taotao.cloud.sys.api.vo.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 字典查询对象
 *
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Schema( description = "字典查询对象")
public record DictQueryVO(
	/**
	 * id
	 */
	@Schema(description = "id")
	Long id,
	/**
	 * 字典名称
	 */
	@Schema(description = "字典名称")
	String dictName,
	/**
	 * 字典编码
	 */
	@Schema(description = "字典编码")
	String dictCode,
	/**
	 * 描述
	 */
	@Schema(description = "描述")
	String description,
	/**
	 * 排序值
	 */
	@Schema(description = "排序值")
	Integer dictSort,
	/**
	 * 备注信息
	 */
	@Schema(description = "备注信息")
	String remark,
	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	LocalDateTime createTime,
	/**
	 * 最后修改时间
	 */
	@Schema(description = "最后修改时间")
	LocalDateTime lastModifiedTime) implements Serializable {

	static final long serialVersionUID = 5126530068827085130L;


}
