package com.taotao.cloud.realtime.mall.bean;


import java.math.BigDecimal;

/**
 *
 * Date: 2021/2/24
 * Desc:地区统计宽表实体类
 */

public class ProvinceStats {

    private String stt;
    private String edt;
    private Long province_id;
    private String province_name;
    private String area_code;
    private String iso_code;
    private String iso_3166_2;
    private BigDecimal order_amount;
    private Long  order_count;
    private Long ts;

    public ProvinceStats(OrderWide orderWide){
        province_id = orderWide.getProvince_id();
        order_amount = orderWide.getSplit_total_amount();
        province_name=orderWide.getProvince_name();
        area_code=orderWide.getProvince_area_code();
        iso_3166_2=orderWide.getProvince_iso_code();
        iso_code=orderWide.getProvince_iso_code();

        order_count = 1L;
        ts=System.currentTimeMillis();
    }
}

