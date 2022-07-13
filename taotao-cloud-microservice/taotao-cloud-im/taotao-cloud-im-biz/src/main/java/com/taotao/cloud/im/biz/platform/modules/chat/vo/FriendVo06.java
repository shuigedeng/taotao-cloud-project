package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import com.platform.modules.chat.enums.FriendTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 好友对象
 */
@Data
@Accessors(chain = true) // 链式调用
public class FriendVo06 {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 头像
     */
    private String portrait;
    /**
     * 微聊号
     */
    private String chatNo;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 好友类型
     */
    private FriendTypeEnum userType = FriendTypeEnum.NORMAL;

}
