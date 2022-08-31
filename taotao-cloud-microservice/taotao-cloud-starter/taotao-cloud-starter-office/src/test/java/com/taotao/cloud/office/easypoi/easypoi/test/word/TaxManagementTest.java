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
package com.taotao.cloud.office.easypoi.easypoi.test.word;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import cn.afterturn.easypoi.test.word.entity.TaxManagement;
import cn.afterturn.easypoi.word.WordExportUtil;
import cn.afterturn.easypoi.word.entity.params.ExcelListEntity;

import org.junit.Test;

public class TaxManagementTest {

    @Test
    public void testHasTotal() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String curTime = format.format(new Date());

        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("type", "个人所得税");
        map1.put("presum", "1580");
        map1.put("thissum", "1750");
        map1.put("curmonth", "1-11月");
        map1.put("now", curTime);
        mapList.add(map1);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("type", "增值税");
        map2.put("presum", "1080");
        map2.put("thissum", "1650");
        map2.put("curmonth", "1-11月");
        map2.put("now", curTime);
        mapList.add(map2);
        map.put("taxlist", mapList);
        map.put("totalpreyear", "2660");
        map.put("totalthisyear", "3400");
        try {
            XWPFDocument doc = WordExportUtil
                .exportWord07("word/纳税信息.docx", map);
            FileOutputStream fos = new FileOutputStream("D:/home/excel/纳税信息_test.docx");
            doc.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testEntity() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String curTime = format.format(new Date());

        Map<String, Object> map = new HashMap<String, Object>();
        List<TaxManagement> mapList = new ArrayList<TaxManagement>();
        TaxManagement tax = new TaxManagement();
        tax.setType("个人所得税");
        tax.setCurmonth("1-11月");
        tax.setNow(curTime);
        tax.setPresum("1580");
        tax.setThissum("1750");
        mapList.add(tax);
        tax = new TaxManagement();
        tax.setType("增值税");
        tax.setCurmonth("1-10月");
        tax.setNow(curTime);
        tax.setPresum("1650");
        tax.setThissum("1750");
        mapList.add(tax);
        map.put("taxlist", new ExcelListEntity(mapList, TaxManagement.class));
        Map<String, String> total = new HashMap<String, String>();
        total.put("totalpreyear", "2660");
        total.put("totalthisyear", "3400");
        map.put("total", total);
        map.put("totalpreyear", "2660");
        map.put("totalthisyear", "3400");
        try {
            XWPFDocument doc = WordExportUtil
                .exportWord07("word/纳税信息.docx", map);
            FileOutputStream fos = new FileOutputStream("D:/home/excel/纳税信息_entity.docx");
            doc.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
