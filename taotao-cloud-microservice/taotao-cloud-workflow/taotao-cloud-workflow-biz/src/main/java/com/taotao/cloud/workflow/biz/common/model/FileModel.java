package com.taotao.cloud.workflow.biz.common.model;

import lombok.Data;

/**
 * 附件模型
 *
 */
@Data
public class FileModel {
    private String fileId;
    private String fileName;
    private String fileSize;
    private String fileTime;
    private String fileState;
    private String fileType;
}
