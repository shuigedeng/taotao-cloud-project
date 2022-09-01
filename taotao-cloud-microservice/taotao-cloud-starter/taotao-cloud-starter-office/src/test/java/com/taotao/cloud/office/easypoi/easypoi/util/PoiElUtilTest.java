package com.taotao.cloud.office.easypoi.easypoi.util;

import cn.afterturn.easypoi.util.PoiElUtil;
import junit.framework.Assert;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PoiElUtilTest {

    @Test
    public void testEval() throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("jueyue", "jueyue");

        //length 测试
        Object obj = PoiElUtil.eval("le:(jueyue)", map);
        Assert.assertEquals("6", obj);

        //format date 测试
        map.put("date", new Date());
        obj = PoiElUtil.eval("fd:(date;yyyy-MM-dd)", map);
        System.out.println(obj);
        obj = PoiElUtil.eval("fd:(date;yyyy-MM-dd HH:mm:ss)", map);
        System.out.println(obj);
        obj = PoiElUtil.eval("fd:(date;yyyy-MM-dd HH:mm)", map);
        System.out.println(obj);
        obj = PoiElUtil.eval("fd:(date;yyyy/MM/dd HH:mm:ss)", map);
        System.out.println(obj);

        map.put("date2", new Date().getTime());
        obj = PoiElUtil.eval("fd:(date2;yyyy-MM-dd)", map);
        System.out.println(obj);
        obj = PoiElUtil.eval("fd:(date2;yyyy-MM-dd HH:mm:ss)", map);
        System.out.println(obj);
        obj = PoiElUtil.eval("fd:(date2;yyyy-MM-dd HH:mm)", map);
        System.out.println(obj);
        obj = PoiElUtil.eval("fd:(date2;yyyy/MM/dd HH:mm:ss)", map);
        System.out.println(obj);

        //format number 测试 
        map.put("number", 213123123123.34234234);
        obj = PoiElUtil.eval("fn:(number;###.00)", map);
        System.out.println(obj);
        obj = PoiElUtil.eval("fn:(number;###.0)", map);
        System.out.println(obj);
        obj = PoiElUtil.eval("fn:(number;#,###.0)", map);
        System.out.println(obj);

        // !if 测试
        obj = PoiElUtil.eval("!if:(le:(jueyue) == '6')", map);
        Assert.assertEquals(obj, true);

        obj = PoiElUtil.eval("!if:(fd:(date2;yyyy-MM-dd) == '2015-11-01')", map);
        //Assert.assertEquals(obj, true);

        obj = PoiElUtil.eval("!if:(fn:(number;###.0) > '3')", map);
        Assert.assertEquals(obj, true);

        map.put("key1", "测试1");

        //三目运算
        obj = PoiElUtil.eval("'6' == '6' ? 'jueyue' : '小明'", map);
        Assert.assertEquals(obj, "jueyue");

        obj = PoiElUtil.eval("le:(jueyue) == '6' ? 'jueyue' : '小明'", map);
        Assert.assertEquals(obj, "jueyue");

        obj = PoiElUtil.eval("'6'   == le:(jueyue) ? 'jueyue' : '小明'", map);
        Assert.assertEquals(obj, "jueyue");

        obj = PoiElUtil.eval("'6'   == le:(jueyue) ? fd:(date;yyyy-MM-dd) : '小明'", map);
        //Assert.assertEquals(obj, "2015-11-01");
        obj = PoiElUtil.eval("'6'   != le:(jueyue) ? '小明' : fd:(date;yyyy-MM-dd)", map);
        //Assert.assertEquals(obj, "2015-11-01");

        obj = PoiElUtil.eval("jueyue == 'jueyue' ? key1 : '小明'", map);
        Assert.assertEquals(obj, "测试1");

        map.put("key1", "100");
        map.put("key2", "200");

        obj = PoiElUtil.eval("key1 == key2 ? key1 : '小明'", map);
        Assert.assertEquals(obj, "小明");

        obj = PoiElUtil.eval("key1 < key2 ? key1 : '小明'", map);
        Assert.assertEquals(obj, "100");

        obj = PoiElUtil.eval("key2 > key1 ? key1 : '小明'", map);
        Assert.assertEquals(obj, "100");

        map.put("key1", "你好");

        obj = PoiElUtil.eval("key1 == '你好' ? '不好' : '小明'", map);
        Assert.assertEquals(obj, "不好");

    }

    @Test
    public void strTest() {
        Assert.assertTrue("-1".compareTo("1") < 0);
    }


    @Test
    public void calculate() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");
        map.put("d", "4");
        String obj = PoiElUtil.eval("cal: a+b", map) + "";
        Assert.assertEquals(obj, "3.0");
        obj = PoiElUtil.eval("cal: a+b+(c-d)", map) + "";
        Assert.assertEquals(obj, "2.0");
        obj = PoiElUtil.eval("cal: a*b*(c+d)", map) + "";
        Assert.assertEquals(obj, "14.0");
        obj = PoiElUtil.eval("cal: c*b*(a+b)/(b+d)", map) + "";
        Assert.assertEquals(obj, "3.0");
    }
}
