package com.taotao.cloud.office.easypoi.easypoi.csv;

import cn.afterturn.easypoi.csv.CsvExportUtil;
import cn.afterturn.easypoi.csv.entity.CsvExportParams;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.handler.inter.IReadHandler;
import cn.afterturn.easypoi.handler.inter.IWriter;
import com.taotao.cloud.office.easypoi.easypoi.test.excel.read.FileUtilTest;
import com.taotao.cloud.office.easypoi.test.entity.MsgClient;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jueyue on 19-11-26.
 */
public class ExcelToCsv {

    @Test
    public void test() {
        try {
            FileOutputStream fos    = new FileOutputStream("D:/home/excel/ExcelToCsv.test.csv");
            ImportParams     params = new ImportParams();
            params.setTitleRows(1);
            CsvExportParams csvExportParams = new CsvExportParams();
            IWriter ce = CsvExportUtil.exportCsv(csvExportParams, MsgClient.class, fos);
            ExcelImportUtil.importExcelBySax(
                    new FileInputStream(
                            new File(FileUtilTest.getWebRootPath("import/BigDataExport.xlsx"))),
                    MsgClient.class, params, new IReadHandler<MsgClient>() {

                        private List<MsgClient> list = new ArrayList<>();

                        @Override
                        public void handler(MsgClient o) {
                            list.add(o);
                            if (list.size() == 10000) {
                                ce.write(list);
                                list.clear();
                            }
                        }

                        @Override
                        public void doAfterAll() {
                            System.out.println("succcess--------------------------------");
                        }
                    });
        } catch (Exception e) {

        }
    }
}
