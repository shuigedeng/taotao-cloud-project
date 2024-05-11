package com.taotao.cloud.design.patterns.pipeline.demo;

import com.taotao.cloud.design.patterns.pipeline.AbstractDemoJob;
import com.taotao.cloud.design.patterns.pipeline.DemoPipelineProduct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 审核-信息提交-业务实现
 *
 * @author 
 * @date 2023/05/12 14:36
 */
@Service
@Slf4j
public class ManagerSubmitJob extends AbstractDemoJob {

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
        try {
            /*
             * DB操作
             */
            log.info("任务[{}]信息提交,线程号:{}", JSONUtil.toJsonStr(userRequestData), tradeId);
            productData.setUserResponseData(DemoResp.buildRes("成功"));
        } catch (Exception ex) {
            log.error("审核-信息提交-DB操作失败,入参:{}", JSONUtil.toJsonStr(userRequestData), ex);
            throw ex;
        }
        return DemoPipelineProduct.DemoSignalEnum.NORMAL;
    }
}
