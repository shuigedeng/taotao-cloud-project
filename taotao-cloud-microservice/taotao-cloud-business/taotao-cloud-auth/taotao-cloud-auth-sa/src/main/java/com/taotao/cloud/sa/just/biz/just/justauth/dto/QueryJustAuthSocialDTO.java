package com.taotao.cloud.sa.just.biz.just.justauth.dto;

import com.gitegg.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 第三方用户信息
 * </p>
 *
 * @author GitEgg
 * @since 2022-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="JustAuthSocial对象", description="第三方用户信息")
public class QueryJustAuthSocialDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "第三方ID")
    private String uuid;

    @ApiModelProperty(value = "第三方来源")
    private String source;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "开始时间")
    private String beginDateTime;

    @ApiModelProperty(value = "结束时间")
    private String endDateTime;

}
