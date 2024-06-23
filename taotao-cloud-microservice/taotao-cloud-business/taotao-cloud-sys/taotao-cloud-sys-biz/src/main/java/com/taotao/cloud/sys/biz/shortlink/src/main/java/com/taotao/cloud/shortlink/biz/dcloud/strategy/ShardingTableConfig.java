package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.dcloud.strategy;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author：刘森飚
 **/

public class ShardingTableConfig {

    /**
     * 存储数据表位置编号
     */
    private static final List<String> tableSuffixList = new ArrayList<>();


    //配置启用那些表的后缀
    static {
        tableSuffixList.add("0");
        tableSuffixList.add("a");
    }


    /**
     * 获取随机的后缀
     * @return
     */
    public static String getRandomTableSuffix(String code){
        //hashCode是固定的
        int hashCode = code.hashCode();
        int index = Math.abs(hashCode) % tableSuffixList.size();
        return tableSuffixList.get(index);
    }
}
