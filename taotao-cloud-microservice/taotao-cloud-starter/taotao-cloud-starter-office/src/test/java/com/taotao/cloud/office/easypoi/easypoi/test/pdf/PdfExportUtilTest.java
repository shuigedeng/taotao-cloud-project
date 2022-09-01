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
package com.taotao.cloud.office.easypoi.easypoi.test.pdf;


import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.afterturn.easypoi.test.entity.MsgClient;
import cn.afterturn.easypoi.test.entity.MsgClientGroup;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.pdf.PdfExportUtil;
import cn.afterturn.easypoi.pdf.entity.PdfExportParams;

import com.itextpdf.layout.Document;
import org.junit.jupiter.api.Test;


public class PdfExportUtilTest {

    @Test
    public void testExportPdf() {
        
        Field[] fields =  MsgClient.class.getFields();
        for (int i = 0; i < fields.length; i++) {
            Excel excel = fields[i].getAnnotation(Excel.class);
            System.out.println(excel);
        }
        
        List<MsgClient> list = new ArrayList<MsgClient>();
        for (int i = 0; i < 10; i++) {
            MsgClient client = new MsgClient();
            client.setBirthday(new Date());
            client.setClientName("小明" + i);
            client.setClientPhone("18797" + i);
            client.setCreateBy("JueYue");
            client.setId("1" + i);
            client.setRemark("测试" + i);
            MsgClientGroup group = new MsgClientGroup();
            group.setGroupName("测试" + i);
            client.setGroup(group);
            list.add(client);
        }
        Date start = new Date();
        PdfExportParams params = new PdfExportParams("2412312",null);
        try {
            File file = new File("D:/home/excel//PdfExportUtilTest.testExportPdf.pdf");
            file.createNewFile();
            Document document =  PdfExportUtil.exportPdf(params, MsgClient.class, list,new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }  
    }

    @Test
    public void testExportPdfExportParamsListOfExcelExportEntityCollectionOfQextendsMapOfQQ() {
    }

}
