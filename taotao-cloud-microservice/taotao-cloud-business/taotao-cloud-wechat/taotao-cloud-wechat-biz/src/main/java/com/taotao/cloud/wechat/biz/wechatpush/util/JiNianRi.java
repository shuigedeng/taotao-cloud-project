package com.taotao.cloud.wechat.biz.wechatpush.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class JiNianRi {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 距离date还有多少天
     * @param date
     * @return
     */
    public static int before(String date) {
        int day = 0;
        try {
            long time = simpleDateFormat.parse(date).getTime() - System.currentTimeMillis();
            day = (int) (time / 86400000L);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }


    /**
     * 已经过去date多少天
     * @param date
     * @return
     */
    public static int after(String date) {
        int day = 0;
        try {
            long time = System.currentTimeMillis() - simpleDateFormat.parse(date).getTime();
            day = (int) (time / 86400000L);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }

    public static int getJieHun(String jieHun) {
        return before(jieHun);
    }

    public static int getLinZhen(String linZheng) {
        return before(linZheng);
    }

    public static int getLianAi(String lianAi) {
        return after(lianAi);
    }

    public static int getShengRi(String shengRi){
        return before(shengRi);
    }

    public static void main(String[] args) {
//        System.out.println(getJieHun());
    }


}
