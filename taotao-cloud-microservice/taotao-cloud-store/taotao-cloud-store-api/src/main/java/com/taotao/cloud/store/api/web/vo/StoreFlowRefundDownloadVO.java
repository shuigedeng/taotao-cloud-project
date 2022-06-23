package com.taotao.cloud.store.api.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 店铺流水下载
 *
 * 
 */
@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺流水下载")
public class StoreFlowRefundDownloadVO extends StoreFlowPayDownloadVO {

    @Schema(description = "售后SN")
    private String refundSn;
}
