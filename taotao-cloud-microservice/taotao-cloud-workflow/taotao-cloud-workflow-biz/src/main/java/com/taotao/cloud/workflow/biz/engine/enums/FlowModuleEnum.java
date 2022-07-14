package com.taotao.cloud.workflow.biz.engine.enums;

/**
 * 功能流程
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月26日 上午9:18
 */
public enum  FlowModuleEnum {
    //订单测试
    CRM_Order("crmOrder"),
    // CRM应用-合同
    crm_contract("crm_contract"),
    // CRM应用-回款
    crm_receivable("crm_receivable"),
    // CRM应用-发票
    crm_invoice("crm_invoice");

    private String message;

    FlowModuleEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
