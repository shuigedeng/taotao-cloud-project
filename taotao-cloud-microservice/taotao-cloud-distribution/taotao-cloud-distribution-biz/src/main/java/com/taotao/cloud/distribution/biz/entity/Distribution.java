package com.taotao.cloud.distribution.biz.entity;

import cn.lili.mybatis.BaseEntity;
import cn.lili.common.utils.BeanUtil;
import cn.lili.modules.distribution.entity.dto.DistributionApplyDTO;
import cn.lili.modules.distribution.entity.enums.DistributionStatusEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 分销员对象
 *
 * 
 * @since 2020-03-14 23:04:56
 */
@Data
@ApiModel(value = "分销员")
@TableName("li_distribution")
@NoArgsConstructor
public class Distribution extends BaseEntity {


    private static final long serialVersionUID = -4878132663540847325L;

    public Distribution(String memberId, String memberName, DistributionApplyDTO distributionApplyDTO) {
        this.memberId = memberId;
        this.memberName = memberName;
        distributionOrderCount=0;
        this.distributionStatus = DistributionStatusEnum.APPLY.name();
        BeanUtil.copyProperties(distributionApplyDTO, this);
    }

    @Schema(description =  "会员id")
    private String memberId;

    @Schema(description =  "会员名称")
    private String memberName;

    @Schema(description =  "会员姓名")
    private String name;

    @Schema(description =  "身份证号")
    private String idNumber;

    @Schema(description =  "分销总额")
    private Double rebateTotal = 0D;

    @Schema(description =  "可提现金额")
    private Double canRebate = 0D;

    @Schema(description =  "冻结金额")
    private Double commissionFrozen = 0D;

    @Schema(description =  "分销订单数")
    private Integer distributionOrderCount;

    /**
     * @see DistributionStatusEnum
     */
    @Schema(description =  "分销员状态", required = true)
    private String distributionStatus;

    @Length(min = 1, max = 200, message = "结算银行开户行名称长度为1-200位")
    @NotBlank(message = "结算银行开户行名称不能为空")
    @Schema(description =  "结算银行开户行名称")
    private String settlementBankAccountName;

    @Length(min = 1, max = 200, message = "结算银行开户账号长度为1-200位")
    @NotBlank(message = "结算银行开户账号不能为空")
    @Schema(description =  "结算银行开户账号")
    private String settlementBankAccountNum;

    @Length(min = 1, max = 200, message = "结算银行开户支行名称长度为1-200位")
    @NotBlank(message = "结算银行开户支行名称不能为空")
    @Schema(description =  "结算银行开户支行名称")
    private String settlementBankBranchName;

}
