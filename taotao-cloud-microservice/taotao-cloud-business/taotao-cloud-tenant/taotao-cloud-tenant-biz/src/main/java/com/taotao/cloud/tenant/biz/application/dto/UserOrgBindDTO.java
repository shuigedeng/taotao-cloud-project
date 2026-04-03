package com.taotao.cloud.tenant.biz.application.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户组织绑定DTO
 */
@Data
public class UserOrgBindDTO {
    
    /**
     * 组织ID列表
     */
    private List<Long> orgIds;
    
    /**
     * 主组织ID
     */
    private Long mainOrgId;
}
