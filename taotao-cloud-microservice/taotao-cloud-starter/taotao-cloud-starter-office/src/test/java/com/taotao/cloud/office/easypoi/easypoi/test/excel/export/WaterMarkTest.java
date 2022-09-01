package com.taotao.cloud.office.easypoi.easypoi.test.excel.export;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.test.addressList.AddressListEntity;
import cn.afterturn.easypoi.test.en.Sex;
import cn.afterturn.easypoi.test.excel.handler.ExcelDiceAddressListHandlerImpl;
import cn.afterturn.easypoi.util.PoiWatermarkUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jueyue on 20-4-29.
 */
public class WaterMarkTest {


    @Test
    public void testWaterMarkOne() throws Exception {

        List<AddressListEntity> list = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
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
        try {
            PoiWatermarkUtil.putWaterRemarkToExcel(workbook.getSheetAt(0), "imgs/watermark.png");
            System.out.println(new Date().getTime() - start.getTime());
            FileOutputStream fos = new FileOutputStream("D:/home/excel/WaterMarkTest.testOne.xlsx");
            workbook.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testWaterMarkCenter() throws Exception {

        List<AddressListEntity> list = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
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
        try {
            PoiWatermarkUtil.putWaterRemarkToExcel(workbook.getSheetAt(0), "imgs/watermark.png","CENTER");
            System.out.println(new Date().getTime() - start.getTime());
            FileOutputStream fos = new FileOutputStream("D:/home/excel/WaterMarkTest.testWaterMarkCenter.xlsx");
            workbook.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWaterMarkRight() throws Exception {

        List<AddressListEntity> list = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
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
        try {
            PoiWatermarkUtil.putWaterRemarkToExcel(workbook.getSheetAt(0), "imgs/watermark.png","RIGHT");
            System.out.println(new Date().getTime() - start.getTime());
            FileOutputStream fos = new FileOutputStream("D:/home/excel/WaterMarkTest.testWaterMarkRight.xlsx");
            workbook.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
