package com.taotao.cloud.office.easypoi.easypoi.test.excel.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class CxFeeEO {

    @Excel(name = "单据编号", needMerge = true)
    private String feeNo;
    @Excel(name = "供商卡号", needMerge = true)
    private String supplierCode;
    @Excel(name = "供商名称", needMerge = true)
    private String supplierName;
    @Excel(name = "总金额(元)", needMerge = true)
    private BigDecimal amount;
    @Excel(name = "金额类型", needMerge = true)
    private String amountTypeName;
    @Excel(name = "支付方式编码", needMerge = true)
    private Integer discountWay;
    @Excel(name = "支付方式名称", needMerge = true)
    private String discountWayName;
    @Excel(name = "费用类型", needMerge = true)
    private String rebateType;
    @Excel(name = "费用类型名称", needMerge = true)
    private String rebateDesc;
    @Excel(name = "单据状态", needMerge = true)
    private String feeStatusName;
    @Excel(name = "计划扣费日期", needMerge = true, format = "yyyy-MM-dd")
    private Date collectDate;
    @Excel(name = "采购课组", needMerge = true)
    private String categoryMi;
    @Excel(name = "单据来源", needMerge = true)
    private String source;
    @Excel(name = "创建人", needMerge = true)
    private String userInfo;
    @Excel(name = "备注", needMerge = true)
    private String text;
    @ExcelCollection(name = "单据行项")
    private List<CxFeeItemEO> items;

    @Data
    public static class CxFeeItemEO {
        @Excel(name = "商品/项目编码")
        private String goodsCode;
        @Excel(name = "商品/项目名称")
        private String goodsName;
        @Excel(name = "门店编码")
        private String shopCode;
        @Excel(name = "门店名称")
        private String shopName;
        @Excel(name = "金额(元)")
        private BigDecimal amount;
        @Excel(name = "数量")
        private BigDecimal quantity;
        @Excel(name = "备注")
        private String remark;
    }
}
