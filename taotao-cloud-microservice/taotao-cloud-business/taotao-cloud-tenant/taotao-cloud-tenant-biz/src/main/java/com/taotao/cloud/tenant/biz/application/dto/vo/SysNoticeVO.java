package com.taotao.cloud.tenant.biz.application.dto.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 通知公告VO
 */
@Data
public class SysNoticeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公告ID
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
     * 公告类型
     */
    private String noticeType;

    /**
     * 公告类型名称
     */
    private String noticeTypeName;

    /**
     * 发布状态
     */
    private Integer publishStatus;

    /**
     * 发布状态名称
     */
    private String publishStatusName;

    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;

    /**
     * 发布人ID
     */
    private Long publisherId;

    /**
     * 发布人姓名
     */
    private String publisherName;

    /**
     * 发布范围：0-全部组织/1-指定组织
     */
    private Integer publishScope;

    /**
     * 指定组织ID列表
     */
    private List<Long> orgIds;

    /**
     * 指定组织名称列表
     */
    private List<String> orgNames;

    /**
     * 当前用户是否已读（0-未读/1-已读）
     */
    private Integer isRead;

    /**
     * 生效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime effectiveTime;

    /**
     * 失效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expirationTime;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 是否置顶名称
     */
    private String isTopName;

    /**
     * 置顶排序
     */
    private Integer topSort;

    /**
     * 附件ID列表
     */
    private String attachmentIds;

    /**
     * 附件信息列表
     */
    private List<AttachmentInfo> attachments;

    /**
     * 阅读次数
     */
    private Integer readCount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 附件信息内部类
     */
    @Data
    public static class AttachmentInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        
        /**
         * 文件ID
         */
        private Long fileId;
        
        /**
         * 文件名称
         */
        private String fileName;
        
        /**
         * 文件大小
         */
        private Long fileSize;
        
        /**
         * 文件URL
         */
        private String fileUrl;
    }
}
