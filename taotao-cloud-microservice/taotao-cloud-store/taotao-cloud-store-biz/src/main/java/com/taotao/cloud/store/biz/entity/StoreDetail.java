package com.taotao.cloud.store.biz.entity;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.store.api.dto.AdminStoreApplyDTO;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * 店铺详细
 *
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = StoreDetail.TABLE_NAME)
@TableName(StoreDetail.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = StoreDetail.TABLE_NAME, comment = "店铺详细表")
public class StoreDetail extends BaseSuperEntity<StoreDetail, Long> {

	public static final String TABLE_NAME = "tt_store_detail";

	@NotBlank(message = "店铺不能为空")
	@Column(name = "store_id", nullable = false, columnDefinition = "varchar(64) not null comment '店铺id'")
	private String storeId;

	@Size(min = 2, max = 200, message = "店铺名称长度为2-200位")
	@NotBlank(message = "店铺名称不能为空")
	@Column(name = "store_name", nullable = false, columnDefinition = "varchar(64) not null comment '店铺名称'")
	private String storeName;

	@NotBlank(message = "公司名称不能为空")
	@Size(min = 2, max = 100, message = "公司名称错误")
	@Column(name = "company_name", nullable = false, columnDefinition = "varchar(64) not null comment '公司名称'")
	private String companyName;

	@NotBlank(message = "公司地址不能为空")
	@Size(min = 1, max = 200, message = "公司地址,长度为1-200字符")
	@Column(name = "company_address", nullable = false, columnDefinition = "varchar(64) not null comment '公司地址'")
	private String companyAddress;

	@Column(name = "company_address_id_path", nullable = false, columnDefinition = "varchar(64) not null comment '公司地址地区Id'")
	private String companyAddressIdPath;

	@Column(name = "company_address_path", nullable = false, columnDefinition = "varchar(64) not null comment '公司地址地区'")
	private String companyAddressPath;

	@Column(name = "company_phone", nullable = false, columnDefinition = "varchar(64) not null comment '公司电话'")
	private String companyPhone;

	@Column(name = "company_email", nullable = false, columnDefinition = "varchar(64) not null comment '电子邮箱'")
	private String companyEmail;

	@Min(value = 1, message = "员工总数,至少一位")
	@Column(name = "employee_num", nullable = false, columnDefinition = "int not null default 0 comment '员工总数'")
	private Integer employeeNum;

	@Min(value = 1, message = "注册资金,至少一位")
	@Column(name = "registered_capital", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '注册资金'")
	private BigDecimal registeredCapital;

	@Length(min = 2, max = 20, message = "联系人长度为：2-20位字符")
	@Column(name = "link_name", nullable = false, columnDefinition = "varchar(64) not null comment '联系人姓名'")
	private String linkName;

	@Column(name = "link_phone", nullable = false, columnDefinition = "varchar(64) not null comment '联系人电话'")
	private String linkPhone;

	@Size(min = 18, max = 18, message = "营业执照长度为18位字符")
	@Column(name = "license_num", nullable = false, columnDefinition = "varchar(64) not null comment '营业执照号'")
	private String licenseNum;

	@Size(min = 1, max = 200, message = "法定经营范围长度为1-200位字符")
	@Column(name = "scope", nullable = false, columnDefinition = "varchar(64) not null comment '法定经营范围'")
	private String scope;

	@Column(name = "licence_photo", nullable = false, columnDefinition = "varchar(64) not null comment '营业执照电子版'")
	private String licencePhoto;

	@Size(min = 2, max = 20, message = "法人姓名长度为2-20位字符")
	@Column(name = "legal_name", nullable = false, columnDefinition = "varchar(64) not null comment '法人姓名'")
	private String legalName;

	@Size(min = 18, max = 18, message = "法人身份证号长度为18位")
	@Column(name = "legal_id", nullable = false, columnDefinition = "varchar(64) not null comment '法人身份证'")
	private String legalId;

	@Column(name = "legal_photo", nullable = false, columnDefinition = "varchar(64) not null comment '法人身份证照片'")
	private String legalPhoto;

	@Column(name = "settlement_bank_account_name", nullable = false, columnDefinition = "varchar(64) not null comment '结算银行开户行名称'")
	private String settlementBankAccountName;

	@Column(name = "settlement_bank_account_num", nullable = false, columnDefinition = "varchar(64) not null comment '结算银行开户账号'")
	private String settlementBankAccountNum;

	@Column(name = "settlement_bank_branch_name", nullable = false, columnDefinition = "varchar(64) not null comment '结算银行开户支行名称'")
	private String settlementBankBranchName;

	@Column(name = "settlement_bank_joint_name", nullable = false, columnDefinition = "varchar(64) not null comment '结算银行支行联行号'")
	private String settlementBankJointName;

	@Column(name = "goods_management_category", nullable = false, columnDefinition = "varchar(64) not null comment '店铺经营类目'")
	private String goodsManagementCategory;

	@Column(name = "settlement_cycle", nullable = false, columnDefinition = "varchar(64) not null comment '结算周期'")
	private String settlementCycle;

	@Column(name = "settlement_day", nullable = false, columnDefinition = "TIMESTAMP comment '结算日'")
	private LocalDateTime settlementDay;

	@Column(name = "stock_warning", nullable = false, columnDefinition = "int not null default 0 comment '库存预警数量'")
	private Integer stockWarning;

	@Column(name = "dd_code", nullable = false, columnDefinition = "varchar(64) not null comment '同城配送达达店铺编码'")
	private String ddCode;

	//店铺退货收件地址
	@Column(name = "sales_consignee_name", nullable = false, columnDefinition = "varchar(64) not null comment '收货人姓名'")
	private String salesConsigneeName;

	@Column(name = "sales_consignee_mobile", nullable = false, columnDefinition = "varchar(64) not null comment '收件人手机'")
	private String salesConsigneeMobile;

	@Column(name = "sales_consignee_address_id", nullable = false, columnDefinition = "varchar(64) not null comment '地址Id 逗号分割'")
	private String salesConsigneeAddressId;

	@Column(name = "sales_consignee_address_path", nullable = false, columnDefinition = "varchar(64) not null comment '地址名称 逗号分割'")
	private String salesConsigneeAddressPath;

	@Column(name = "sales_consignee_detail", nullable = false, columnDefinition = "varchar(64) not null comment '详细地址'")
	private String salesConsigneeDetail;

	//public StoreDetail(Store store, AdminStoreApplyDTO adminStoreApplyDTO) {
	//    this.storeId = store.getId();
	//    //设置店铺公司信息、设置店铺银行信息、设置店铺其他信息
	//    BeanUtil.copyProperties(adminStoreApplyDTO, this);
	//    this.settlementDay = DateUtil.date();
	//    this.stockWarning = 10;
	//}
}
