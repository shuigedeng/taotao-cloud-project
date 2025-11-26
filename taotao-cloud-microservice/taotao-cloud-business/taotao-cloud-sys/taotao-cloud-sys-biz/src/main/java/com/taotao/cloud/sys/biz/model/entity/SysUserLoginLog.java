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

package com.taotao.cloud.sys.biz.model.entity;
//
// import jakarta.persistence.Id;
// import jakarta.persistence.Table;
// import java.util.Date;
// import com.taotao.boot.common.utils.lang.StringUtils;
// import org.apache.commons.lang3.builder.EqualsBuilder;
// import org.apache.commons.lang3.builder.HashCodeBuilder;
// import org.springrain.frame.annotation.WhereSQL;
// import org.springrain.frame.entity.BaseEntity;
//
/// **
// * 系统用户登录日志
// */
// @Table(name = "t_permissions_log")
// public class SysUserLoginLog extends BaseEntity {
//
//    private static final long serialVersionUID = 1L;
//
//    // alias
//    /*
//     * public static final String TABLE_ALIAS = "权限变更日志"; public static final String
//     * ALIAS_ID = "主键"; public static final String ALIAS_SITEID = "站点ID"; public
//     * static final String ALIAS_ACTIONTYPE = "操作类型 创建、编辑、删除、启用、禁用"; public static
//     * final String ALIAS_OPERATORUSERID = "操作人ID"; public static final String
//     * ALIAS_OPERATORUSERNAME = "操作人当时名称"; public static final String
//     * ALIAS_OPERATORUSERROLES = "操作人当时所属角色名称，逗号分割"; public static final String
//     * ALIAS_OPERATOROBJECTID = "操作对象ID"; public static final String
//     * ALIAS_OPERATOROBJECTNAME = "操作对象当时的名称"; public static final String
//     * ALIAS_ACTIONCONTENT = "操作内容详情"; public static final String ALIAS_CREATEUSERID
//     * = "记录创建人"; public static final String ALIAS_CREATETIME = "记录创建时间"; public
//     * static final String ALIAS_BAK1 = "备用字段"; public static final String
//     * ALIAS_BAK2 = "备用字段"; public static final String ALIAS_BAK3 = "备用字段"; public
//     * static final String ALIAS_BAK4 = "备用字段"; public static final String
//     * ALIAS_BAK5 = "备用字段";
//     */
//    // date formats
//    // public static final String FORMAT_CREATETIME = DateUtils.DATETIME_FORMAT;
//
//    // columns START
//    /**
//     * 主键
//     */
//    private String id;
//    /**
//     * 站点ID
//     */
//    private String siteId;
//    /**
//     * 操作类型 创建、编辑、删除、启用、禁用
//     */
//    private Integer actionType;
//    /**
//     * 操作人ID
//     */
//    private String operatorUserId;
//    /**
//     * 操作人当时名称
//     */
//    private String operatorUserName;
//    /**
//     * 操作人当时所属角色名称，逗号分割
//     */
//    private String operatorUserRoles;
//
//    /**
//     * 操作对象类型
//     */
//    private Integer operatorObjectType;
//
//    /**
//     * 操作对象ID
//     */
//    private String operatorObjectId;
//    /**
//     * 操作对象当时的名称
//     */
//    private String operatorObjectName;
//    /**
//     * 操作内容详情
//     */
//    private String actionContent;
//    /**
//     * 记录创建人
//     */
//    private String createUserId;
//    /**
//     * 记录创建时间
//     */
//    private Date createTime;
//    /**
//     * 备用字段
//     */
//    private String bak1;
//    /**
//     * 备用字段
//     */
//    private String bak2;
//    /**
//     * 备用字段
//     */
//    private String bak3;
//    /**
//     * 备用字段
//     */
//    private String bak4;
//    /**
//     * 备用字段
//     */
//    private String bak5;
//    // columns END 数据库字段结束
//
//    // concstructor
//
//    public SysUserLoginLog() {
//    }
//
//    public SysUserLoginLog(String id) {
//        this.id = id;
//    }
//
//    public SysUserLoginLog(String siteId, Integer actionType, String operatorUserId, String
// operatorUserName,
//                          String operatorUserRoles, Integer operatorObjectType, String
// operatorObjectId, String operatorObjectName,
//                          String actionContent, String createUserId, Date createTime) {
//        super();
//        this.siteId = siteId;
//        this.actionType = actionType;
//        this.operatorUserId = operatorUserId;
//        this.operatorUserName = operatorUserName;
//        this.operatorUserRoles = operatorUserRoles;
//        this.operatorObjectType = operatorObjectType;
//        this.operatorObjectId = operatorObjectId;
//        this.operatorObjectName = operatorObjectName;
//        this.actionContent = actionContent;
//        this.createUserId = createUserId;
//        this.createTime = createTime;
//    }
//
//    public SysUserLoginLog(String siteId, Integer actionType, Integer operatorObjectType, String
// operatorObjectId,
//                          String operatorObjectName, String actionContent) {
//        super();
//        this.siteId = siteId;
//        this.actionType = actionType;
//        this.operatorObjectType = operatorObjectType;
//        this.operatorObjectId = operatorObjectId;
//        this.operatorObjectName = operatorObjectName;
//        this.actionContent = actionContent;
//    }
//
//    // get and set
//
//    /**
//     * 主键
//     */
//    @Id
//    @WhereSQL(sql = "id=:PermissionsLog_id")
//    public String getId() {
//        return this.id;
//    }
//
//    /**
//     * 主键
//     */
//    public void setId(String value) {
//        if (StringUtils.isNotBlank(value)) {
//            value = value.trim();
//        }
//        this.id = value;
//    }
//
//    /**
//     * 站点ID
//     */
//    @WhereSQL(sql = "siteId=:PermissionsLog_siteId")
//    public String getSiteId() {
//        return this.siteId;
//    }
//
//    /**
//     * 站点ID
//     */
//    public void setSiteId(String value) {
//        if (StringUtils.isNotBlank(value)) {
//            value = value.trim();
//        }
//        this.siteId = value;
//    }
//
//    /**
//     * 操作类型 创建、编辑、删除、启用、禁用
//     */
//    @WhereSQL(sql = "actionType=:PermissionsLog_actionType")
//    public Integer getActionType() {
//        return this.actionType;
//    }
//
//    /**
//     * 操作类型 创建、编辑、删除、启用、禁用
//     */
//    public void setActionType(Integer value) {
//        this.actionType = value;
//    }
//
//    /**
//     * 操作人ID
//     */
//    @WhereSQL(sql = "operatorUserId=:PermissionsLog_operatorUserId")
//    public String getOperatorUserId() {
//        return this.operatorUserId;
//    }
//
//    /**
//     * 操作人ID
//     */
//    public void setOperatorUserId(String value) {
//        if (StringUtils.isNotBlank(value)) {
//            value = value.trim();
//        }
//        this.operatorUserId = value;
//    }
//
//    /**
//     * 操作人当时名称
//     */
//    @WhereSQL(sql = "operatorUserName=:PermissionsLog_operatorUserName")
//    public String getOperatorUserName() {
//        return this.operatorUserName;
//    }
//
//    /**
//     * 操作人当时名称
//     */
//    public void setOperatorUserName(String value) {
//        if (StringUtils.isNotBlank(value)) {
//            value = value.trim();
//        }
//        this.operatorUserName = value;
//    }
//
//    /**
//     * 操作人当时所属角色名称，逗号分割
//     */
//    @WhereSQL(sql = "operatorUserRoles=:PermissionsLog_operatorUserRoles")
//    public String getOperatorUserRoles() {
//        return this.operatorUserRoles;
//    }
//
//    /**
//     * 操作人当时所属角色名称，逗号分割
//     */
//    public void setOperatorUserRoles(String value) {
//        if (StringUtils.isNotBlank(value)) {
//            value = value.trim();
//        }
//        this.operatorUserRoles = value;
//    }
//
//    /**
//     * 操作对象类型
//     *
//     * @return
//     */
//    public Integer getOperatorObjectType() {
//        return operatorObjectType;
//    }
//
//    public void setOperatorObjectType(Integer operatorObjectType) {
//        this.operatorObjectType = operatorObjectType;
//    }
//
//    /**
//     * 操作对象类型
//     */
//    @WhereSQL(sql = "operatorObjectId=:PermissionsLog_operatorObjectId")
//    public String getOperatorObjectId() {
//        return this.operatorObjectId;
//    }
//
//    /**
//     * 操作对象类型
//     */
//    public void setOperatorObjectId(String value) {
//        if (StringUtils.isNotBlank(value)) {
//            value = value.trim();
//        }
//        this.operatorObjectId = value;
//    }
//
//    /**
//     * 操作对象当时的名称
//     */
//    @WhereSQL(sql = "operatorObjectName=:PermissionsLog_operatorObjectName")
//    public String getOperatorObjectName() {
//        return this.operatorObjectName;
//    }
//
//    /**
//     * 操作对象当时的名称
//     */
//    public void setOperatorObjectName(String value) {
//        if (StringUtils.isNotBlank(value)) {
//            value = value.trim();
//        }
//        this.operatorObjectName = value;
//    }
//
//    /**
//     * 操作内容详情
//     */
//    @WhereSQL(sql = "actionContent=:PermissionsLog_actionContent")
//    public String getActionContent() {
//        return this.actionContent;
//    }
//
//    /**
//     * 操作内容详情
//     */
//    public void setActionContent(String value) {
//        if (StringUtils.isNotBlank(value)) {
//            value = value.trim();
//        }
//        this.actionContent = value;
//    }
//
//    /**
//     * 记录创建人
//     */
//    @WhereSQL(sql = "createUserId=:PermissionsLog_createUserId")
//    public String getCreateUserId() {
//        return this.createUserId;
//    }
//
//    /**
//     * 记录创建人
//     */
//    public void setCreateUserId(String value) {
//        if (StringUtils.isNotBlank(value)) {
//            value = value.trim();
//        }
//        this.createUserId = value;
//    }
//    /*
//     * public String getcreateTimeString() { return
//     * DateUtils.convertDate2String(FORMAT_CREATETIME, getcreateTime()); } public
//     * void setcreateTimeString(String value) throws ParseException{
//     * setcreateTime(DateUtils.convertString2Date(FORMAT_CREATETIME,value)); }
//     */
//
//    /**
//     * 记录创建时间
//     */
//    @WhereSQL(sql = "createTime=:PermissionsLog_createTime")
//    public Date getCreateTime() {
//        return this.createTime;
//    }
//
//    /**
//     * 记录创建时间
//     */
//    public void setCreateTime(Date value) {
//        this.createTime = value;
//    }
//
//    /**
//     * 备用字段
//     */
//    @WhereSQL(sql = "bak1=:PermissionsLog_bak1")
//    public String getBak1() {
//        return this.bak1;
//    }
//
//    /**
//     * 备用字段
//     */
//    public void setBak1(String value) {
//        if (StringUtils.isNotBlank(value)) {
//            value = value.trim();
//        }
//        this.bak1 = value;
//    }
//
//    /**
//     * 备用字段
//     */
//    @WhereSQL(sql = "bak2=:PermissionsLog_bak2")
//    public String getBak2() {
//        return this.bak2;
//    }
//
//    /**
//     * 备用字段
//     */
//    public void setBak2(String value) {
//        if (StringUtils.isNotBlank(value)) {
//            value = value.trim();
//        }
//        this.bak2 = value;
//    }
//
//    /**
//     * 备用字段
//     */
//    @WhereSQL(sql = "bak3=:PermissionsLog_bak3")
//    public String getBak3() {
//        return this.bak3;
//    }
//
//    /**
//     * 备用字段
//     */
//    public void setBak3(String value) {
//        if (StringUtils.isNotBlank(value)) {
//            value = value.trim();
//        }
//        this.bak3 = value;
//    }
//
//    /**
//     * 备用字段
//     */
//    @WhereSQL(sql = "bak4=:PermissionsLog_bak4")
//    public String getBak4() {
//        return this.bak4;
//    }
//
//    /**
//     * 备用字段
//     */
//    public void setBak4(String value) {
//        if (StringUtils.isNotBlank(value)) {
//            value = value.trim();
//        }
//        this.bak4 = value;
//    }
//
//    /**
//     * 备用字段
//     */
//    @WhereSQL(sql = "bak5=:PermissionsLog_bak5")
//    public String getBak5() {
//        return this.bak5;
//    }
//
//    /**
//     * 备用字段
//     */
//    public void setBak5(String value) {
//        if (StringUtils.isNotBlank(value)) {
//            value = value.trim();
//        }
//        this.bak5 = value;
//    }
//
//    @Override
//    public String toString() {
//        return new
// StringBuilder().append("主键[").append(getId()).append("],").append("站点ID[").append(getSiteId())
//                .append("],").append("操作类型
// 创建、编辑、删除、启用、禁用[").append(getActionType()).append("],").append("操作人ID[")
//
// .append(getOperatorUserId()).append("],").append("操作人当时名称[").append(getOperatorUserName()).append("],")
//
// .append("操作人当时所属角色名称，逗号分割[").append(getOperatorUserRoles()).append("],").append("操作对象类型[")
//
// .append(getOperatorObjectId()).append("],").append("操作对象当时的名称[").append(getOperatorObjectName())
//
// .append("],").append("操作内容详情[").append(getActionContent()).append("],").append("记录创建人[")
//
// .append(getCreateUserId()).append("],").append("记录创建时间[").append(getCreateTime()).append("],")
//
// .append("备用字段[").append(getBak1()).append("],").append("备用字段[").append(getBak2()).append("],")
//
// .append("备用字段[").append(getBak3()).append("],").append("备用字段[").append(getBak4()).append("],")
//                .append("备用字段[").append(getBak5()).append("],").toString();
//    }
//
//    @Override
//    public int hashCode() {
//        return new HashCodeBuilder().append(getId()).toHashCode();
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj instanceof SysUserLoginLog == false) {
//            return false;
//        }
//
//        if (this == obj) {
//            return true;
//        }
//
//        SysUserLoginLog other = (SysUserLoginLog) obj;
//        return new EqualsBuilder().append(getId(), other.getId()).isEquals();
//    }
// }
