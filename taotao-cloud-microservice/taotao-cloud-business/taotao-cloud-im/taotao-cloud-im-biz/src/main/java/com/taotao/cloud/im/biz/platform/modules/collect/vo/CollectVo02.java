package com.taotao.cloud.im.biz.platform.modules.collect.vo;

import com.platform.modules.collect.enums.CollectTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true) // 链式调用
public class CollectVo02 {

    /**
     * 主键
     */
    private Long collectId;
    /**
     * 收藏类型
     */
    private CollectTypeEnum collectType;
    /**
     * 内容
     */
    private String content;
    /**
     * 创建时间
     */
    private Date createTime;

    public String getCollectTypeLabel() {
        if (collectType == null) {
            return null;
        }
        return collectType.getInfo();
    }
}
