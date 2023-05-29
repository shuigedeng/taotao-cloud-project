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

package com.taotao.cloud.store.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.common.utils.bean.BeanUtils;
import com.taotao.cloud.store.api.model.dto.AdminStoreApplyDTO;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import com.taotao.cloud.web.base.entity.JpaEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;

/** 店铺详细 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = StoreDetail.TABLE_NAME)
@TableName(StoreDetail.TABLE_NAME)
@EntityListeners({JpaEntityListener.class})
// @org.hibernate.annotations.Table(appliesTo = StoreDetail.TABLE_NAME, comment = "店铺详细表")
public class StoreDetail extends BaseSuperEntity<StoreDetail, String> {

    public static final String TABLE_NAME = "tt_store_detail";

    @NotBlank(message = "店铺不能为空")
    @Column(name = "store_id", columnDefinition = "varchar(64) not null comment '店铺id'")
    private String storeId;

    @Size(min = 2, max = 200, message = "店铺名称长度为2-200位")
    @NotBlank(message = "店铺名称不能为空")
    @Column(name = "store_name", columnDefinition = "varchar(64) not null comment '店铺名称'")
    private String storeName;

    @NotBlank(message = "公司名称不能为空")
    @Size(min = 2, max = 100, message = "公司名称错误")
    @Column(name = "company_name", columnDefinition = "varchar(64) not null comment '公司名称'")
    private String companyName;

    @NotBlank(message = "公司地址不能为空")
    @Size(min = 1, max = 200, message = "公司地址,长度为1-200字符")
    @Column(name = "company_address", columnDefinition = "varchar(64) not null comment '公司地址'")
    private String companyAddress;

    @Column(name = "company_address_id_path", columnDefinition = "varchar(64) not null comment '公司地址地区Id'")
    private String companyAddressIdPath;

    @Column(name = "company_address_path", columnDefinition = "varchar(64) not null comment '公司地址地区'")
    private String companyAddressPath;

    @Column(name = "company_phone", columnDefinition = "varchar(64) not null comment '公司电话'")
    private String companyPhone;

    @Column(name = "company_email", columnDefinition = "varchar(64) not null comment '电子邮箱'")
    private String companyEmail;

    @Min(value = 1, message = "员工总数,至少一位")
    @Column(name = "employee_num", columnDefinition = "int not null default 0 comment '员工总数'")
    private Integer employeeNum;

    @Min(value = 1, message = "注册资金,至少一位")
    @Column(name = "registered_capital", columnDefinition = "decimal(10,2) not null default 0 comment '注册资金'")
    private BigDecimal registeredCapital;

    @Length(min = 2, max = 20, message = "联系人长度为：2-20位字符")
    @Column(name = "link_name", columnDefinition = "varchar(64) not null comment '联系人姓名'")
    private String linkName;

    @Column(name = "link_phone", columnDefinition = "varchar(64) not null comment '联系人电话'")
    private String linkPhone;

    @Size(min = 18, max = 18, message = "营业执照长度为18位字符")
    @Column(name = "license_num", columnDefinition = "varchar(64) not null comment '营业执照号'")
    private String licenseNum;

    @Size(min = 1, max = 200, message = "法定经营范围长度为1-200位字符")
    @Column(name = "scope", columnDefinition = "varchar(64) not null comment '法定经营范围'")
    private String scope;

    @Column(name = "licence_photo", columnDefinition = "varchar(64) not null comment '营业执照电子版'")
    private String licencePhoto;

    @Size(min = 2, max = 20, message = "法人姓名长度为2-20位字符")
    @Column(name = "legal_name", columnDefinition = "varchar(64) not null comment '法人姓名'")
    private String legalName;

    @Size(min = 18, max = 18, message = "法人身份证号长度为18位")
    @Column(name = "legal_id", columnDefinition = "varchar(64) not null comment '法人身份证'")
    private String legalId;

    @Column(name = "legal_photo", columnDefinition = "varchar(64) not null comment '法人身份证照片'")
    private String legalPhoto;

    @Column(name = "settlement_bank_account_name", columnDefinition = "varchar(64) not null comment '结算银行开户行名称'")
    private String settlementBankAccountName;

    @Column(name = "settlement_bank_account_num", columnDefinition = "varchar(64) not null comment '结算银行开户账号'")
    private String settlementBankAccountNum;

    @Column(name = "settlement_bank_branch_name", columnDefinition = "varchar(64) not null comment '结算银行开户支行名称'")
    private String settlementBankBranchName;

    @Column(name = "settlement_bank_joint_name", columnDefinition = "varchar(64) not null comment '结算银行支行联行号'")
    private String settlementBankJointName;

    @Column(name = "goods_management_category", columnDefinition = "varchar(64) not null comment '店铺经营类目'")
    private String goodsManagementCategory;

    @Column(name = "settlement_cycle", columnDefinition = "varchar(64) not null comment '结算周期'")
    private String settlementCycle;

    @Column(name = "settlement_day", columnDefinition = "TIMESTAMP comment '结算日'")
    private LocalDateTime settlementDay;

    @Column(name = "stock_warning", columnDefinition = "int not null default 0 comment '库存预警数量'")
    private Integer stockWarning;

    @Column(name = "dd_code", columnDefinition = "varchar(64) not null comment '同城配送达达店铺编码'")
    private String ddCode;

    // 店铺退货收件地址
    @Column(name = "sales_consignee_name", columnDefinition = "varchar(64) not null comment '收货人姓名'")
    private String salesConsigneeName;

    @Column(name = "sales_consignee_mobile", columnDefinition = "varchar(64) not null comment '收件人手机'")
    private String salesConsigneeMobile;

    @Column(name = "sales_consignee_address_id", columnDefinition = "varchar(64) not null comment '地址Id 逗号分割'")
    private String salesConsigneeAddressId;

    @Column(name = "sales_consignee_address_path", columnDefinition = "varchar(64) not null comment '地址名称 逗号分割'")
    private String salesConsigneeAddressPath;

    @Column(name = "sales_consignee_detail", columnDefinition = "varchar(64) not null comment '详细地址'")
    private String salesConsigneeDetail;

    public StoreDetail(Store store, AdminStoreApplyDTO adminStoreApplyDTO) {
        this.storeId = store.getId();
        // 设置店铺公司信息、设置店铺银行信息、设置店铺其他信息
        BeanUtils.copyProperties(adminStoreApplyDTO, this);
        this.settlementDay = LocalDateTime.now();
        this.stockWarning = 10;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        StoreDetail dict = (StoreDetail) o;
        return getId() != null && Objects.equals(getId(), dict.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
