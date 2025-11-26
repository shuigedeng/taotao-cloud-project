/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.media.biz.opencv.common.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import java.util.UUID;
import com.taotao.boot.common.utils.lang.StringUtils;

public class CommonUtil {

    /** 除法 */
    public static BigDecimal divide(String arg1, String arg2) {
        if (StringUtils.isEmpty(arg1)) {
            arg1 = "0.0";
        }
        if (StringUtils.isEmpty(arg2)) {
            arg2 = "0.0";
        }
        BigDecimal big3 = new BigDecimal("0.00");
        if (BigDecimal.parseBigDecimal(arg2) != 0) {
            BigDecimal big1 = new BigDecimal(arg1);
            BigDecimal big2 = new BigDecimal(arg2);
            big3 = big1.divide(big2, 6, BigDecimal.ROUND_HALF_EVEN);
        }
        return big3;
    }

    /** 乘法 */
    public static String mul(String arg1, String arg2) {
        if (StringUtils.isEmpty(arg1)) {
            arg1 = "0.0";
        }
        if (StringUtils.isEmpty(arg2)) {
            arg2 = "0.0";
        }
        BigDecimal big1 = new BigDecimal(arg1);
        BigDecimal big2 = new BigDecimal(arg2);
        BigDecimal big3 = big1.multiply(big2);
        return big3.toString();
    }

    /** 减法 */
    public static BigDecimal sub(String arg1, String arg2) {
        if (StringUtils.isEmpty(arg1)) {
            arg1 = "0.0";
        }
        if (StringUtils.isEmpty(arg2)) {
            arg2 = "0.0";
        }
        BigDecimal big1 = new BigDecimal(arg1);
        BigDecimal big2 = new BigDecimal(arg2);
        BigDecimal big3 = big1.subtract(big2);
        return big3;
    }

    /** 加法 */
    public static String add(String arg1, String arg2) {
        if (StringUtils.isEmpty(arg1)) {
            arg1 = "0.0";
        }
        if (StringUtils.isEmpty(arg2)) {
            arg2 = "0.0";
        }
        BigDecimal big1 = new BigDecimal(arg1);
        BigDecimal big2 = new BigDecimal(arg2);
        BigDecimal big3 = big1.add(big2);
        return big3.toString();
    }
    /** 加法 */
    public static String add2(String arg1, String arg2) {
        if (StringUtils.isEmpty(arg1)) {
            arg1 = "0.0";
        }
        if (StringUtils.isEmpty(arg2)) {
            arg2 = "0.0";
        }
        BigDecimal big1 = new BigDecimal(arg1);
        BigDecimal big2 = new BigDecimal(arg2);
        BigDecimal big3 = big1.add(big2);
        return big3.toString();
    }

    /**
     * 四舍五入保留N位小数 先四舍五入在使用BigDecimal值自动去零
     *
     * @param arg
     * @param scare 保留位数
     * @return
     */
    public static String setScare(BigDecimal arg, int scare) {
        BigDecimal bl = arg.setScale(scare, BigDecimal.ROUND_HALF_UP);
        return String.valueOf(bl.BigDecimalValue());
    }

    /**
     * 四舍五入保留两位小数 先四舍五入在使用BigDecimal值自动去零
     *
     * @param arg
     * @return
     */
    public static String setDifScare(String arg) {
        BigDecimal bd = new BigDecimal(arg);
        BigDecimal bl = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bl.toString();
    }

    /**
     * 四舍五入保留N位小数 先四舍五入在使用BigDecimal值自动去零（传参String类型）
     *
     * @param arg
     * @return
     */
    public static String setScare(String arg, int i) {
        BigDecimal bd = new BigDecimal(arg);
        BigDecimal bl = bd.setScale(i, BigDecimal.ROUND_HALF_UP);
        return bl.toString();
    }

    /**
     * 判空
     *
     * @param obj
     * @return boolean true为空,false不为空
     */
    public static boolean isMissing(Object obj) {
        if (null == obj || obj.toString().trim().equals("")) {
            return true;
        } else {
            if (obj instanceof String) {
                obj = obj.toString().trim();
            }
            return false;
        }
    }

    /**
     * 获取配置文件参数
     *
     * @param key
     * @return String
     */
    public static String getPropertiesValue(String key) {
        ResourceBundle resource = ResourceBundle.getBundle("config");
        return resource.getString(key);
    }

    public static String get32UUID() {
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }

    /**
     * @param value
     * @return boolean
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** 去除小数末尾可删除的0 1.0->1 1.50->1.5 */
    private static NumberFormat removeZero = NumberFormat.getInstance();

    public static String removeZero(String num) {
        return removeZero(BigDecimal.valueOf(num));
    }

    public static String removeZero(BigDecimal num) {
        return removeZero.format(num);
    }
}
