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
* 第三方用户绑定
* </p>
*
* @author GitEgg
* @since 2022-05-19
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_just_auth_social_user")
@ApiModel(value = "JustAuthSocialUser对象", description = "第三方用户绑定")
public class JustAuthSocialUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "第三方用户id")
    @TableField("social_id")
    private Long socialId;


}
