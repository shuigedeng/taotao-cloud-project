package com.taotao.cloud.office.easypoi.easypoi.test.excel.handler;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模拟使用,生产请用真实字典
 *
 * @author jueyue on 20-4-26.
 */
public class ExcelDiceAddressListHandlerImpl implements IExcelDictHandler {

    /**
     * 返回字典所有值
     * key: dictKey
     *
     * @param dict 字典Key
     * @return
     */
    @Override
    public List<Map> getList(String dict) {
        List<Map>           list    = new ArrayList<>();
        Map<String, String> dictMap = new HashMap<>();
        dictMap.put("dictKey", "0");
        dictMap.put("dictValue", "严重瞌睡");
        list.add(dictMap);
        dictMap = new HashMap<>();
        dictMap.put("dictKey", "1");
        dictMap.put("dictValue", "小B");
        list.add(dictMap);
        dictMap = new HashMap<>();
        dictMap.put("dictKey", "1");
        dictMap.put("dictValue", "深度富有");
        list.add(dictMap);
        return list;
    }

    @Override
    public String toName(String dict, Object obj, String name, Object value) {
        if ("level".equals(dict)) {
            int level = Integer.parseInt(value.toString());
            switch (level) {
                case 1:
                    return "小B";
                case 0:
                    return "严重瞌睡";
                case 2:
                    return "深度富有";
            }
        }
        return null;
    }

    @Override
    public String toValue(String dict, Object obj, String name, Object value) {
        if ("level".equals(dict)) {
            int level = Integer.parseInt(value.toString());
            switch (level) {
                case 1:
                    return "小B";
                case 0:
                    return "严重瞌睡";
                case 2:
                    return "深度富有";
            }
        }
        return null;
    }
}
