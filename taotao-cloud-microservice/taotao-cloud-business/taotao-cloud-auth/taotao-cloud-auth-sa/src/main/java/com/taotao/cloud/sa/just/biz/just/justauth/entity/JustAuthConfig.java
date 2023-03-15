package com.taotao.cloud.sa.just.biz.just.justauth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("t_just_auth_config")
@ApiModel(value = "JustAuthConfig对象", description = "租户第三方登录功能配置表")
public class JustAuthConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "JustAuth开关")
    @TableField("enabled")
    private Boolean enabled;

    @ApiModelProperty(value = "自定义扩展第三方登录的配置类")
    @TableField("enum_class")
    private String enumClass;

    @ApiModelProperty(value = "Http请求的超时时间")
    @TableField("http_timeout")
    private Integer httpTimeout;

    @ApiModelProperty(value = "缓存类型")
    @TableField("cache_type")
    private String cacheType;

    @ApiModelProperty(value = "缓存前缀")
    @TableField("cache_prefix")
    private String cachePrefix;

    @ApiModelProperty(value = "缓存超时时间")
    @TableField("cache_timeout")
    private Integer cacheTimeout;

    @ApiModelProperty(value = "状态")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;


}
