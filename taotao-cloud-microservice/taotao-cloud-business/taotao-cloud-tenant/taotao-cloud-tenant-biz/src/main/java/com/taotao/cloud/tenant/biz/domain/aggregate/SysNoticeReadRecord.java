package com.taotao.cloud.tenant.biz.domain.aggregate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mdframe.forge.starter.tenant.core.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 通知公告已读记录表实体类
 */
@Data
@TableName("sys_notice_read_record")
public class SysNoticeReadRecord {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 公告ID
     */
    private Long noticeId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户所属组织ID
     */
    private Long orgId;

    /**
     * 用户所属组织名称
     */
    private String orgName;

    /**
     * 阅读时间
     */
    private LocalDateTime readTime;
    
    private LocalDateTime createTime;
}
