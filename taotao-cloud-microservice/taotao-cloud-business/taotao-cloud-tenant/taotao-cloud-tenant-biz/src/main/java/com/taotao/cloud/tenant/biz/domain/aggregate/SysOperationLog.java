package com.taotao.cloud.tenant.biz.domain.aggregate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志表实体
 */
@Data
@TableName("sys_operation_log")
public class SysOperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long tenantId;

    private Long userId;

    private String username;

    private String operationModule;

    private String operationType;

    private String operationDesc;

    private String requestMethod;

    private String requestUrl;

    private String requestParams;

    private String responseResult;

    private String errorMsg;

    private Integer operationStatus;

    private String operationIp;

    private String operationLocation;

    private String userAgent;

    private Long executeTime;

    private LocalDateTime operationTime;
}
