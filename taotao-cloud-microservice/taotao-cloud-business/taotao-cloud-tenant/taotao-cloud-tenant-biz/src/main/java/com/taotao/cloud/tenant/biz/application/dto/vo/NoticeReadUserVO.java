package com.taotao.cloud.tenant.biz.application.dto.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通知公告已读/未读用户VO
 */
@Data
public class NoticeReadUserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 阅读时间（未读用户此字段为空）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime readTime;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;
}
