package com.taotao.cloud.office.easypoi.easypoi.test.excel.handler;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;

/**
 * @author by jueyue on 18-8-3.
 */
public class ExcelDictHandlerImpl implements IExcelDictHandler {

    @Override
    public String toName(String dict, Object obj, String name, Object value) {
        if ("category".equals(dict)) {
            return "市场准入";
        }
        if ("mtype".equals(dict)) {
            return value.toString().equals("1") ? "市场准入" : "证件";
        }
        if ("sourceType".equals(dict)) {
            return "申请人提交纸质材料";
        }
        if ("lawType".equals(dict)) {
            return "规章";
        }
        return null;
    }

    @Override
    public String toValue(String dict, Object obj, String name, Object value) {
        return null;
    }
}
