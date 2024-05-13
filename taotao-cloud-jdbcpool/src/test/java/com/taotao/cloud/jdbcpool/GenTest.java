package com.taotao.cloud.jdbcpool;

import com.github.houbb.heaven.util.io.FileUtil;
import java.util.Map;
import org.junit.Ignore;
import org.junit.Test;

/**
 * <p> project: jdbc-pool-GenTest </p>
 * <p> create on 2020/7/18 9:25 </p>
 *
 * @author shuigedeng
 * @since 1.2.0
 */
@Ignore
public class GenTest {

    @Test
    public void driverClassTest() {
        Map<String, String> map = FileUtil.readToMap("D:\\_github\\jdbc-pool\\src\\test\\resources\\driverClass.txt", " ");

        final String format = "DRIVER_CLASS_MAP.put(\"%s\", \"%s\");";
        for(Map.Entry<String, String> entry : map.entrySet()) {
            String result = String.format(format, entry.getKey(), entry.getValue());
            System.out.println(result);
        }
    }

    @Test
    public void driverClassMDTest() {
        Map<String, String> map = FileUtil.readToMap("D:\\_github\\jdbc-pool\\src\\test\\resources\\driverClass.txt", " ");

        final String format = "| %s | %s |";
        for(Map.Entry<String, String> entry : map.entrySet()) {
            String result = String.format(format, entry.getKey(), entry.getValue());
            System.out.println(result);
        }
    }

}
