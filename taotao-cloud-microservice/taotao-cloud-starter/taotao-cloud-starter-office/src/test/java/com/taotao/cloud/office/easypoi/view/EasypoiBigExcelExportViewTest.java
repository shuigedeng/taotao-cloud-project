package com.taotao.cloud.office.easypoi.view;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.afterturn.easypoi.view.PoiBaseView;
import com.taotao.cloud.office.easypoi.test.entity.MsgClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.afterturn.easypoi.entity.vo.BigExcelConstants;
import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.handler.inter.IExcelExportServer;
/**
 * 针对导出数据量大,批量查询批量插入方式,防止内存溢出解决的
 * @author JueYue
 * 2017年6月18日
 */
@Controller
@RequestMapping("/EasypoiBigExcelExportViewTest")
public class EasypoiBigExcelExportViewTest {
    
    @Autowired
    private IExcelExportServer excelExportServer;


    @RequestMapping()
    public String download(ModelMap map) {
        ExportParams params = new ExportParams("2412312", "测试", ExcelType.XSSF);
        params.setFreezeCol(2);
        map.put(BigExcelConstants.CLASS, MsgClient.class);
        map.put(BigExcelConstants.PARAMS, params);
        //就是我们的查询参数,会带到接口中,供接口查询使用
        map.put(BigExcelConstants.DATA_PARAMS, new HashMap<String,String>());
        map.put(BigExcelConstants.DATA_INTER,excelExportServer);
        return BigExcelConstants.EASYPOI_BIG_EXCEL_VIEW;

    }

    /**如果上面的方法不行,可以使用下面的用法
     * 同样的效果,只不过是直接问输出了,不经过view了
     * @param map
     * @param request
     * @param response
     */

    @RequestMapping("load")
    public void downloadByPoiBaseView(ModelMap map, HttpServletRequest request,
                                      HttpServletResponse response) {
        ExportParams params = new ExportParams("2412312", "测试", ExcelType.XSSF);
        params.setFreezeCol(2);
        map.put(BigExcelConstants.CLASS, MsgClient.class);
        map.put(BigExcelConstants.PARAMS, params);
        //就是我们的查询参数,会带到接口中,供接口查询使用
        map.put(BigExcelConstants.DATA_PARAMS, new HashMap<String,String>());
        map.put(BigExcelConstants.DATA_INTER,excelExportServer);
        PoiBaseView.render(map, request, response, BigExcelConstants.EASYPOI_BIG_EXCEL_VIEW);

    }

}
