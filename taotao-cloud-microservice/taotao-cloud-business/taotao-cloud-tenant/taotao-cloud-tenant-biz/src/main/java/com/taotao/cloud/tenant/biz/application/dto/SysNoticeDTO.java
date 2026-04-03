package com.taotao.cloud.tenant.biz.application.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 通知公告新增/修改DTO
 */
@Data
public class SysNoticeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公告ID（修改时必传）
     */
    private Long noticeId;

    /**
     * 租户编号
     */
    private Long tenantId;

    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 公告内容
     */
    private String noticeContent;

    /**
     * 公告类型：NOTICE-通知公告/ANNOUNCEMENT-系统公告/NEWS-新闻动态
     */
    private String noticeType;

    /**
     * 发布状态：0-草稿/1-已发布/2-已撤回
     */
    private Integer publishStatus;

    /**
     * 发布范围：0-全部组织/1-指定组织
     */
    private Integer publishScope;

    /**
     * 指定组织ID列表（publishScope=1时必填）
     */
    private List<Long> orgIds;

    /**
     * 生效时间
     */
    private LocalDateTime effectiveTime;

    /**
     * 失效时间
     */
    private LocalDateTime expirationTime;

    /**
     * 是否置顶：0-否/1-是
     */
    private Integer isTop;

    /**
     * 置顶排序（数字越大越靠前）
     */
    private Integer topSort;

    /**
     * 附件ID列表（多个附件ID用逗号分隔）
     */
    private String attachmentIds;

    /**
     * 备注
     */
    private String remark;
}
