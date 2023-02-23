package com.taotao.cloud.im.biz.platform.modules.topic.vo;

import com.platform.common.enums.YesOrNoEnum;
import com.platform.modules.topic.enums.TopicReplyTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true) // 链式调用
public class TopicVo06 {

    /**
     * 回复id
     */
    private Long replyId;
    /**
     * 回复类型1帖子2用户
     */
    private TopicReplyTypeEnum replyType;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 回复时间
     */
    private Date createTime;
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
     * 是否可以删除
     */
    private YesOrNoEnum canDeleted;
    /**
     * 用户id
     */
    private Long toUserId;
    /**
     * 昵称
     */
    private String toNickName;
    /**
     * 头像
     */
    private String toPortrait;

}
