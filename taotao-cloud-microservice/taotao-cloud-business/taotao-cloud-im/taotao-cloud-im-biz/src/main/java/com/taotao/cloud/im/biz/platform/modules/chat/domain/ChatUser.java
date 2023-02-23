package com.taotao.cloud.im.biz.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.platform.common.constant.ApiConstant;
import com.platform.common.enums.GenderEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.push.vo.PushParamVo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 用户表实体类
 * q3z3
 * </p>
 */
@Data
@TableName("chat_user")
@Accessors(chain = true)
public class ChatUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long userId;
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
     * 头像
     */
    private String portrait;
    /**
     * 封面
     */
    private String cover;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 省份
     */
    private String provinces;
    /**
     * 城市
     */
    private String city;
    /**
     * 微聊号
     */
    private String chatNo;
    /**
     * 密码
     */
    private String password;
    /**
     * 盐
     */
    private String salt;
    /**
     * 状态Y正常N禁用
     */
    private YesOrNoEnum status;
    /**
     * 推送id
     */
    private String cid;
    /**
     * 用户token
     */
    private String token;
    /**
     * 版本信息
     */
    private String version;
    /**
     * 注册时间
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date createTime;
    /**
     * 注销0正常null注销
     */
    @TableLogic
    private Integer deleted;
    /**
     * 注销时间
     */
    private Date deletedTime;

    /**
     * 格式化，防止出错
     */
    public static ChatUser initUser(ChatUser user) {
        if (user != null) {
            return user;
        }
        return new ChatUser()
                .setGender(GenderEnum.MALE)
                .setPortrait(ApiConstant.DELETED_PORTRAIT)
                .setNickName(ApiConstant.DELETED_NICK_NAME)
                .setStatus(YesOrNoEnum.NO);
    }

    /**
     * 格式化，防止出错
     */
    public static PushParamVo initParam(ChatUser user) {
        user = initUser(user);
        return new PushParamVo()
                .setUserId(user.getUserId())
                .setPortrait(user.getPortrait())
                .setNickName(user.getNickName());
    }

}
