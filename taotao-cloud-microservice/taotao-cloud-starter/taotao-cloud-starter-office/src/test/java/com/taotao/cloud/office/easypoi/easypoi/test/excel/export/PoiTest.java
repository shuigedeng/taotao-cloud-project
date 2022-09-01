package com.taotao.cloud.office.easypoi.easypoi.test.excel.export;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.handler.inter.IExcelExportServer;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Description: TODO
 *
 * @author wxy
 * @version 1.0
 * @date 2020/9/15 11:08
 */
public class PoiTest {
    @Test
    public void test() throws Exception {
        Workbook     workbook = null;
        ExportParams params   = new ExportParams();
        params.setType(ExcelType.XSSF);
        final int[] nums = {100};
        workbook = ExcelExportUtil.exportBigExcel(params, CxFeeEO.class, new IExcelExportServer() {
            @Override
            public List<Object> selectListForExcelExport(Object queryParams, int page) {
                nums[0]--;
                if (nums[0] > 0) {
                    return getList();
                }
                return null;
            }
        }, null);

        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }

        FileOutputStream fos = new FileOutputStream("D:/home/excel/ExcelExportBigData.bigDataExport.xlsx");
        workbook.write(fos);
        fos.close();
    }

    private List<Object> getList() {
        List<Object> feeEOS  = Lists.newArrayList();
        CxFeeEO      cxFeeEO = new CxFeeEO();
        cxFeeEO.setFeeNo("FD20090200000848");
        cxFeeEO.setSupplierCode("211105");
        cxFeeEO.setSupplierName("xxx有限公司");
        cxFeeEO.setAmount(new BigDecimal("96.322000"));
        cxFeeEO.setAmountTypeName("含税");
        cxFeeEO.setDiscountWay(1);
        cxFeeEO.setDiscountWayName("票扣");
        cxFeeEO.setRebateType("FEE26");
        cxFeeEO.setRebateDesc("一次性库补");
        cxFeeEO.setFeeStatusName("已执行");
        cxFeeEO.setCollectDate(new Date());
        cxFeeEO.setCategoryMi("U49");
        cxFeeEO.setSource("库补单据.xlsx");
        cxFeeEO.setUserInfo("马处");
        cxFeeEO.setText("");


        List<CxFeeEO.CxFeeItemEO> itemEOS = Lists.newArrayList();
        for (int i = 0; i < 200; i++) {
            CxFeeEO.CxFeeItemEO cxFeeItemEO = new CxFeeEO.CxFeeItemEO();
            cxFeeItemEO.setGoodsCode("610030");
            cxFeeItemEO.setGoodsName("610030");
            cxFeeItemEO.setShopCode("1200");
            cxFeeItemEO.setShopName("某店");
            cxFeeItemEO.setAmount(new BigDecimal("88.00000"));
            cxFeeItemEO.setQuantity(new BigDecimal("1.00000"));
            cxFeeItemEO.setRemark("55");

            itemEOS.add(cxFeeItemEO);
        }
        cxFeeEO.setItems(itemEOS);

        feeEOS.add(cxFeeEO);
        return feeEOS;
    }
}
