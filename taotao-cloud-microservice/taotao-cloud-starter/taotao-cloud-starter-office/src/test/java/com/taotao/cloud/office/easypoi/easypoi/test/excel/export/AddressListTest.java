package com.taotao.cloud.office.easypoi.easypoi.test.excel.export;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.test.addressList.AddressListEntity;
import cn.afterturn.easypoi.test.en.Sex;
import cn.afterturn.easypoi.test.excel.handler.ExcelDiceAddressListHandlerImpl;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 下拉测试
 *
 * @author jueyue on 20-4-26.
 */
public class AddressListTest {

    @Test
    public void testOne() throws Exception {

        List<AddressListEntity> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            AddressListEntity client = new AddressListEntity();
            client.setName("小明" + i);
            client.setSex(Sex.MAN);
            client.setStatus(i % 3 + "");
            client.setBilibili(i % 3);
            list.add(client);
        }
        Date         start  = new Date();
        ExportParams params = new ExportParams("下拉测试", "测试", ExcelType.XSSF);
        params.setDictHandler(new ExcelDiceAddressListHandlerImpl());
        Workbook workbook = ExcelExportUtil.exportExcel(params, AddressListEntity.class, list);
        System.out.println(new Date().getTime() - start.getTime());
        FileOutputStream fos = new FileOutputStream("D:/home/excel/AddressListTest.testOne.xlsx");
        workbook.write(fos);
        fos.close();
    }


    @Test
    public void testOneXls() throws Exception {

        List<AddressListEntity> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            AddressListEntity client = new AddressListEntity();
            client.setName("小明" + i);
            client.setSex(Sex.MAN);
            client.setStatus(i % 3 + "");
            client.setBilibili(i % 3);
            list.add(client);
        }
        Date         start  = new Date();
        ExportParams params = new ExportParams("下拉测试", "测试", ExcelType.HSSF);
        params.setDictHandler(new ExcelDiceAddressListHandlerImpl());
        Workbook workbook = ExcelExportUtil.exportExcel(params, AddressListEntity.class, list);
        System.out.println(new Date().getTime() - start.getTime());
        FileOutputStream fos = new FileOutputStream("D:/home/excel/AddressListTest.testOneXls.xls");
        workbook.write(fos);
        fos.close();
    }
}
