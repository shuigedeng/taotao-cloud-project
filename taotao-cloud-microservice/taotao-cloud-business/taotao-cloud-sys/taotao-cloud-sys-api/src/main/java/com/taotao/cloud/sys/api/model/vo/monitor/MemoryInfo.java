package com.taotao.cloud.sys.api.model.vo.monitor;

import org.dromara.hutoolcore.util.NumberUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import lombok.Data;

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

    public double getTotal() {
        return NumberUtil.div(total, CommonConstant.GB, 2);
    }

    public double getUsed() {
        return NumberUtil.div(used, CommonConstant.GB, 2);
    }

    public double getFree() {
        return NumberUtil.div(free, CommonConstant.GB, 2);
    }

    public double getUsage() {
        return NumberUtil.div(used * 100, total, 2);
    }
}
