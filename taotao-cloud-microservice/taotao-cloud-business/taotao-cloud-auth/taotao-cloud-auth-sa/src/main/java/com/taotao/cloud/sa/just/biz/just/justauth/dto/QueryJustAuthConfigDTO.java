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
@ApiModel(value="JustAuthConfig对象", description="租户第三方登录功能配置表")
public class QueryJustAuthConfigDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "登录开关")
    private Boolean enabled;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "开始时间")
    private String beginDateTime;

    @ApiModelProperty(value = "结束时间")
    private String endDateTime;

}
