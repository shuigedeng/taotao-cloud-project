package com.taotao.cloud.order.biz.application.liteflow.component;

import com.taotao.cloud.order.biz.application.liteflow.bean.PriceStepVO;
import com.taotao.cloud.order.biz.application.liteflow.bean.ProductPackVO;
import com.taotao.cloud.order.biz.application.liteflow.enums.PriceTypeEnum;
import com.taotao.cloud.order.biz.application.liteflow.slot.PriceContext;
import com.yomahub.liteflow.core.NodeComponent;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 价格步骤初始化器(把原价初始化进去)
 */
@Component("priceStepInitCmp")
public class PriceStepInitCmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        PriceContext context = this.getContextBean(PriceContext.class);

        //初始化价格步骤
        List<ProductPackVO> packList = context.getProductPackList();
        BigDecimal totalOriginalPrice = new BigDecimal(0);
        for(ProductPackVO packItem : packList){
            totalOriginalPrice = totalOriginalPrice.add(packItem.getSalePrice().multiply(new BigDecimal(packItem.getCount())));
        }
        context.addPriceStep(new PriceStepVO(PriceTypeEnum.ORIGINAL,
                null,
                null,
                totalOriginalPrice,
                totalOriginalPrice,
                PriceTypeEnum.ORIGINAL.getName()));
        context.setOriginalOrderPrice(totalOriginalPrice);
    }

    @Override
    public boolean isAccess() {
        PriceContext context = this.getContextBean(PriceContext.class);
        if(CollectionUtils.isNotEmpty(context.getProductPackList())){
            return true;
        }else{
            return false;
        }
    }
}
