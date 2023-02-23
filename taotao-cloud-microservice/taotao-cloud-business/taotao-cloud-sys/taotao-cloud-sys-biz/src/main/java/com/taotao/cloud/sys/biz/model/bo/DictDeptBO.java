package com.taotao.cloud.sys.biz.model.bo;

import lombok.Data;

@Data
public class DictDeptBO {

	//dict
	private Long id;
	private Long dictId;
	private String itemText;
	private String itemValue;
	private String description;
	private Integer status;

	//dept
	private Integer deptId;
	private String name;
	private Integer parentId;
	private Integer sort;

}
