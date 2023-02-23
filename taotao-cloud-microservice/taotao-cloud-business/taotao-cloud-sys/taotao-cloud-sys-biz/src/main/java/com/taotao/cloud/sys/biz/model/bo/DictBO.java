package com.taotao.cloud.sys.biz.model.bo;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 字典查询对象
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-23 08:49:51
 */
@RecordBuilder
public record DictBO(

	/**
	 * id
	 */
	Long id,
	/**
	 * 字典名称
	 */
	String dictName,
	/**
	 * 字典编码
	 */
	String dictCode,
	/**
	 * 描述
	 */
	String description,
	/**
	 * 排序值
	 */
	Integer dictSort,
	/**
	 * 备注信息
	 */
	String remark,
	/**
	 * 创建时间
	 */
	LocalDateTime createTime,
	/**
	 * 最后修改时间
	 */
	LocalDateTime lastModifiedTime) implements Serializable {

	static final long serialVersionUID = 5126530068827085130L;


}
