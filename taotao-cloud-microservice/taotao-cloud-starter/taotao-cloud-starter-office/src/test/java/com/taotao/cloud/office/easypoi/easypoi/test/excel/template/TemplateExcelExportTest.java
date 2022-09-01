package com.taotao.cloud.office.easypoi.easypoi.test.excel.template;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import cn.afterturn.easypoi.test.entity.temp.BudgetAccountsEntity;
import cn.afterturn.easypoi.test.entity.temp.PayeeEntity;
import cn.afterturn.easypoi.test.entity.temp.TemplateExcelExportEntity;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;

public class TemplateExcelExportTest {

    @Test
    public void test() throws Exception {
        TemplateExportParams params = new TemplateExportParams(
                "WEB-INF/doc/专项支出用款申请书.xls");
        params.setHeadingStartRow(3);
        params.setHeadingRows(2);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", "2014-12-25");
        map.put("money", 2000000.00);
        map.put("upperMoney", "贰佰万");
        map.put("company", "执笔潜行科技有限公司");
        map.put("bureau", "财政局");
        map.put("person", "JueYue");
        map.put("phone", "1879740****");

        List<TemplateExcelExportEntity> list = new ArrayList<TemplateExcelExportEntity>();

        for (int i = 0; i < 4; i++) {
            TemplateExcelExportEntity entity = new TemplateExcelExportEntity();
            entity.setIndex(i + 1 + "");
            entity.setAccountType("开源项目");
            entity.setProjectName("EasyPoi " + i + "期");
            entity.setAmountApplied(i * 10000 + "");
            entity.setApprovedAmount((i + 1) * 10000 - 100 + "");
            List<BudgetAccountsEntity> budgetAccounts = Lists.newArrayList();
            for (int j = 0; j < 1; j++) {
                BudgetAccountsEntity accountsEntity = new BudgetAccountsEntity();
                accountsEntity.setCode("A001");
                accountsEntity.setName("设计");
                budgetAccounts.add(accountsEntity);
                accountsEntity = new BudgetAccountsEntity();
                accountsEntity.setCode("A002");
                accountsEntity.setName("开发");
                budgetAccounts.add(accountsEntity);
            }
            entity.setBudgetAccounts(budgetAccounts);
            PayeeEntity payeeEntity = new PayeeEntity();
            payeeEntity.setBankAccount("6222 0000 1234 1234");
            payeeEntity.setBankName("中国银行");
            payeeEntity.setName("小明");
            entity.setPayee(payeeEntity);
            list.add(entity);
        }

        Workbook workbook = ExcelExportUtil.exportExcel(params, TemplateExcelExportEntity.class,
            list, map);
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/专项支出用款申请书.xls");
        workbook.write(fos);
        fos.close();
    }

    @Test
    public void fe_map() throws Exception {
        TemplateExportParams params = new TemplateExportParams(
                "WEB-INF/doc/专项支出用款申请书_map.xls");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", "2014-12-25");
        map.put("money", 2000000.00);
        map.put("upperMoney", "贰佰万");
        map.put("company", "执笔潜行科技有限公司");
        map.put("bureau", "财政局");
        map.put("person", "JueYue");
        map.put("phone", "1879740****");
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (int i = 0; i < 4; i++) {
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("id", i + 1 + "");
            lm.put("zijin", i * 10000 + "");
            lm.put("bianma", "A001");
            lm.put("mingcheng", "设计");
            lm.put("xiangmumingcheng", "EasyPoi " + i + "期");
            lm.put("quancheng", "开源项目");
            lm.put("sqje", i * 10000 + "");
            lm.put("hdje", i * 10000 + "");

            listMap.add(lm);
        }
        map.put("maplist", listMap);

        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/专项支出用款申请书_map.xls");
        workbook.write(fos);
        fos.close();
    }

    @Test
    public void test2() throws Exception {
        TemplateExportParams params = new TemplateExportParams(
            "doc/merge_test.xls");
        Map<String, Object> map = new HashMap<String, Object>();

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        for (int i = 0; i < 8; i++) {
            Map<String, String> m = new HashMap<String, String>();
            m.put("id", "id" + "1");
            m.put("uname", "name" + "1");
            m.put("amount", i + "");
            list.add(m);
        }
        map.put("list", list);
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/merge_test.xls");
        workbook.write(fos);
        fos.close();
    }

}
