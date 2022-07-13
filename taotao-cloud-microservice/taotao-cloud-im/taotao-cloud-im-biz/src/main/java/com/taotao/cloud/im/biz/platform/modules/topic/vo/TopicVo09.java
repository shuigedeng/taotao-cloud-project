package com.taotao.cloud.im.biz.platform.modules.topic.vo;

import com.platform.modules.topic.enums.TopicNoticeTypeEnum;
import com.platform.modules.topic.enums.TopicTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true) // 链式调用
public class TopicVo09 {

    /**
     * 帖子id
     */
    private Long topicId;
    /**
     * 帖子类型
     */
    private TopicTypeEnum topicType;
    /**
     * 帖子内容
     */
    private String topicContent;
    /**
     * 通知类型 1点赞 2回复
     */
    private TopicNoticeTypeEnum noticeType;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像
     */
    private String portrait;
    /**
     * 回复内容
     */
    private String replyContent;
    /**
     * 回复时间
     */
    private Date replyTime;

}
