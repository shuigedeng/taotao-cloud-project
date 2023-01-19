package com.taotao.cloud.workflow.biz.common.util.jscript;

import java.util.List;
import java.util.Map;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Java执行js代码工具类
 *
 */
public class JScriptUtil {

    /**
     * 数据接口通用定义函数
     */
    public static final String JSCONTENT = "var method = function(data) {" +
            "${jsContent}" +
            "};" +
            "var result = method(JSON.parse('${data}'));" +
            "JSON.stringify(result);";

    /**
     * 调用js代码
     * @param script 脚本内容
     * @return 如果JS内返回的是对象 返回内容为ScriptObjectMirror
     * @throws ScriptException
     */
    public static Object callJs(String script) throws ScriptException {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("js");
        return scriptEngine.eval(script);
    }


    /**
     * 调用js代码, 处理JSON数据 返回JSON数据
     *
     * @param dataProcessing 数据处理函数
     * @param data JSON对象/数组
     * @return JSON对象/数组
     */
    public static Object callJs(String dataProcessing, Object data) throws ScriptException {
        String jsContent = getJsContent(dataProcessing);
        if (StringUtil.isEmpty(dataProcessing)) {
            return data;
        }
        String replace = JSCONTENT.replace("${jsContent}", jsContent);
        replace = replace.replace("${data}", JsonUtil.getObjectToString(data));
        Object result = callJs(replace);
        try {
            List<Map<String, Object>> jsonToListMap = JsonUtil.getJsonToListMap(result.toString());
            return jsonToListMap;
        } catch (Exception e) {
            Map<String, Object> map = JsonUtil.stringToMap(result.toString());
            return map;
        }
    }

    /**
     * 返回js内容
     *
     * @param dataProcessing
     * @return
     */
    public static String getJsContent(String dataProcessing) {
        if (StringUtil.isNotEmpty(dataProcessing) && dataProcessing.length() > 0) {
            // 获取位置
            int indexOf = dataProcessing.indexOf("{");
            if (indexOf > -1) {
                dataProcessing = dataProcessing.substring(indexOf + 1);
            }
            int lastIndexOf = dataProcessing.lastIndexOf("}");
            if (lastIndexOf > -1) {
                dataProcessing = dataProcessing.substring(0, lastIndexOf);
            }
            return dataProcessing;
        }
        return "";
    }

}
