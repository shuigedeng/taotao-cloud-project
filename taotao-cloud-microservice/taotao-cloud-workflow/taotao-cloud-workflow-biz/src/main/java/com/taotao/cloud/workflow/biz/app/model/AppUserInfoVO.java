package com.taotao.cloud.workflow.biz.app.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/12 15:31
 */
@Data
public class AppUserInfoVO {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "户名")
    private String realName;
    @ApiModelProperty(value = "部门名称")
    private String organizeName;
    @ApiModelProperty(value = "账号")
    private String account;
    @ApiModelProperty(value = "岗位名称")
    private String positionName;
    @ApiModelProperty(value = "办公电话")
    private String telePhone;
    @ApiModelProperty(value = "办公座机")
    private String landline;
    @ApiModelProperty(value = "手机号码")
    private String mobilePhone;
    @ApiModelProperty(value = "用户头像")
    private String headIcon;
    @ApiModelProperty(value = "邮箱")
    private String email;
}
