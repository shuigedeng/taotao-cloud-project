package com.taotao.cloud.bigdata.azkaban.mq.base;

/**
 * @author chejiangyi
 */
public enum DelayTimeEnum {
    None(0,0,"立即"),
    S01(1,1,"延迟1秒"),
    S05(2,5, "延迟5秒"),
    S10(3,10, "延迟10秒"),
    S30(4,30, "延迟30秒"),
    M01(5,60, "延迟1分钟"),
    M02(6,60*2, "延迟2分钟"),
    M03(7,60*3, "延迟3分钟"),
    M04(8,60*4, "延迟4分钟"),
    M05(9, 60*5,"延迟5分钟"),
    M16(10,60*6, "延迟6分钟"),
    M07(11,60*7, "延迟7分钟"),
    M08(12,60*8, "延迟8分钟"),
    M09(13,60*9, "延迟9分钟"),
    M10(14,60*10, "延迟10分钟"),
    M20(15,60*20, "延迟20分钟"),
    M30(16,60*30, "延迟30分钟"),
    H01(17,60*60, "延迟1小时"),
    H02(18,60*60*2, "延迟2小时");
    private int code;
    private int second;
    private String message;

    DelayTimeEnum(int code,int second, String message) {
        this.code = code;
        this.second=second;
        this.message = message;
    }

    public int getCode() {
        return code;
    }
    public int getSecond() {
        return second;
    }
}
