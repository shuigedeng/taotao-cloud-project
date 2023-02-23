package com.taotao.cloud.im.biz.platform.modules.topic.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true) // 链式调用
public class TopicVo03 {

    /**
     * 帖子
     */
    private TopicVo04 topic;

    /**
     * 点赞信息
     */
    private List<TopicVo05> likeList;

    /**
     * 是否点赞
     */
    private YesOrNoEnum like;

    /**
     * 评论信息
     */
    private List<TopicVo06> replyList;

}
