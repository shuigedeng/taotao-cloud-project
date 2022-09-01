package com.taotao.cloud.office.easypoi.easypoi.util;

import cn.afterturn.easypoi.util.PoiDataDesensitizationUtil;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class PoiDataDesensitizationUtilTest {

    @Test
    public void starEnd() {
        System.out.println(PoiDataDesensitizationUtil.desensitization("3_4","12312345678"));
        System.out.println(PoiDataDesensitizationUtil.desensitization("4_3","12312345678"));
        System.out.println(PoiDataDesensitizationUtil.desensitization("1_3","371234199001011234"));
        System.out.println(PoiDataDesensitizationUtil.desensitization("0_3","371234199001011234"));
        System.out.println(PoiDataDesensitizationUtil.desensitization("6_0","371234199001011234"));
        System.out.println(PoiDataDesensitizationUtil.desensitization("18_3","371234199001011234"));
        System.out.println(PoiDataDesensitizationUtil.desensitization("20_3","371234199001011234"));
        System.out.println(PoiDataDesensitizationUtil.desensitization("0_30","371234199001011234"));
    }


    @Test
    public void subMax() {
        System.out.println(PoiDataDesensitizationUtil.desensitization("1,3","张"));
        System.out.println(PoiDataDesensitizationUtil.desensitization("0,6","张"));
        System.out.println(PoiDataDesensitizationUtil.desensitization("2,3","张"));
        System.out.println(PoiDataDesensitizationUtil.desensitization("1,3","张三"));
        System.out.println(PoiDataDesensitizationUtil.desensitization("0,3","张全蛋"));
        System.out.println(PoiDataDesensitizationUtil.desensitization("0,5","李张全蛋"));
        System.out.println(PoiDataDesensitizationUtil.desensitization("0,3","李张全蛋"));
        System.out.println(PoiDataDesensitizationUtil.desensitization("0,1","李张全蛋"));
        System.out.println(PoiDataDesensitizationUtil.desensitization("0,3","尼古拉斯.李张全蛋"));
        System.out.println(PoiDataDesensitizationUtil.desensitization("10,3","尼古拉斯.李张全蛋"));
    }

    @Test
    public void markSpilt() {
        System.out.println(PoiDataDesensitizationUtil.desensitization("3~@","111111@qq.com"));
        System.out.println(PoiDataDesensitizationUtil.desensitization("1~@","111111@qq.com"));
        System.out.println(PoiDataDesensitizationUtil.desensitization("1~@","111@11@qq.com"));
        System.out.println(PoiDataDesensitizationUtil.desensitization("1~@","@qq.com"));
        System.out.println(PoiDataDesensitizationUtil.desensitization("0~@","qq.com"));
    }
}
