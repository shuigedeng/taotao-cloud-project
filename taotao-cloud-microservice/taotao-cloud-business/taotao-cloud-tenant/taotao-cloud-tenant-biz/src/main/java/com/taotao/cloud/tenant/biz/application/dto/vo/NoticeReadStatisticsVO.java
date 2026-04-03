package com.taotao.cloud.tenant.biz.application.dto.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 通知公告已读/未读统计VO
 */
@Data
public class NoticeReadStatisticsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公告ID
     */
    private Long noticeId;

    /**
     * 应读人数（目标用户总数）
     */
    private Integer totalUserCount;

    /**
     * 已读人数
     */
    private Integer readCount;

    /**
     * 未读人数
     */
    private Integer unreadCount;

    /**
     * 已读率（百分比）
     */
    private Double readRate;
}
