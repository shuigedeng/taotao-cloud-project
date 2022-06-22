package com.taotao.cloud.sys.biz.model.entity.config;

import java.io.Serializable;
import lombok.Data;

/**
 * 分销配置
 */
@Data
public class DistributionSetting implements Serializable {

    private static final long serialVersionUID = 2099524659914361438L;

    /**
     * 是否开启分销
     */
    private Boolean isOpen;
    /**
     * 分销关系绑定天数
     */
    private Integer distributionDay;
    /**
     * 分销结算天数
     */
    private Integer cashDay;

}
