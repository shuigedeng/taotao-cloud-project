package com.taotao.cloud.office.easyexecl.temp.issue1663;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.WriteDirectionEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.taotao.cloud.common.execl.temp.issue1663.FillData;
import com.taotao.cloud.common.execl.util.TestFileUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Ignore
public class FillTest {
    @Test
    public void TestFillNullPoint(){
        String templateFileName =
            TestFileUtil.getPath() + "temp/issue1663" + File.separator + "template.xlsx";

        String fileName = TestFileUtil.getPath() + "temp/issue1663" + File.separator + "issue1663.xlsx";
        ExcelWriter excelWriter = EasyExcel.write(fileName).withTemplate(templateFileName).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.VERTICAL).build();
        excelWriter.fill(new FillWrapper("data1", data()), fillConfig, writeSheet);

        Map<String, Object> map = new HashMap<String, Object>();
        // Variable {date} does not exist in the template.xlsx, which should be ignored instead of reporting an error.
        map.put("date", "2019年10月9日13:28:28");
        excelWriter.fill(map, writeSheet);
        excelWriter.finish();
    }
    private List<com.taotao.cloud.common.execl.demo.fill.FillData> data() {
        List<com.taotao.cloud.common.execl.demo.fill.FillData> list = new ArrayList<com.taotao.cloud.common.execl.demo.fill.FillData>();
        for (int i = 0; i < 10; i++) {
            com.taotao.cloud.common.execl.demo.fill.FillData fillData = new FillData();
            list.add(fillData);
            fillData.setName("张三");
            fillData.setNumber(5.2);
        }
        return list;
    }
}
