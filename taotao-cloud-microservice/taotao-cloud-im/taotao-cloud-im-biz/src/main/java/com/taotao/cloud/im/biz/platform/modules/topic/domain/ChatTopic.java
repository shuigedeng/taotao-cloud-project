package com.taotao.cloud.im.biz.platform.modules.topic.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.topic.enums.TopicTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 主题实体类
 * q3z3
 * </p>
 */
@Data
@TableName("chat_topic")
@Accessors(chain = true) // 链式调用
public class ChatTopic extends BaseEntity {

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
     * 类型
     */
    private TopicTypeEnum topicType;
    /**
     * 内容
     */
    private String content;
    /**
     * 经纬度
     */
    private String location;
    /**
     * 时间
     */
    private Date createTime;

}
