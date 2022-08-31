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
package com.taotao.cloud.office.easypoi.easypoi.cache.manager;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import cn.afterturn.easypoi.cache.manager.POICacheManager;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;

import org.junit.Test;

public class POICacheManagerTest {

    @Test
    public void test() throws Exception {
        //设置成为自己的文件加载
        //POICacheManager.setFileLoder(new MyFileLoader());
        
        //会吧这个对象ThreadLocal中,线程结束就不起作用了
        //POICacheManager.setFileLoderOnce(new MyFileLoader());
        
        
        TemplateExportParams params = new TemplateExportParams(
                "doc/merge_test.xls");
            Map<String, Object> map = new HashMap<String, Object>();

            List<Map<String, String>> list = new ArrayList<Map<String, String>>();

            for (int i = 0; i < 8; i++) {
                Map<String, String> m = new HashMap<String, String>();
                m.put("id", "id" + "1");
                m.put("uname", "name" + "1");
                m.put("amount", i+"");
                list.add(m);
            }
            map.put("list", list);
            Workbook workbook = ExcelExportUtil.exportExcel(params, map);
            File savefile = new File("D:/home/excel/");
            if (!savefile.exists()) {
                savefile.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream("D:/home/excel/tt.xls");
            workbook.write(fos);
            fos.close();
    }

}
