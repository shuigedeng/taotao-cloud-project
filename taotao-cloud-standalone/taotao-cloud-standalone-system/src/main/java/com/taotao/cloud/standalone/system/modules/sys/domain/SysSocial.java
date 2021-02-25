package com.taotao.cloud.standalone.system.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Classname SysSocial
 * @Description 社交实体类
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @Date 2019-07-17 15:57
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("social_UserConnection")
public class SysSocial implements Serializable {

    @TableField("userId")
    private String userId;

    @TableField("providerId")
    private String providerId;

    @TableField("providerUserId")
    private String providerUserId;

    @TableField("`rank`")
    private Integer rank;

    @TableField("displayName")
    private String displayName;

    @TableField("profileUrl")
    private String profileUrl;

    @TableField("imageUrl")
    private String imageUrl;

    @TableField("accessToken")
    private String accessToken;

    private String secret;

    @TableField("refreshToken")
    private String refreshToken;

    @TableField("expireTime")
    private String expireTime;

    @TableField(exist = false)
    private String userName;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
