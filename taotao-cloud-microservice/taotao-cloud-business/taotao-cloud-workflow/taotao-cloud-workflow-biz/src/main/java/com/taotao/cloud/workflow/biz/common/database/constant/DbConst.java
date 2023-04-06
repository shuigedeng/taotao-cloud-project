/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.workflow.biz.common.database.constant;

/** 数据库相关静态常量 */
public class DbConst {

    /** jdbc工具类返回数据类型 */
    public static final String TABLE_FIELD_MOD = "tableFieldMod";

    public static final String MAP_MOD = "mapMods";
    public static final String INCLUDE_FIELD_MOD = "includeFieldMods";
    public static final String CUSTOM_MOD = "customMods";
    public static final String PAGE_MOD = "pageMod";

    /** url连接 */
    public static final String HOST = "{host}";

    public static final String PORT = "{port}";
    public static final String DB_NAME = "{dbname}";
    public static final String DB_SCHEMA = "{schema}";

    /** 默认表（不可删） */
    public static final String BYO_TABLE =
            "base_authorize,base_comfields,base_billrule,base_dbbackup,base_dblink,base_dictionarydata,"
                    + "base_dictionarytype,base_imcontent,base_languagemap,base_languagetype,base_menu,"
                    + "base_message,base_messagereceive,base_module,base_modulebutton,base_modulecolumn,"
                    + "base_moduledataauthorize,base_moduledataauthorizescheme,base_organize,base_position,"
                    + "base_province,base_role,base_sysconfig,base_syslog,base_timetask,base_timetasklog,"
                    + "base_user,base_userrelation,crm_busines,crm_businesproduct,crm_clue,crm_contract,"
                    + "crm_contractinvoice,crm_contractmoney,crm_contractproduct," /*crm_customer*/
                    + ",crm_customercontacts,"
                    + "crm_followlog,crm_invoice,crm_product,crm_receivable,ext_bigdata,ext_document,"
                    + "ext_documentshare,ext_emailconfig,ext_emailreceive,ext_emailsend,ext_employee,ext_order,"
                    + "ext_orderentry,ext_orderreceivable,ext_projectgantt,ext_schedule,ext_tableexample,"
                    + "ext_worklog,ext_worklogshare,flow_delegate,flow_engine,flow_engineform,flow_enginevisible,"
                    + "flow_task,flow_taskcirculate,flow_tasknode,flow_taskoperator,flow_taskoperatorrecord,"
                    + "wechat_mpeventcontent,wechat_mpmaterial,wechat_mpmessage,wechat_qydepartment,wechat_qymessage,"
                    + "wechat_qyuser,wform_applybanquet,wform_applydelivergoods,wform_applydelivergoodsentry,"
                    + "wform_applymeeting,wform_archivalborrow,wform_articleswarehous,wform_batchpack,wform_batchtable,"
                    + "wform_conbilling,wform_contractapproval,wform_contractapprovalsheet,wform_debitbill,"
                    + "wform_documentapproval,wform_documentsigning,wform_expenseexpenditure,wform_finishedproduct,"
                    + "wform_finishedproductentry,wform_incomerecognition,wform_leaveapply,wform_letterservice,"
                    + "wform_materialrequisition,wform_materialrequisitionentry,wform_monthlyreport,wform_officesupplies,"
                    + "wform_outboundorder,wform_outboundorderentry,wform_outgoingapply,wform_paydistribution,"
                    + "wform_paymentapply,wform_postbatchtab,wform_procurementmaterial,wform_procurementmaterialentry,"
                    + "wform_purchaselist,wform_purchaselistentry,wform_quotationapproval,wform_receiptprocessing,"
                    + "wform_receiptsign,wform_rewardpunishment,wform_salesorder,wform_salesorderentry,wform_salessupport,"
                    + "wform_staffovertime,wform_supplementcard,wform_travelapply,wform_travelreimbursement,wform_vehicleapply,"
                    + "wform_violationhandling,wform_warehousereceipt,wform_warehousereceiptentry,wform_workcontactsheet";
}
