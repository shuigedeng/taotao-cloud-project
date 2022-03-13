package com.taotao.cloud.bulletin.biz.entity;

import cn.lili.mybatis.BaseEntity;
import cn.lili.common.enums.ClientTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 专题活动
 *
 * 
 * @since 2020/12/10 17:42
 */
@Data
@TableName("li_special")
@ApiModel(value = "专题活动")
public class Special extends BaseEntity {

    @Schema(description =  "专题活动名称")
    private String specialName;

    /**
     * @see ClientTypeEnum
     */
    @Schema(description =  "楼层对应连接端类型", allowableValues = "PC,H5,WECHAT_MP,APP")
    private String clientType;

    @Schema(description =  "页面ID")
    private String pageDataId;
}
