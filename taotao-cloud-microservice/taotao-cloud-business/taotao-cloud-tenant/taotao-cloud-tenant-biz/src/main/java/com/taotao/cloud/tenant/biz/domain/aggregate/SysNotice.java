package com.taotao.cloud.tenant.biz.domain.aggregate;

import com.baomidou.mybatisplus.annotation.*;
import com.mdframe.forge.starter.core.domain.BaseEntity;
import com.mdframe.forge.starter.tenant.core.TenantEntity;
import com.mdframe.forge.starter.trans.annotation.DictTrans;
import com.mdframe.forge.starter.trans.annotation.TransField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 通知公告表实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_notice")
@DictTrans
public class SysNotice extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 公告ID
     */
    @TableId(value = "notice_id", type = IdType.AUTO)
    private Long noticeId;

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
    @TransField(dictType = "sys_notice_type")
    private String noticeType;
    
    @TableField(exist = false)
    private String noticeTypeName;

    /**
     * 发布状态：0-草稿/1-已发布/2-已撤回
     */
    @TransField(dictType = "sys_notice_status")
    private Integer publishStatus;
    
    @TableField(exist = false)
    private String publishStatusName;

    /**
     * 发布时间
     */
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
    @TransField(dictType = "yes_no")
    private Integer isTop;
    
    @TableField(exist = false)
    private String isTopName;

    /**
     * 置顶排序（数字越大越靠前）
     */
    private Integer topSort;

    /**
     * 附件ID列表（多个附件ID用逗号分隔，关联sys_file_metadata表）
     */
    private String attachmentIds;

    /**
     * 阅读次数
     */
    private Integer readCount;

    /**
     * 备注
     */
    private String remark;
}
