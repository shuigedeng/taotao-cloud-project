package com.taotao.cloud.office.easypoi.easypoi.csv.read;

import cn.afterturn.easypoi.csv.CsvImportUtil;
import cn.afterturn.easypoi.csv.entity.CsvImportParams;
import cn.afterturn.easypoi.handler.inter.IReadHandler;
import cn.afterturn.easypoi.test.entity.MsgClient;
import cn.afterturn.easypoi.test.excel.read.FileUtilTest;
import cn.afterturn.easypoi.util.JSON;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author by jueyue on 18-10-3.
 */
public class CsvDataRead {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvDataRead.class);


    @Test
    public void test() {
        try {
            Date start = new Date();
            LOGGER.debug("start");
            CsvImportParams params = new CsvImportParams(CsvImportParams.GBK);
            params.setTitleRows(1);
            CsvImportUtil.importCsv(new FileInputStream(
                            new File(FileUtilTest.getWebRootPath("csv/BigDataExport.csv"))),
                    MsgClient.class, params, new IReadHandler() {
                        @Override
                        public void handler(Object o) {

                        }

                        @Override
                        public void doAfterAll() {

                        }
                    });
            LOGGER.debug("end,time is {}", ((new Date().getTime() - start.getTime()) / 1000));
        } catch (Exception e) {
        }
    }


    @Test
    public void testUtf8Bom() {
        try {
            Date start = new Date();
            LOGGER.debug("start");
            CsvImportParams params = new CsvImportParams(CsvImportParams.UTF8);
            CsvImportUtil.importCsv(new FileInputStream(
                            new File(FileUtilTest.getWebRootPath("csv/20181107202743.csv"))),
                    Map.class, params, new IReadHandler<Map>() {
                        @Override
                        public void handler(Map o) {

                        }

                        @Override
                        public void doAfterAll() {

                        }
                    });
            LOGGER.debug("end,time is {}", ((new Date().getTime() - start.getTime()) / 1000));
        } catch (Exception e) {
        }
    }
}
