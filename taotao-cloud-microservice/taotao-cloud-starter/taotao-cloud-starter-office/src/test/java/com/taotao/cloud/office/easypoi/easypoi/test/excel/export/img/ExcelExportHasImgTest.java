package com.taotao.cloud.office.easypoi.easypoi.test.excel.export.img;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.test.entity.img.CompanyHasImgModel;

/**
 * 带有图片的测试服务
 *
 * Created by JueYue on 2017/8/25.
 */
public class ExcelExportHasImgTest {

    List<CompanyHasImgModel> list;

    @Before
    public void initData() {
        list = new ArrayList<CompanyHasImgModel>();
        list.add(new CompanyHasImgModel("百度", "imgs/company/baidu.png", "北京市海淀区西北旺东路10号院百度科技园1号楼"));
        list.add(new CompanyHasImgModel("阿里巴巴", "imgs/company/ali.png", "北京市海淀区西北旺东路10号院百度科技园1号楼"));
        list.add(new CompanyHasImgModel("Lemur", "imgs/company/lemur.png", "亚马逊热带雨林"));
        list.add(new CompanyHasImgModel("一众", "imgs/company/one.png", "山东济宁俺家"));


    }

    @Test
    public void exportCompanyImg() throws Exception {

        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), CompanyHasImgModel.class, list);
        FileOutputStream fos = new FileOutputStream("D:/home/excel/ExcelExportHasImgTest.exportCompanyImg.xls");
        workbook.write(fos);
        fos.close();
    }

}
