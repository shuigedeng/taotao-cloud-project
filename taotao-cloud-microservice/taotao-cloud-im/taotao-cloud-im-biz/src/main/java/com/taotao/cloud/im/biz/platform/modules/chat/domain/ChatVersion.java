package com.taotao.cloud.im.biz.platform.modules.chat.domain;

import com.platform.common.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * 版本实体类
 * q3z3
 * </p>
 */
@Data
@TableName("chat_version")
public class ChatVersion extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;
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

}
