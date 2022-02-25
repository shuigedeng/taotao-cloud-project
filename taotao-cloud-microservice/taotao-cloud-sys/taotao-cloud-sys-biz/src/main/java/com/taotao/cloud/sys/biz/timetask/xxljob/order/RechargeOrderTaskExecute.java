//package com.taotao.cloud.sys.biz.timetask.xxljob.order;
//
//import cn.hutool.core.date.DateTime;
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.json.JSONUtil;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.taotao.cloud.web.timetask.EveryMinuteExecute;
//import java.util.List;
//import java.util.stream.Collectors;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * 充值订单自动取消（每分钟执行）
// *
// * @author zhuhai
// * @since 2021/3/11
// **/
//@Slf4j
//@Component
//public class RechargeOrderTaskExecute implements EveryMinuteExecute {
//    /**
//     * 充值
//     */
//    @Autowired
//    private RechargeService rechargeService;
//    /**
//     * 设置
//     */
//    @Autowired
//    private SettingService settingService;
//
//
//    @Override
//    public void execute() {
//        Setting setting = settingService.get(SettingEnum.ORDER_SETTING.name());
//        OrderSetting orderSetting = JSONUtil.toBean(setting.getSettingValue(), OrderSetting.class);
//        if (orderSetting != null && orderSetting.getAutoCancel() != null) {
//            //充值订单自动取消时间 = 当前时间 - 自动取消时间分钟数
//            DateTime cancelTime = DateUtil.offsetMinute(DateUtil.date(), -orderSetting.getAutoCancel());
//            LambdaQueryWrapper<Recharge> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(Recharge::getPayStatus, PayStatusEnum.UNPAID.name());
//            //充值订单创建时间 <= 订单自动取消时间
//            queryWrapper.le(Recharge::getCreateTime, cancelTime);
//            List<Recharge> list = rechargeService.list(queryWrapper);
//            List<String> cancelSnList = list.stream().map(Recharge::getRechargeSn).collect(Collectors.toList());
//            for (String sn : cancelSnList) {
//                rechargeService.rechargeOrderCancel(sn);
//            }
//        }
//    }
//}
