package com.taotao.cloud.sys.biz.domain.model.vo.monitor;

import cn.hutool.core.util.NumberUtil;
import com.taotao.boot.common.constant.CommonConstants;
import lombok.Data;

import java.math.BigDecimal;

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
        return NumberUtil.div(total, CommonConstants.GB, 2);
    }

    public BigDecimal getUsed() {
        return NumberUtil.div(used, CommonConstants.GB, 2);
    }

    public BigDecimal getFree() {
        return NumberUtil.div(free, CommonConstants.GB, 2);
    }

    public BigDecimal getUsage() {
        return NumberUtil.div(used * 100, total, 2);
    }
}
