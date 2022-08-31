/**
 * Copyright 2013-2015 JueYue (qrb.jueyue@gmail.com)
 *   
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.office.easypoi.easypoi.test.excel.read.check;

import java.io.File;
import java.util.List;

import cn.afterturn.easypoi.test.entity.check.ImportCheckOneLine;
import cn.afterturn.easypoi.test.entity.check.ImportCheckOneLineNoAnn;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.test.excel.read.FileUtilTest;
import cn.afterturn.easypoi.util.PoiPublicUtil;

import org.junit.Assert;
import org.junit.Test;

/**
 * 输入模板校验
 * 单行版本
 * @author JueYue
 *   2015年11月13日 下午8:40:05
 */
public class ImportCheckTestOneLine {

    @Test
    public void testOneLine() {
        boolean isOK = true;
        try {
            ImportParams params = new ImportParams();
            params.setTitleRows(1);
            List<ImportCheckOneLine> list = ExcelImportUtil.importExcel(
                new File(FileUtilTest.getWebRootPath("import/check.xls")),
                ImportCheckOneLine.class, params);
        } catch (Exception e) {
            e.printStackTrace();
            isOK = false;
        }
        Assert.assertTrue(isOK);
    }

    @Test
    public void testOneLineError() {
        boolean isOK = true;
        try {
            ImportParams params = new ImportParams();
            params.setTitleRows(1);
            params.setStartSheetIndex(1);
            List<ImportCheckOneLine> list = ExcelImportUtil.importExcel(
                new File(FileUtilTest.getWebRootPath("import/check.xls")),
                ImportCheckOneLine.class, params);
        } catch (Exception e) {
            e.printStackTrace();
            isOK = false;
        }
        Assert.assertTrue(!isOK);
    }

    @Test
    public void testOneLineByParams() {
        boolean isOK = true;
        try {
            ImportParams params = new ImportParams();
            params.setTitleRows(1);
            params.setImportFields(new String[] { "姓名", "性别", "年纪", "爱好" });
            List<ImportCheckOneLineNoAnn> list = ExcelImportUtil.importExcel(
                new File(FileUtilTest.getWebRootPath("import/check.xls")),
                ImportCheckOneLineNoAnn.class, params);
        } catch (Exception e) {
            e.printStackTrace();
            isOK = false;
        }
        Assert.assertTrue(isOK);
    }

    @Test
    public void testOneLineErrorByParams() {
        boolean isOK = true;
        try {
            ImportParams params = new ImportParams();
            params.setTitleRows(1);
            params.setStartSheetIndex(1);
            params.setImportFields(new String[] { "姓名", "性别", "年纪", "爱好" });
            List<ImportCheckOneLineNoAnn> list = ExcelImportUtil.importExcel(
                new File(FileUtilTest.getWebRootPath("import/check.xls")),
                ImportCheckOneLineNoAnn.class, params);
        } catch (Exception e) {
            e.printStackTrace();
            isOK = false;
        }
        Assert.assertTrue(!isOK);
    }

}
