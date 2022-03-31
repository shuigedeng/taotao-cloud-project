package com.taotao.cloud.order.biz.entity.trade;

import cn.lili.common.security.enums.UserEnums;
import cn.lili.common.utils.StringUtils;
import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 订单日志
 *
 * 
 * @since 2020-03-25 2:30 下午
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("li_order_log")
@ApiModel(value = "订单日志")
@NoArgsConstructor
public class OrderLog extends BaseIdEntity {

    private static final long serialVersionUID = -1599270944927160096L;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @CreatedBy
    @TableField(fill = FieldFill.INSERT)
    @Schema(description =  "创建者", hidden = true)
    private String createBy;
	/**
	 * 应用ID
	 */
    @CreatedDate@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    @Schema(description =  "创建时间", hidden = true)

    private Date createTime;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "订单编号")
	/**
	 * 应用ID
	 */
    private String orderSn;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "操作者id(可以是卖家)")
	/**
	 * 应用ID
	 */
    private String operatorId;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    /**
     * @see UserEnums
     */
    @Schema(description =  "操作者类型")
	/**
	 * 应用ID
	 */
    private String operatorType;
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "操作者名称")
    private String operatorName;
	/**
	 * 应用ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @Schema(description =  "日志信息")
    private String message;

    public OrderLog(String orderSn, String operatorId, String operatorType, String operatorName, String message) {
        this.orderSn = orderSn;
        this.operatorId = operatorId;
        this.operatorType = operatorType;
        this.operatorName = operatorName;
        this.message = message;
    }

    public String getCreateBy() {
        if (StringUtils.isEmpty(createBy)) {
            return "系统";
        }
        return createBy;
    }
}
