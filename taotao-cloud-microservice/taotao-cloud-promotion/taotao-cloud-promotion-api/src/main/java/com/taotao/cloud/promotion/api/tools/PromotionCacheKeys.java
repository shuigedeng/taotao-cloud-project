package com.taotao.cloud.promotion.api.tools;


import com.taotao.cloud.common.enums.CachePrefix;

/**
 * 满额活动缓存Key
 * 
 * @since 2020/10/12
 **/
public class PromotionCacheKeys {

    /**
     * 读取满优惠redis key
     * @param activityId 活动ID
     * @return 满优惠redis key
     */
    public static String getFullDiscountKey(String activityId){
        //return CachePrefix.STORE_ID_FULL_DISCOUNT + "::" + activityId;
	    return null;
    }

    /**
     * 读取满优惠redis key
     * @param id id
     * @return 满优惠redis key
     */
    public static String getPromotionGoodsKey(String id){
        //return CachePrefix.PROMOTION_GOODS + "::" + id;
	    return "";
    }

    /**
     * 读取秒杀活动redis key
     * @param timeStr 时间字符串（格式为 yyyyMMdd）
     * @return 满优惠redis key
     */
    public static String getSeckillTimelineKey(String timeStr){
        //return CachePrefix.STORE_ID_SECKILL + "::" + timeStr;
	    return null;
    }

}
