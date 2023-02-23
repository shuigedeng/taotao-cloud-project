package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
public class VersionVo {

    /**
     * 是否升级
     */
    private YesOrNoEnum upgrade;
    /**
     * 版本
     */
    private String version;
    /**
     * 地址
     */
    private String url;
    /**
     * 内容
     */
    private String content;
    /**
     * 是否强制升级
     */
    private YesOrNoEnum forceUpgrade;

}
