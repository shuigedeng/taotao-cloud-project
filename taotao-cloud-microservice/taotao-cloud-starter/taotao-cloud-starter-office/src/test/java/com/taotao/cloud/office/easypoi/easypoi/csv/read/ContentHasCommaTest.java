package com.taotao.cloud.office.easypoi.easypoi.csv.read;

import cn.afterturn.easypoi.csv.CsvImportUtil;
import cn.afterturn.easypoi.csv.entity.CsvImportParams;
import cn.afterturn.easypoi.handler.inter.IReadHandler;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.office.easypoi.easypoi.test.excel.read.FileUtilTest;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Map;

/**
 * @author jueyue on 21-2-7.
 */
public class ContentHasCommaTest {


    @Test
    public void test() {
        try {
            Date start = new Date();
            LogUtils.debug(" ContentHasCommaTest start");
            CsvImportParams params = new CsvImportParams(CsvImportParams.UTF8);
            CsvImportUtil.importCsv(new FileInputStream(
                            new File(FileUtilTest.getWebRootPath("csv/20181107202743_comma.csv"))),
                    Map.class, params, new IReadHandler<Map>() {
                        @Override
                        public void handler(Map o) {
							LogUtils.error(o.get("商户号").toString() + "---" + o.get("商户名称").toString());
                        }

                        @Override
                        public void doAfterAll() {

                        }
                    });
			LogUtils.debug(" ContentHasCommaTest end,time is {}", ((new Date().getTime() - start.getTime()) / 1000));
        } catch (Exception e) {
        }
    }
}
