//package com.taotao.cloud.sys.biz.timetask.xxljob.promotion;
//
//import com.google.gson.Gson;
//import com.taotao.cloud.web.timetask.EveryDayExecute;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * 促销活动每日定时器
// *
// * @author Chopper
// * @since 2021/3/18 3:23 下午
// */
//@Slf4j
//@Component
//public class PromotionEverydayExecute implements EveryDayExecute {
//
//    /**
//     * ES商品索引
//     */
//    @Autowired
//    private EsGoodsIndexService esGoodsIndexService;
//    /**
//     * 系统设置
//     */
//    @Autowired
//    private SettingService settingService;
//    /**
//     * 秒杀活动
//     */
//    @Autowired
//    private SeckillService seckillService;
//
//    /**
//     * 将已过期的促销活动置为结束
//     */
//    @Override
//    public void execute() {
//        //清除所以商品索引的无效促销活动
//        this.esGoodsIndexService.cleanInvalidPromotion();
//        //定时创建活动
//        addSeckill();
//
//    }
//
//    /**
//     * 添加秒杀活动
//     * 从系统设置中获取秒杀活动的配置
//     * 添加30天后的秒杀活动
//     */
//    private void addSeckill() {
//        Setting setting = settingService.get(SettingEnum.SECKILL_SETTING.name());
//        SeckillSetting seckillSetting = new Gson().fromJson(setting.getSettingValue(), SeckillSetting.class);
//        for (int i = 1; i <= SeckillService.PRE_CREATION; i++) {
//            Seckill seckill = new Seckill(i, seckillSetting.getHours(), seckillSetting.getSeckillRule());
//            seckillService.savePromotions(seckill);
//        }
//    }
//}
