package com.taotao.cloud.sys.biz.service.impl;

import com.taotao.cloud.sys.biz.service.IBussinessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.seata.core.context.RootContext;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;

//https://blog.csdn.net/weixin_46209120/article/details/132223385
/**
 * BusinessServiceImpl
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Service
@Slf4j
public class BusinessServiceImpl implements IBussinessService {

    @Override
    @GlobalTransactional(name = "createOrder", rollbackFor = Exception.class)
    public boolean saveOrder() {
        log.info("=============用户下单=================");
        log.info("当前 XID: {}", RootContext.getXID());

//        //获取全局唯一订单号
//        Long orderId = UUIDGenerator.generateUUID();
//        //阶段一： 创建订单
//        Order order = orderService.prepareSaveOrder(orderVo,orderId);
//        //扣减库存
//        storageFeignService.deduct(orderVo.getCommodityCode(), orderVo.getCount());
//        //扣减余额
//        accountFeignService.debit(orderVo.getUserId(), orderVo.getMoney());
        return true;
    }
}
