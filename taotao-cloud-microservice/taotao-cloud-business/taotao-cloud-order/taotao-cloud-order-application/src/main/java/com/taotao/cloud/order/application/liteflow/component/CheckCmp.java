package com.taotao.cloud.order.application.liteflow.component;

import com.yomahub.liteflow.example.bean.PriceCalcReqVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 初始化参数检查组件
 */
@Component("checkCmp")
public class CheckCmp extends NodeComponent {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void process() throws Exception {
        //拿到请求参数
        PriceCalcReqVO req = this.getSlot().getRequestData();

//        log.info("参数验证完成");
    }
}
