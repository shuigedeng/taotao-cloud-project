package com.taotao.cloud.sa.just.biz.just.justauth.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
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
@ApiModel(value="JustAuthConfigDTO对象", description="租户第三方登录功能配置表")
public class JustAuthConfigDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "登录开关")
    private Boolean enabled;

    @ApiModelProperty(value = "配置类")
    private String enumClass;

    @ApiModelProperty(value = "Http超时")
    private Integer httpTimeout;

    @ApiModelProperty(value = "缓存类型")
    private String cacheType;

    @ApiModelProperty(value = "缓存前缀")
    private String cachePrefix;

    @ApiModelProperty(value = "缓存超时")
    private Integer cacheTimeout;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;
}
