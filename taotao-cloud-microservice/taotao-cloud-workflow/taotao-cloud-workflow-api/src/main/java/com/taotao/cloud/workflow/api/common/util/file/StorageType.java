package com.taotao.cloud.workflow.api.common.util.file;

import lombok.Data;

/**
 * 文件储存类型常量类
 *
 */
@Data
public class StorageType {
    /**
     * 本地存储
     */
    public static final String STORAGE = "storage";

    /**
     * Minio存储
     */
    public static final String MINIO = "minio";

}
