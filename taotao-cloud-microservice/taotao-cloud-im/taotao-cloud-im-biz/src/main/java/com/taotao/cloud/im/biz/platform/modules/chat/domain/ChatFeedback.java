package com.taotao.cloud.im.biz.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 建议反馈实体类
 * q3z3
 * </p>
 */
@Data
@TableName("chat_feedback")
@Accessors(chain = true) // 链式调用
public class ChatFeedback extends BaseEntity {

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
     * 图片
     */
    private String images;
    /**
     * 内容
     */
    private String content;
    /**
     * 版本
     */
    private String version;
    /**
     * 创建时间
     */
    private Date createTime;

}
