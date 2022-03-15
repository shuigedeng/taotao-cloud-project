package com.taotao.cloud.promotion.biz.entity;

import cn.lili.modules.promotion.entity.dto.BasePromotions;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 拼团活动实体类
 *
 * 
 */
@Entity
@Table(name = Pintuan.TABLE_NAME)
@TableName(Pintuan.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Pintuan.TABLE_NAME, comment = "拼团活动实体类")
public class Pintuan extends BasePromotions<Pintuan, Long> {

	public static final String TABLE_NAME = "li_pintuan";

    @Min(message = "成团人数需大于等于2", value = 2)
    @Max(message = "成团人数最多10人", value = 10)
    @NotNull(message = "成团人数必填")
    @Schema(description =  "成团人数")
    private Integer requiredNum;

    @Min(message = "限购数量必须为数字", value = 0)
    @NotNull(message = "限购数量必填")
    @Schema(description =  "限购数量")
    private Integer limitNum;

    @Schema(description =  "虚拟成团", required = true)
    @NotNull(message = "虚拟成团必填")
    private Boolean fictitious;

    @Schema(description =  "拼团规则")
    private String pintuanRule;


}
