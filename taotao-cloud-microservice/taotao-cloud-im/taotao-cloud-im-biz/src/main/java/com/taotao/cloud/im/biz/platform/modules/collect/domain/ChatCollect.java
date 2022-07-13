package com.taotao.cloud.im.biz.platform.modules.collect.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.collect.enums.CollectTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 收藏表实体类
 * q3z3
 * </p>
 */
@Data
@TableName("chat_collect")
@Accessors(chain = true) // 链式调用
public class ChatCollect extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
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

}
