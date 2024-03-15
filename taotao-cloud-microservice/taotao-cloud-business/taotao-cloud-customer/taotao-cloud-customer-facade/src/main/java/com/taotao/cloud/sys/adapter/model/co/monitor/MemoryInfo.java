package com.taotao.cloud.sys.adapter.model.co.monitor;

import com.taotao.cloud.common.constant.CommonConstant;
import java.math.BigDecimal;
import lombok.Data;
import org.dromara.hutool.core.math.NumberUtil;

/**
 * 內存相关信息
 */
@Data
public class MemoryInfo {

    /**
     * 内存总量
     */
    private double total;

    /**
     * 已用内存
     */
    private double used;

    /**
     * 剩余内存
     */
    private double free;

    public BigDecimal getTotal() {
        return NumberUtil.div(total, CommonConstant.GB, 2);
    }

    public BigDecimal getUsed() {
        return NumberUtil.div(used, CommonConstant.GB, 2);
    }

    public BigDecimal getFree() {
        return NumberUtil.div(free, CommonConstant.GB, 2);
    }

    public BigDecimal getUsage() {
        return NumberUtil.div(used * 100, total, 2);
    }
}
