package com.taotao.cloud.uc.api.vo.dict;

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
@Schema(name = "DictQueryVO", description = "字典查询对象")
public class DictQueryVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 5126530068827085130L;

	/**
	 * id
	 */
	@Schema(description = "id")
	private Long id;
	/**
	 * 字典名称
	 */
	@Schema(description = "字典名称")
	private String dictName;
	/**
	 * 字典编码
	 */
	@Schema(description = "字典编码")
	private String dictCode;
	/**
	 * 描述
	 */
	@Schema(description = "描述")
	private String description;
	/**
	 * 排序值
	 */
	@Schema(description = "排序值")
	private Integer dictSort;
	/**
	 * 备注信息
	 */
	@Schema(description = "备注信息")
	private String remark;
	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private LocalDateTime createTime;
	/**
	 * 最后修改时间
	 */
	@Schema(description = "最后修改时间")
	private LocalDateTime lastModifiedTime;

	public DictQueryVO() {
	}

	public DictQueryVO(Long id, String dictName, String dictCode, String description,
		Integer dictSort, String remark, LocalDateTime createTime,
		LocalDateTime lastModifiedTime) {
		this.id = id;
		this.dictName = dictName;
		this.dictCode = dictCode;
		this.description = description;
		this.dictSort = dictSort;
		this.remark = remark;
		this.createTime = createTime;
		this.lastModifiedTime = lastModifiedTime;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDictSort() {
		return dictSort;
	}

	public void setDictSort(Integer dictSort) {
		this.dictSort = dictSort;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(LocalDateTime lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
}
