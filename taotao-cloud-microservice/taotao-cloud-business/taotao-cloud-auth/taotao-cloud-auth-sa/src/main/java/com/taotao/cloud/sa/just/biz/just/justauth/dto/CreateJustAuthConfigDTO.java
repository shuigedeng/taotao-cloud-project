package com.taotao.cloud.sa.just.biz.just.justauth.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 租户第三方登录功能配置表
 * </p>
 *
 * @author GitEgg
 * @since 2022-05-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="JustAuthConfig对象", description="租户第三方登录功能配置表")
public class CreateJustAuthConfigDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "登录开关")
    @NotBlank(message="登录开关不能为空")
    @Length(min=1,max=1)
    private Boolean enabled;

    @ApiModelProperty(value = "配置类")
    @Length(min=1,max=255)
    private String enumClass;

    @ApiModelProperty(value = "Http超时")
    @Min(0L)
    @Max(2147483647L)
    @Length(min=1,max=19)
    private Integer httpTimeout;

    @ApiModelProperty(value = "缓存类型")
    @Length(min=1,max=32)
    private String cacheType;

    @ApiModelProperty(value = "缓存前缀")
    @Length(min=1,max=100)
    private String cachePrefix;

    @ApiModelProperty(value = "缓存超时")
    @Min(0L)
    @Max(2147483647L)
    @Length(min=1,max=255)
    private Integer cacheTimeout;

    @ApiModelProperty(value = "状态")
    @NotBlank(message="状态不能为空")
    @Min(0L)
    @Max(2147483647L)
    @Length(min=1,max=3)
    private Integer status;

    @ApiModelProperty(value = "备注")
    @Length(min=1,max=255)
    private String remark;
}
