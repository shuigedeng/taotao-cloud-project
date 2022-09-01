package com.taotao.cloud.office.easypoi.easypoi.test.excel.export.img;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.taotao.cloud.office.easypoi.test.entity.img.CompanyHasImgModel;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author by jueyue on 19-6-23.
 */
public class ExcelExportUrlImgTest {

    List<CompanyHasImgModel> list;

    @Before
    public void initData() {
        list = new ArrayList<CompanyHasImgModel>();
        list.add(new CompanyHasImgModel("百度", "http://y3.ifengimg.com/a/2016_03/6154e935f8a0fc6.jpg", "北京市海淀区西北旺东路10号院百度科技园1号楼"));
        list.add(new CompanyHasImgModel("阿里巴巴", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1561861328&di=a515b18b702e027878df86bdbe1d1a3f&imgtype=jpg&er=1&src=http%3A%2F%2Fimg1.gtimg.com%2Fhb%2Fpics%2Fhv1%2F125%2F206%2F1609%2F104677880.jpg", "北京市海淀区西北旺东路10号院百度科技园1号楼"));
        list.add(new CompanyHasImgModel("Lemur", "imgs/company/lemur.png", "亚马逊热带雨林"));
        list.add(new CompanyHasImgModel("一众", "imgs/company/one.png", "山东济宁俺家"));


    }

    @Test
    public void exportCompanyImg() throws Exception {

        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        Workbook         workbook = ExcelExportUtil.exportExcel(new ExportParams(), CompanyHasImgModel.class, list);
        FileOutputStream fos      = new FileOutputStream("D:/home/excel/ExcelExportUrlImgTest.exportCompanyUrlImg.xls");
        workbook.write(fos);
        fos.close();
    }
}
