package com.taotao.cloud.office.easypoi.easypoi;

import cn.afterturn.easypoi.util.JSON;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * @author by jueyue on 19-9-29.
 */
public class JOSNTest {

    //@Test
    public void test() throws Exception {
        BufferedReader rows = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\jueyue\\Desktop\\山东组织机构.json")));
        StringBuilder  json = new StringBuilder();
        String         str  = "";
        while ((str = rows.readLine()) != null) {
            json.append(str);
        }
        Map map = JSON.parseJson(json.toString(), Map.class);
        System.out.println(map.get("name").toString());
        dedai((List<Map>) map.get("children"));

        //System.out.println(map);
    }

    private void dedai(List<Map> children) {
        for (int i = 0; i < children.size(); i++) {
            Map map = children.get(i);
            System.out.println(map.get("name").toString());
            if (map.containsKey("children")) {
                dedai((List<Map>) map.get("children"));
            }
        }
    }
}
