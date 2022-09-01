package com.taotao.cloud.office.easypoi.easypoi.csv;

import cn.afterturn.easypoi.csv.CsvExportUtil;
import cn.afterturn.easypoi.csv.entity.CsvExportParams;
import cn.afterturn.easypoi.handler.inter.IWriter;
import com.taotao.cloud.office.easypoi.test.entity.MsgClient;
import com.taotao.cloud.office.easypoi.test.entity.groupname.GroupExportVo;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class CsvExportUtilTest {

    @Test
    public void exportCsv() throws IOException {
        Date            start    = new Date();
        CsvExportParams params   = new CsvExportParams();
        File            savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/ExcelExportBigData.bigDataExport.csv");
        IWriter          ce  = CsvExportUtil.exportCsv(params, GroupExportVo.class, fos);
        List<GroupExportVo> list;
        for (int i = 0; i < 20000; i++) {
            list = getList();
            ce.write(list);
            list.clear();
        }
        ce.close();
        System.out.println("导出完成" + (new Date().getTime() - start.getTime()));
    }

    private List<GroupExportVo> getList() {
        List<GroupExportVo> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            GroupExportVo vo = new GroupExportVo();
            vo.setActualLandingTime("111111111");
            vo.setActualTakeoffTime("22222222222222");
            vo.setAerocraft("3333333333333");
            vo.setCompanyName("悟耘科技有限公司");
            vo.setHeightDesc("无敌的小明");
            vo.setId(213213123);
            vo.setIndexName("111111111111111111111111");
            vo.setLandingTime("2020年4月6日");
            vo.setRemark("A啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊3333333333333333333333333333333333333333333331111111111111");
            vo.setScopeName("优秀");
            list.add(vo);
        }
        return list;
    }


    @Test
    public void exportCsvGBK() throws IOException {
        List<MsgClient> list   = new ArrayList<MsgClient>();
        Date            start  = new Date();
        CsvExportParams params = new CsvExportParams();
        params.setEncoding(CsvExportParams.GBK);
        for (int i = 0; i < 50; i++) {
            MsgClient client = new MsgClient();
            client.setBirthday(new Date());
            client.setClientName("小明" + i);
            client.setClientPhone("18797" + i);
            client.setCreateBy("JueYue");
            client.setId("1" + i);
            client.setRemark("测试" + i);
            list.add(client);
        }

        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/ExcelExportBigData.bigDataExport_GBK.csv");
        IWriter          ce  = CsvExportUtil.exportCsv(params, MsgClient.class, fos);
        for (int i = 0; i < 1000; i++) {
            ce.write(list);
        }
        ce.close();
        System.out.println("导出完成" + (new Date().getTime() - start.getTime()));
    }

}
