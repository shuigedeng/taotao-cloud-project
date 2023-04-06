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

package com.taotao.cloud.store.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 店铺基础VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:20:08
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺基础VO")
public class StoreDetailVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7605952923416404638L;

    @Schema(description = "唯一标识")
    private String id;

    @Schema(description = "店铺id")
    private String storeId;

    @Schema(description = "店铺名称")
    private String storeName;

    @Schema(description = "公司名称")
    private String companyName;

    @Schema(description = "公司地址")
    private String companyAddress;

    @Schema(description = "公司地址地区Id")
    private String companyAddressIdPath;

    @Schema(description = "公司地址地区")
    private String companyAddressPath;

    @Schema(description = "公司电话")
    private String companyPhone;

    @Schema(description = "电子邮箱")
    private String companyEmail;

    @Schema(description = "员工总数")
    private Integer employeeNum;

    @Schema(description = "注册资金")
    private BigDecimal registeredCapital;

    @Schema(description = "联系人姓名")
    private String linkName;

    @Schema(description = "联系人电话")
    private String linkPhone;

    private String licenseNum;

    @Schema(description = "法定经营范围")
    private String scope;

    @Schema(description = "营业执照电子版")
    private String licencePhoto;

    @Schema(description = "法人姓名")
    private String legalName;

    @Schema(description = "法人身份证")
    private String legalId;

    @Schema(description = "法人身份证照片")
    private String legalPhoto;

    @Schema(description = "结算银行开户行名称")
    private String settlementBankAccountName;

    @Schema(description = "结算银行开户账号")
    private String settlementBankAccountNum;

    @Schema(description = "结算银行开户支行名称")
    private String settlementBankBranchName;

    @Schema(description = "结算银行支行联行号")
    private String settlementBankJointName;

    @Schema(description = "店铺经营类目")
    private String goodsManagementCategory;

    @Schema(description = "结算周期")
    private String settlementCycle;

    @Schema(description = "库存预警数量")
    private Integer stockWarning;

    @Schema(description = "同城配送达达店铺编码")
    private String ddCode;

    // 店铺退货收件地址
    @Schema(description = "收货人姓名")
    private String salesConsigneeName;

    @Schema(description = "收件人手机")
    private String salesConsigneeMobile;

    @Schema(description = "地址Id， '，'分割")
    private String salesConsigneeAddressId;

    @Schema(description = "地址名称， '，'分割")
    private String salesConsigneeAddressPath;

    @Schema(description = "详细地址")
    private String salesConsigneeDetail;

    @Schema(description = "店铺状态")
    private String storeDisable;

    @Schema(description = "是否自营", required = true)
    private Boolean selfOperated;

    @Schema(description = "经纬度")
    private String storeCenter;

    @Schema(description = "店铺logo")
    private String storeLogo;

    @Schema(description = "店铺简介")
    private String storeDesc;

    @Schema(description = "地址名称， '，'分割")
    private String storeAddressPath;

    @Schema(description = "地址id，'，'分割 ")
    private String storeAddressIdPath;

    @Schema(description = "详细地址")
    private String storeAddressDetail;

    @Schema(description = "腾讯云智服唯一标识")
    private String yzfSign;

    @Schema(description = "腾讯云智服小程序唯一标识")
    private String yzfMpSign;
}
