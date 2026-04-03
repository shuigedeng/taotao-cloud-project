package com.taotao.cloud.tenant.biz.domain.aggregate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mdframe.forge.starter.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 文件元数据实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_file_metadata")
public class SysFileMetadata extends BaseEntity {
    
    
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 文件唯一ID
     */
    private String fileId;

    /**
     * 原始文件名
     */
    private String originalName;

    /**
     * 存储文件名
     */
    private String storageName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 文件MIME类型
     */
    private String mimeType;

    /**
     * 文件扩展名
     */
    private String extension;

    /**
     * 文件MD5
     */
    private String md5;

    /**
     * 存储策略
     */
    private String storageType;

    /**
     * 存储桶
     */
    private String bucket;

    /**
     * 访问URL
     */
    private String accessUrl;

    /**
     * 缩略图URL
     */
    private String thumbnailUrl;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务ID
     */
    private String businessId;

    /**
     * 文件分组ID
     */
    private Long groupId;

    /**
     * 上传者ID
     */
    private Long uploaderId;

    /**
     * 上传时间
     */
    private LocalDateTime uploadTime;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 是否私有
     */
    private Boolean isPrivate;

    /**
     * 下载次数
     */
    private Integer downloadCount;

    /**
     * 状态
     */
    private Integer status;
}
