package com.taotao.cloud.design.patterns.pipeline.demo;

import com.taotao.cloud.design.patterns.pipeline.AbstractDemoJob;
import com.taotao.cloud.design.patterns.pipeline.DemoPipelineProduct;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.json.JSONUtil;
import org.springframework.stereotype.Service;

/**
 * 解锁-实现层
 *
 * @author 
 * @date 2023/05/17 17:00
 */
@Service
@Slf4j
public class CheckRequestUnLockJob extends AbstractDemoJob {

    /**
     * 子类执行逻辑
     *
     * @param tradeId     流程Id
     * @param productData 请求数据
     * @return
     * @throws Exception 异常
     */
    @Override
    DemoPipelineProduct.DemoSignalEnum execute(String tradeId, DemoPipelineProduct.DemoProductData productData) throws Exception {
        DemoReq userRequestData = productData.getUserRequestData();
        log.info("任务[{}]解锁,线程号:{}", JSONUtil.toJsonStr(userRequestData), tradeId);
        return DemoPipelineProduct.DemoSignalEnum.NORMAL;
    }
}
