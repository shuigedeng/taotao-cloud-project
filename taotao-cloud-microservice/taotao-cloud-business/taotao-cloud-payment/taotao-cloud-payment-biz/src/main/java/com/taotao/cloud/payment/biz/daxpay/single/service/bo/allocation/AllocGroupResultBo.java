package com.taotao.cloud.payment.biz.daxpay.single.service.bo.allocation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.taotao.cloud.payment.biz.daxpay.core.result.MchAppResult;

import java.math.BigDecimal;

/**
 * 分账组
 * @author xxm
 * @since 2024/4/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "分账组")
public class AllocGroupResultBo extends MchAppResult {

    @Schema(description = "分账组编号")
    private String groupNo;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "通道")
    private String channel;

    @Schema(description = "默认分账组")
    private Boolean defaultGroup;

    @Schema(description = "分账比例(百分之多少)")
    private BigDecimal totalRate;

    @Schema(description = "备注")
    private String remark;
}
