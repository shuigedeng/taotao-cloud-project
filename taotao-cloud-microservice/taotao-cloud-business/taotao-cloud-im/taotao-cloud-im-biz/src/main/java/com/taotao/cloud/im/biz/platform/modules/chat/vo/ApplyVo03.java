package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import com.platform.common.enums.GenderEnum;
import com.platform.modules.chat.enums.ApplySourceEnum;
import com.platform.modules.chat.enums.ApplyStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
public class ApplyVo03 {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户头像
     */
    private String portrait;
    /**
     * 用户昵称
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
     * 主键
     */
    private Long applyId;
    /**
     * 理由
     */
    private String reason;
    /**
     * 申请来源
     */
    private ApplySourceEnum applySource;
    /**
     * 申请状态0无1同意2拒绝
     */
    private ApplyStatusEnum applyStatus;

    public String getApplySourceLabel() {
        if (applySource == null) {
            return null;
        }
        return applySource.getInfo();
    }

    public String getGenderLabel() {
        if (gender == null) {
            return null;
        }
        return gender.getInfo();
    }

    public String getApplyStatusLabel() {
        if (applyStatus == null) {
            return null;
        }
        return applyStatus.getInfo();
    }
}
