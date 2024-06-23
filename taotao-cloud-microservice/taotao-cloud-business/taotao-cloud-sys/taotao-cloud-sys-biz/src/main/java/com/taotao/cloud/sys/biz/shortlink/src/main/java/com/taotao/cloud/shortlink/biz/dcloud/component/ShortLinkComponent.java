package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.dcloud.component;

import com.taotao.cloud.shortlink.biz.dcloud.strategy.ShardingDBConfig;
import com.taotao.cloud.shortlink.biz.dcloud.strategy.ShardingTableConfig;
import org.springframework.stereotype.Component;

/**
 * @Author 刘森飚
 * @Version 1.0
 **/

@Component
public class ShortLinkComponent {

    /**
     * 62个字符
     */
    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";


    /**
     * 生成短链码
     * @param param
     * @return
     */
    public String createShortLinkCode(String param){

        long murmurhash = CommonUtil.murmurHash32(param);
        //进制转换
        String code = encodeToBase62(murmurhash);

        String shortLinkCode = ShardingDBConfig.getRandomDBPrefix(code) + code + ShardingTableConfig.getRandomTableSuffix(code);


        return shortLinkCode;
    }

    /**
     * 10进制转62进制
     * @param num
     * @return
     */
    private String encodeToBase62(long num){

        // StringBuffer线程安全，StringBuilder线程不安全
        StringBuffer sb = new StringBuffer();
        do{
            int i = (int )(num%62);
            sb.append(CHARS.charAt(i));
            num = num/62;
        }while (num>0);

        String value = sb.reverse().toString();
        return value;

    }
}
