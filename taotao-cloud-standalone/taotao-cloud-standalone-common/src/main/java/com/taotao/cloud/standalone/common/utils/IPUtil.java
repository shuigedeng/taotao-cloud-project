package com.taotao.cloud.standalone.common.utils;

import org.apache.commons.io.FileUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * @Classname IPUtil
 * @Description IP工具类
 * @Author shuigedeng
 * @since 2019/12/14 10:19 下午
 * @Version 1.0
 */
public class IPUtil {

    public static String getCityInfo(String ip) {
        try {
            //db
            ClassPathResource resource = new ClassPathResource("/ip2region/ip2region.db");
            String tmpDir = System.getProperties().getProperty("java.io.tmpdir");
            String dbPath = tmpDir + "ip.db";
            File file = new File(dbPath);
            InputStream inputStream = resource.getInputStream();
            FileUtils.copyInputStreamToFile(inputStream, file);

            //查询算法
            int algorithm = DbSearcher.BTREE_ALGORITHM; //B-tree
            //DbSearcher.BINARY_ALGORITHM //Binary
            //DbSearcher.MEMORY_ALGORITYM //Memory

            DbConfig config = new DbConfig();
            DbSearcher searcher = new DbSearcher(config, file.getPath());

            //define the method
            Method method = null;
            switch (algorithm) {
                case DbSearcher.BTREE_ALGORITHM:
                    method = searcher.getClass().getMethod("btreeSearch", String.class);
                    break;
                case DbSearcher.BINARY_ALGORITHM:
                    method = searcher.getClass().getMethod("binarySearch", String.class);
                    break;
                case DbSearcher.MEMORY_ALGORITYM:
                    method = searcher.getClass().getMethod("memorySearch", String.class);
                    break;
            }

            DataBlock dataBlock = null;
            if (Util.isIpAddress(ip) == false) {
                System.out.println("Error: Invalid ip address");
            }
            dataBlock = (DataBlock) method.invoke(searcher, ip);

            return dataBlock.getRegion();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
