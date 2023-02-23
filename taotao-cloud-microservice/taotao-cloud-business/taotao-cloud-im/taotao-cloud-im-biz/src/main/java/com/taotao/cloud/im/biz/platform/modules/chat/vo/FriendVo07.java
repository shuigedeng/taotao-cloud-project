package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import com.platform.common.enums.GenderEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.modules.chat.enums.ApplySourceEnum;
import com.platform.modules.chat.enums.FriendTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 好友详情
 */
@Data
@Accessors(chain = true) // 链式调用
public class FriendVo07 {

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
     * 性别1男0女
     */
    private GenderEnum gender;
    /**
     * 封面
     */
    private String cover;
    /**
     * 微聊号
     */
    private String chatNo;
    /**
     * 省份
     */
    private String provinces;
    /**
     * 城市
     */
    private String city;
    /**
     * 介绍
     */
    private String intro;
    /**
     * 是否是好友
     */
    private YesOrNoEnum isFriend = YesOrNoEnum.NO;
    /**
     * 是否黑名单
     */
    private YesOrNoEnum black = YesOrNoEnum.NO;
    /**
     * 好友来源
     */
    private ApplySourceEnum source;
    /**
     * 好友类型
     */
    private FriendTypeEnum userType = FriendTypeEnum.NORMAL;

    public String getGenderLabel() {
        if (gender == null) {
            return null;
        }
        return gender.getInfo();
    }

    public String getSourceLabel() {
        if (source == null) {
            return null;
        }
        return source.getInfo();
    }

}
