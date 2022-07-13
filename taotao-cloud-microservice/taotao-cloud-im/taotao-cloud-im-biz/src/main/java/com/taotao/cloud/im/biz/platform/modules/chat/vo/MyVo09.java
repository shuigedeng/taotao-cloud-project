package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import com.platform.common.enums.GenderEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
public class MyVo09 {

    /**
     * 主键
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
     * 封面
     */
    private String cover;
    /**
     * 性别1男0女
     */
    private GenderEnum gender;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 推送id
     */
    private String cid;
    /**
     * 微聊号
     */
    private String chatNo;
    /**
     * 介绍
     */
    private String intro;
    /**
     * 省份
     */
    private String provinces;
    /**
     * 城市
     */
    private String city;

    public String getGenderLabel() {
        if (gender == null) {
            return null;
        }
        return gender.getInfo();
    }

}
