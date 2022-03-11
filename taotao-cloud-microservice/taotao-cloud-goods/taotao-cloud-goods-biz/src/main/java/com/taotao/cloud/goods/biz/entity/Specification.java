package com.taotao.cloud.goods.biz.entity;

import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 商品规格项
 *
 * 
 * @since 2020-02-18 15:18:56
 */
@Entity
@Table(name = Specification.TABLE_NAME)
@TableName(Specification.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Specification.TABLE_NAME, comment = "规格项")
public class Specification extends BaseSuperEntity<Specification, Long> {

	public static final String TABLE_NAME = "li_specification";

    /**
     * 规格名称
     */
    @NotEmpty(message = "规格名称不能为空")
    @Size(max = 20, message = "规格名称不能超过20个字符")
    @ApiModelProperty(value = "规格名称", required = true)
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String specName;

    /**
     * 所属卖家 0属于平台
     * <p>
     * 店铺自定义规格暂时废弃 2021-06-23
     * 后续推出新配置方式
     */
    @ApiModelProperty(hidden = true)
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String storeId;

    /**
     * 规格值名字
     */
    @TableField(value = "spec_value")
    @ApiModelProperty(value = "规格值名字, 《,》分割")
    @Length(max = 255, message = "长度超出限制")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String specValue;


}
