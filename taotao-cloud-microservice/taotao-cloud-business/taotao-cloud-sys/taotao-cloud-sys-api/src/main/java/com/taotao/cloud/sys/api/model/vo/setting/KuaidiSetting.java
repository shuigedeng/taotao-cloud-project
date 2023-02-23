package com.taotao.cloud.sys.api.model.vo.setting;

import lombok.Data;

import java.io.Serializable;

/**
 * 快递设置
 *
 */
@Data
public class KuaidiSetting implements Serializable {
    private static final long serialVersionUID = 3520379500723173689L;
    /**
     * 企业id
     */
    private String ebusinessID;
    /**
     * 密钥
     */
    private String appKey;
    /**
     * api地址
     */
    private String reqURL;
}
