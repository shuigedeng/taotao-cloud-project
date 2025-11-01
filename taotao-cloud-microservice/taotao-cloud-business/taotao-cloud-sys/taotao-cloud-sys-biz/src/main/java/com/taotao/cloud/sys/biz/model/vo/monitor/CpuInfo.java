package com.taotao.cloud.sys.biz.model.vo.monitor;

import lombok.Data;
import lombok.experimental.*;
import lombok.experimental.*;


import java.math.BigDecimal;

/**
 * CPU相关信息
 */
@Data
public class CpuInfo {

    /**
     * 核心数
     */
    private int cpuNum;

    /**
     * CPU总的使用率
     */
    private double total;

    /**
     * CPU系统使用率
     */
    private double sys;

    /**
     * CPU用户使用率
     */
    private double used;

    /**
     * CPU当前等待率
     */
    private double wait;

    /**
     * CPU当前空闲率
     */
    private double free;

    public double getTotal() {
        return NumberUtil.round(total * 100, 2).doubleValue();
    }

    public BigDecimal getSys() {
        return NumberUtil.div(sys * 100, total, 2);
    }

    public BigDecimal getUsed() {
        return NumberUtil.div(used * 100, total, 2);
    }

    public BigDecimal getWait() {
        return NumberUtil.div(wait * 100, total, 2);
    }

    public BigDecimal getFree() {
        return NumberUtil.div(free * 100, total, 2);
    }
}

