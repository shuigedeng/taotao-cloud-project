package com.taotao.cloud.office.easypoi.easypoi.test.excel.export;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.test.entity.onettomany.hasname.DataitemEntity;
import cn.afterturn.easypoi.test.entity.onettomany.hasname.DemandEntity;
import cn.afterturn.easypoi.test.entity.onettomany.hasname.DeptEntity;
import cn.afterturn.easypoi.test.entity.onettomany.hasname.SupMaterialEntity;
import cn.afterturn.easypoi.test.excel.handler.ExcelDictHandlerImpl;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author by jueyue on 18-8-3.
 */
public class ExcelExportOneToManyHaseNameTest {

    @Test
    public void demandEntityTest() {
        List<DemandEntity> list = getList();
        try {
            ExportParams params = new ExportParams("一对多,对多,对多导出", "测试", ExcelType.XSSF);
            params.setDictHandler(new ExcelDictHandlerImpl());
            Workbook workbook = ExcelExportUtil.exportExcel(params, DemandEntity.class, list);
            File savefile = new File("D:/home/excel/");
            if (!savefile.exists()) {
                savefile.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream("D:/home/excel/ExcelExportOneToManyHaseNameTest.demandEntityTest.xlsx");
            workbook.write(fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<DemandEntity> getList() {
        List<DemandEntity> list = new ArrayList<DemandEntity>();
        DemandEntity de = new DemandEntity();
        de.setDeptName("省建设厅");
        de.setCode("审核转报-00180-000");
        de.setName("建筑业企业资质变更示例");
        de.setHandleTotal(3500L);
        de.setCategory(1);
        SupMaterialEntity sme = new SupMaterialEntity();
        sme.setMtype(1);
        sme.setLawType(1);
        sme.setSourceType(1);
        sme.setName("建筑业企业资质变更申请表");
        DataitemEntity dae = new DataitemEntity();
        dae.setName("企业营业执照号（五证合一社会信用号码）");
        List<DeptEntity> sdepts = new ArrayList<DeptEntity>();
        sdepts.add(new DeptEntity("省工商局"));
        dae.setSdepts(sdepts);
        sme.getDataitemList().add(dae);
        dae = new DataitemEntity();
        dae.setName("企业名称");
        sdepts = new ArrayList<DeptEntity>();
        sdepts.add(new DeptEntity("省工商局"));
        dae.setSdepts(sdepts);
        sme.getDataitemList().add(dae);
        dae = new DataitemEntity();
        dae.setName("资质证书编号");
        sdepts = new ArrayList<DeptEntity>();
        sdepts.add(new DeptEntity("省工商局"));
        sdepts.add(new DeptEntity("省省建设厅"));
        dae.setSdepts(sdepts);
        dae.setRemark("这就是生活啊");
        sme.getDataitemList().add(dae);
        de.getSupMaterialList().add(sme);

        sme = new SupMaterialEntity();
        sme.setMtype(1);
        sme.setLawType(1);
        sme.setSourceType(1);
        sme.setName("居民身份证");
        de.getSupMaterialList().add(sme);

        list.add(de);

        de = new DemandEntity();
        de.setDeptName("省建设厅");
        de.setCode("审核转报-00190-000");
        de.setName("建筑业企业资质变更示例无子项");
        de.setHandleTotal(6500L);
        de.setCategory(1);
        sme = new SupMaterialEntity();
        sme.setMtype(1);
        sme.setLawType(1);
        sme.setSourceType(1);
        sme.setName("建筑业企业资质变更申请表");
        dae = new DataitemEntity();
        dae.setName("企业营业执照号（五证合一社会信用号码）");
        sdepts = new ArrayList<DeptEntity>();
        sdepts.add(new DeptEntity("省工商局"));
        dae.setSdepts(sdepts);
        sme.getDataitemList().add(dae);
        dae = new DataitemEntity();
        dae.setName("企业名称");
        sdepts = new ArrayList<DeptEntity>();
        sdepts.add(new DeptEntity("省工商局"));
        dae.setSdepts(sdepts);
        sme.getDataitemList().add(dae);
        dae = new DataitemEntity();
        dae.setName("资质证书编号");
        sdepts = new ArrayList<DeptEntity>();
        sdepts.add(new DeptEntity("省工商局"));
        sdepts.add(new DeptEntity("省省建设厅"));
        dae.setSdepts(sdepts);
        dae.setRemark("这就是人生啊");
        sme.getDataitemList().add(dae);
        de.getSupMaterialList().add(sme);
        list.add(de);


        return list;
    }
}
