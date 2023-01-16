package com.taotao.cloud.workflow.api.common.model.login;

import java.util.List;
import lombok.Data;

/**
 *
 */
@Data
public class PermissionModel {
    private String modelId;
    private String moduleName;
    private List<PermissionVO> button;
    private List<PermissionVO> column;
    private List<PermissionVO> resource;
    private List<PermissionVO> form;
}
