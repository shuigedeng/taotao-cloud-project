package com.taotao.cloud.im.biz.platform.modules.topic.domain;

import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 帖子点赞实体类
 * q3z3
 * </p>
 */
@Data
@TableName("chat_topic_like")
@Accessors(chain = true) // 链式调用
public class ChatTopicLike extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 帖子id
     */
    private Long topicId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 是否点赞
     */
    private YesOrNoEnum hasLike;

}
