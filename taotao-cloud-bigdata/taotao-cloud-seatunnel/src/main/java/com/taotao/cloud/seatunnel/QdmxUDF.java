package com.taotao.cloud.seatunnel;

import cn.hutool.core.util.StrUtil;
import com.google.auto.service.AutoService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.seatunnel.api.table.type.BasicType;
import org.apache.seatunnel.api.table.type.SeaTunnelDataType;
import org.apache.seatunnel.transform.sql.zeta.ZetaUDF;
//mvn -T 8 clean install -DskipTests -Dcheckstyle.skip -Dmaven.javadoc.skip=true
@AutoService(ZetaUDF.class)
public class QdmxUDF implements ZetaUDF {

    @Override
    public String functionName() {
        return "QDMX";
    }

    @Override
    public SeaTunnelDataType<?> resultType(List<SeaTunnelDataType<?>> list) {
        return BasicType.STRING_TYPE;
    }

    // list 参数实例：（也就是kafka 解析过来的数据）
    // SeaTunnelRow{tableId=, kind=+I, fields=[{key1=value1,key2=value2,.....}]}
    @Override
    public Object evaluate(List<Object> list) {
        String str = list.get(0).toString();
        // 1 Remove the prefix
        str = StrUtil.replace(str, "SeaTunnelRow{tableId=, kind=+I, fields=[{", "");
        // 2 Remove the suffix
        str = StrUtil.sub(str, -3, 0);
        // 3 build Map key value
        Map<String, String> map = parseToMap(str);
        if ("null".equals(map.get(list.get(1).toString())))
            return "";
        // 4 return the value of the key
        return map.get(list.get(1).toString());
    }

    public static Map<String, String> parseToMap(String input) {
        Map<String, String> map = new HashMap<>();
        // 去除大括号 在字符串阶段去除
        // input = input.replaceAll("[{}]", "");
        // 拆分键值对
        String[] pairs = input.split(", ");

        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().toLowerCase();
                String value = keyValue[1].trim();
                map.put(key, value);
            }
        }
        return map;
    }
}
