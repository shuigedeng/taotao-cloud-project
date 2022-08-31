package com.taotao.cloud.office.easypoi.easypoi.test.excel.export;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Workbook;

import cn.afterturn.easypoi.test.entity.HyperLinkEntity;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.handler.impl.ExcelDataHandlerDefaultImpl;

import org.junit.Test;

/**
 * 超链接测试
 * @author JueYue
 *   2015年7月20日 下午10:34:18
 */
public class ExcelExportForLink {

    @Test
    public void test() throws Exception {

        List<HyperLinkEntity> list = new ArrayList<HyperLinkEntity>();
        HyperLinkEntity client = new HyperLinkEntity();
        client.setName("百度");
        client.setUrl("https://www.baidu.com/");
        list.add(client);
        client = new HyperLinkEntity();
        client.setName("新浪");
        client.setUrl("http://www.sina.com.cn/");
        list.add(client);

        Date start = new Date();
        ExportParams params = new ExportParams("2412312", "测试", ExcelType.XSSF);
        params.setDataHandler(new ExcelDataHandlerDefaultImpl() {

            @Override
            public Hyperlink getHyperlink(CreationHelper creationHelper, Object obj, String name, Object value) {
                Hyperlink link = creationHelper.createHyperlink(HyperlinkType.URL);
                HyperLinkEntity e = (HyperLinkEntity) obj;
                link.setAddress(e.getUrl());
                link.setLabel(e.getName());
                return link;
            }

        });
        Workbook workbook = ExcelExportUtil.exportExcel(params, HyperLinkEntity.class, list);
        System.out.println(new Date().getTime() - start.getTime());
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/ExcelExportForLink.xlsx");
        workbook.write(fos);
        fos.close();
    }

}
