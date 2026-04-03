package com.taotao.cloud.tenant.biz.domain.aggregate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mdframe.forge.starter.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件存储配置实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_file_storage_config")
public class SysFileStorageConfig extends BaseEntity {
    
    
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 存储类型
     */
    private String storageType;

    /**
     * 是否默认策略
     */
    private Boolean isDefault;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 访问端点
     */
    private String endpoint;

    /**
     * 访问密钥ID
     */
    private String accessKey;

    /**
     * 访问密钥Secret
     */
    private String secretKey;

    /**
     * 存储桶名称
     */
    private String bucketName;

    /**
     * 区域
     */
    private String region;

    /**
     * 基础路径
     */
    private String basePath;

    /**
     * 访问域名
     */
    private String domain;

    /**
     * 是否使用HTTPS
     */
    private Boolean useHttps;

    /**
     * 最大文件大小（MB）
     */
    private Integer maxFileSize;

    /**
     * 允许的文件类型
     */
    private String allowedTypes;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 扩展配置
     */
    private String extraConfig;
}
