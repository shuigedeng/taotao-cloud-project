package com.taotao.cloud.store.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 店铺流水下载
 *
 * 
 * @date: 2021/8/13 4:14 下午
 */
@Schema(description = "店铺流水下载")
public class StoreFlowRefundDownloadVO extends StoreFlowPayDownloadVO {

    @Schema(description = "售后SN")
    private String refundSn;

	public String getRefundSn() {
		return refundSn;
	}

	public void setRefundSn(String refundSn) {
		this.refundSn = refundSn;
	}
}
