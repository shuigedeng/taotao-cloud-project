package com.taotao.cloud.workflow.api.vo.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

@Data
@TableName("base_comfields")
public class ComFieldsEntity {


	@TableId("F_ID")
	private String id;


	@TableField("F_FIELDNAME")
	private String fieldName;

	@TableField("F_FIELD")
	private String field;


	@TableField("F_DATATYPE")
	private String datatype;


	@TableField("F_DATALENGTH")
	private String datalength;

	@TableField("F_ALLOWNULL")
	private String allowNull;


	@TableField("F_DESCRIPTION")
	private String description;


	@TableField("F_SORTCODE")
	private Long sortcode;


	@TableField("F_ENABLEDMARK")
	private Integer enabledmark;


	@TableField("F_CREATORTIME")
	private Date creatortime;


	@TableField("F_CREATORUSERID")
	private String creatoruserid;


	@TableField("F_LASTMODIFYTIME")
	@JSONField(name = "F_LastModifyTime")
	private Date lastModifyTime;


	@TableField("F_LASTMODIFYUSERID")
	private String lastmodifyuserid;


	@TableField("F_DELETEMARK")
	private Integer deletemark;


	@TableField("F_DELETETIME")
	private Date deletetime;


	@TableField("F_DELETEUSERID")
	private String deleteuserid;

}

