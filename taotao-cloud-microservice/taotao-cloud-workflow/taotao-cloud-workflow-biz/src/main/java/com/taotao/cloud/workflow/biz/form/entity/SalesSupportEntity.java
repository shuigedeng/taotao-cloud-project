package com.taotao.cloud.workflow.biz.form.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 销售支持表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
 */
@Data
@TableName("wform_salessupport")
public class SalesSupportEntity {
    /**
     * 主键
     */
    @TableId("F_ID")
    private String id;

    /**
     * 流程主键
     */
    @TableField("F_FLOWID")
    private String flowId;

    /**
     * 流程标题
     */
    @TableField("F_FLOWTITLE")
    private String flowTitle;

    /**
     * 紧急程度
     */
    @TableField("F_FLOWURGENT")
    private Integer flowUrgent;

    /**
     * 流程单据
     */
    @TableField("F_BILLNO")
    private String billNo;

    /**
     * 申请人
     */
    @TableField("F_APPLYUSER")
    private String applyUser;

    /**
     * 申请日期
     */
    @TableField("F_APPLYDATE")
    private Date applyDate;

    /**
     * 申请部门
     */
    @TableField("F_APPLYDEPT")
    private String applyDept;

    /**
     * 相关客户
     */
    @TableField("F_CUSTOMER")
    private String customer;

    /**
     * 相关项目
     */
    @TableField("F_PROJECT")
    private String project;

    /**
     * 售前支持
     */
    @TableField("F_PSALESUPINFO")
    private String pSaleSupInfo;

    /**
     * 开始时间
     */
    @TableField("F_STARTDATE")
    private Date startDate;

    /**
     * 结束日期
     */
    @TableField("F_ENDDATE")
    private Date endDate;

    /**
     * 支持天数
     */
    @TableField("F_PSALESUPDAYS")
    private String pSaleSupDays;

    /**
     * 准备天数
     */
    @TableField("F_PSALEPREDAYS")
    private String pSalePreDays;

    /**
     * 机构咨询
     */
    @TableField("F_CONSULMANAGER")
    private String consulManager;

    /**
     * 售前顾问
     */
    @TableField("F_PSALSUPCONSUL")
    private String pSalSupConsul;

    /**
     * 相关附件
     */
    @TableField("F_FILEJSON")
    private String fileJson;

    /**
     * 销售总结
     */
    @TableField("F_SALSUPCONCLU")
    private String salSupConclu;

    /**
     * 交付说明
     */
    @TableField("F_CONSULTRESULT")
    private String consultResult;

    /**
     * 咨询评价
     */
    @TableField("F_IEVALUATION")
    private String iEvaluation;

    /**
     * 发起人总结
     */
    @TableField("F_CONCLUSION")
    private String conclusion;
}
