package com.taotao.cloud.payment.biz.daxpay.sdk.model.reconcile;

import lombok.Data;
import lombok.experimental.*;

/**
 * 对账单文件下载链接
 * @author xxm
 * @since 2024/8/21
 */
@Data
public class ReconcileDownModel {
    /**
     * 文件下载链接
     */
    private String fileUrl;
}
