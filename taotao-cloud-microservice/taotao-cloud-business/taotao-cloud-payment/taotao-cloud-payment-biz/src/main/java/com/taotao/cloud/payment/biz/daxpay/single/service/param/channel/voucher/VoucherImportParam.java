package com.taotao.cloud.payment.biz.daxpay.single.service.param.channel.voucher;

import com.taotao.cloud.payment.biz.daxpay.single.service.code.VoucherStatusEnum;
import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 储值卡导入参数
 * @author xxm
 * @since 2022/3/14
 */
@Data
@Schema(title = "储值卡导入参数")
public class VoucherImportParam {

    @Schema(description = "卡号")
    private String cardNo;

    @Schema(description = "面值")
    private Integer faceValue;

    @Schema(description = "余额")
    private Integer balance;

    @Schema(description = "是否长期有效")
    private boolean enduring;

    @Schema(description = "开始时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime endTime;

    /**
     * @see VoucherStatusEnum
     */
    @Schema(description = "默认状态")
    private String status;

}
