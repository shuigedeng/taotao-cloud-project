package com.taotao.cloud.im.biz.platform.modules.shake.vo;

import com.platform.common.enums.GenderEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
public class NearVo02 {

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 头像
     */
    private String portrait;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 介绍
     */
    private String intro;
    /**
     * 性别1男0女
     */
    private GenderEnum gender;
    /**
     * 距离
     */
    private double distance;
    /**
     * 距离单位
     */
    private String distanceUnit;

    public String getGenderLabel() {
        if (gender == null) {
            return null;
        }
        return gender.getInfo();
    }

}
