package com.taotao.cloud.sa.just.biz.just.justauth.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 租户第三方登录信息配置表
 * </p>
 *
 * @author GitEgg
 * @since 2022-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="JustAuthSource对象", description="租户第三方登录信息配置表")
public class QueryJustAuthSourceDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名称")
    private String sourceName;

    @ApiModelProperty(value = "登录类型")
    private String sourceType;
    
    @ApiModelProperty(value = "状态")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "开始时间")
    private String beginDateTime;

    @ApiModelProperty(value = "结束时间")
    private String endDateTime;

}
