package com.taotao.cloud.sys.biz.api.controller.tools.core.validation.custom;

import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

@Getter
public final class IdCard18Model {
    private String areaCode;
    private String birthday;
    private int serialNo;
    private char checkCode;

    /**
     * 区域代码列表
     */
    public static String[] areaCodes;

    public IdCard18Model() {

    }

    public IdCard18Model(String idCard18) {
        boolean checkIdCard18 = checkIdCard18(idCard18);
        if(!checkIdCard18){
            throw new IllegalArgumentException("身份证格式不正确");
        }
        //解析第一位字段
        this.areaCode = idCard18.substring(0,6);
        if(ArrayUtils.isNotEmpty(areaCodes)){
            if(!ArrayUtils.contains(areaCodes,areaCode)){
                throw new IllegalArgumentException("不存在的区域:"+areaCode);
            }
        }
        this.birthday = idCard18.substring(6,14);
        checkBirthday(birthday);
        this.serialNo = NumberUtils.toInt(idCard18.substring(14,17));
        this.checkCode = CharUtils.toChar(idCard18.substring(17));
    }

    /**
     * 检测日期格式是否正确
     * @param birthday
     */
    private void checkBirthday(String birthday) {
        try {
            DateUtils.parseDate(birthday,format);
        } catch (ParseException e) {
            throw new IllegalArgumentException("身份证日期不正确:"+birthday);
        }
    }

    static final String format = "yyyyMMdd";

    /**
     * 获取出生日期
     *
     * @return
     */
    public Date getBirthdayDate() {
        try {
            return DateUtils.parseDate(birthday, format);
        } catch (ParseException e) {
            // don't have exception
        }
        return null;
    }

    @Override
    public String toString() {
        return areaCode + birthday + serialNo + checkCode;
    }

    /**
     * 获取性别
     * true: 女
     * false : 男
     *
     * @return
     */
    public Sex getSex() {
        boolean sex = serialNo % 2 == 0;
        if (sex) {
            return Sex.woman;
        }
        return Sex.man;
    }

    /**
     * 是否男性
     * @return
     */
    public boolean isMan(){
        return getSex() == Sex.man;
    }

    public boolean isWoman(){
        return getSex() == Sex.woman;
    }

    /**
     * 性别
     */
    public enum Sex {
        man, woman
    }

    /**
     * 检查 18 位身份证正确性
     *
     * @param idCard
     * @return
     */
    public static boolean checkIdCard18(String idCard) {
        if (StringUtils.isBlank(idCard)) {
            return false;
        }
        if (idCard.length() != 18) {
            return false;
        }

        // 获取前17位
        String idcard17 = idCard.substring(0, 17);
        // 前17位全部为数字
        if (!idcard17.matches("^[0-9]*$")) {
            return false;
        }


        // 对于 18 位身份证,校验最后一位
        char checkCode = calcCheckcode(idcard17);
        char idcard18 = idCard.charAt(17);
        if (idcard18 != checkCode && Character.toLowerCase(idcard18) != checkCode) {
            return false;
        }

        return true;
    }

    /**
     * 计算加权码
     *
     * @param idcard17
     * @return
     */
    public static char calcCheckcode(String idcard17) {
        char[] chars = idcard17.toCharArray();
        int[] charInts = new int[chars.length];
        for (int i = 0; i < charInts.length; i++) {
            charInts[i] = NumberUtils.toInt(String.valueOf(chars[i]));
        }
        //加权因子列表
        int[] power = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        // 计算加权和
        int sum = 0;
        for (int i = 0; i < charInts.length; i++) {
            sum = sum + charInts[i] * power[i];
        }
        //获取检验码,和身份证第 18 位
        //校验码列表
        char[] checkCodes = {'1', '0', 'x', '9', '8', '7', '6', '5', '4', '3', '2'};
        return checkCodes[sum % 11];
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof IdCard18Model)) {
            return false;
        }
        return this.toString().equalsIgnoreCase(obj.toString());
    }
}
