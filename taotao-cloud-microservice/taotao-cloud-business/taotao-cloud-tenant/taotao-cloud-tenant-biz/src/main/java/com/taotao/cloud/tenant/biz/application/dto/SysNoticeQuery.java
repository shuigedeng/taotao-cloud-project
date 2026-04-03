package com.taotao.cloud.tenant.biz.application.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通知公告查询条件DTO
 */
@Data
public class SysNoticeQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公告标题（模糊查询）
     */
    private String noticeTitle;

    /**
     * 公告类型
     */
    private String noticeType;

    /**
     * 发布状态
     */
    private Integer publishStatus;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 发布人姓名（模糊查询）
     */
    private String publisherName;

    /**
     * 生效开始时间
     */
    private LocalDateTime effectiveTimeStart;

    /**
     * 生效结束时间
     */
    private LocalDateTime effectiveTimeEnd;
}
