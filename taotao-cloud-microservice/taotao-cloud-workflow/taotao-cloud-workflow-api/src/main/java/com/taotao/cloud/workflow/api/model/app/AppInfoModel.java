package com.taotao.cloud.workflow.api.model.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AppInfoModel {

	@ApiModelProperty(value = "用户id")
	private String id;
	@ApiModelProperty(value = "用户账号")
	private String account;
	@ApiModelProperty(value = "用户姓名")
	private String realName;
	@ApiModelProperty(value = "用户头像")
	private String headIcon;
	@ApiModelProperty(value = "组织名称")
	private String organizeName;
	@ApiModelProperty(value = "部门名称")
	private String departmentName;
	@ApiModelProperty(value = "角色名称")
	private String roleName;
	@ApiModelProperty(value = "岗位名称")
	private String positionName;
	@ApiModelProperty(value = "性别")
	private Integer gender;
	@ApiModelProperty(value = "生日")
	private Long birthday;
	@ApiModelProperty(value = "手机号码")
	private String mobilePhone;
	@ApiModelProperty(value = "邮箱")
	private String email;

}
