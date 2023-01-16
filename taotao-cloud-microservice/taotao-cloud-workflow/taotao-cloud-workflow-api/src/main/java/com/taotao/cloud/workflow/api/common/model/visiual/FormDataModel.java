package com.taotao.cloud.workflow.api.common.model.visiual;

import lombok.Data;

/**
 *
 */
@Data
public class FormDataModel {
    /**
     * 模块
     */
    private String areasName;
    /**
     * 功能名称
     */
    private String className;
    /**
     * 后端目录
     */
    private String serviceDirectory;
    /**
     * 所属模块
     */
    private String module;
    /**
     * 子表名称集合
     */
    private String subClassName;


    private String formRef;
    private String formModel;
    private String size;
    private String labelPosition;
    private Integer labelWidth;
    private String formRules;
    private String drawerWidth;
    private Integer gutter;
    private Boolean disabled;
    private String span;
    private Boolean formBtns;
    private Integer idGlobal;
    private String fields;
    private String popupType;
    private String fullScreenWidth;
    private String formStyle;
    private String generalWidth;
    private Boolean hasCancelBtn;
    private String cancelButtonText;
    private Boolean hasConfirmBtn;
    private String confirmButtonText;
    private Boolean hasPrintBtn;
    private String printButtonText;

    private String printId;

    private FieLdsModel children;
}
