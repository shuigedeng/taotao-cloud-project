package com.taotao.cloud.im.biz.platform.modules.topic.vo;

import com.platform.modules.topic.enums.TopicTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true) // 链式调用
public class TopicVo04 {

    /**
     * 主键
     */
    private Long topicId;
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
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像
     */
    private String portrait;

}
