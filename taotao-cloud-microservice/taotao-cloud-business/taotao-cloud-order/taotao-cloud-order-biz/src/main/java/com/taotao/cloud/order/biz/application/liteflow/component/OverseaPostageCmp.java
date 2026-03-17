package com.taotao.cloud.order.biz.application.liteflow.component;

import com.taotao.cloud.order.biz.application.liteflow.bean.PriceStepVO;
import com.taotao.cloud.order.biz.application.liteflow.enums.PriceTypeEnum;
import com.taotao.cloud.order.biz.application.liteflow.slot.PriceContext;
import com.yomahub.liteflow.core.NodeComponent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 境外购运费计算组件
 */
@Component("overseaPostageCmp")
public class OverseaPostageCmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        PriceContext context = this.getContextBean(PriceContext.class);

        /**这里Mock境外购运费的策略是：不管多少钱，都要加上15元运费**/
        BigDecimal postage = new BigDecimal(15);
        BigDecimal prePrice = context.getLastestPriceStep().getCurrPrice();
        BigDecimal currPrice = prePrice.add(postage);

        context.addPriceStep(new PriceStepVO(PriceTypeEnum.OVERSEAS_POSTAGE,
                null,
                prePrice,
                currPrice.subtract(prePrice),
                currPrice,
                PriceTypeEnum.OVERSEAS_POSTAGE.getName()));
    }
}
