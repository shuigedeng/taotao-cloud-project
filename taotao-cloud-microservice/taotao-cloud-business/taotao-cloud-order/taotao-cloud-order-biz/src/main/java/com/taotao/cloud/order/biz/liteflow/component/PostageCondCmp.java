package com.yomahub.liteflow.example.component;

import com.yomahub.liteflow.core.NodeSwitchComponent;
import com.yomahub.liteflow.example.slot.PriceContext;
import org.springframework.stereotype.Component;

/**
 * 运费条件组件
 */
@Component("postageCondCmp")
public class PostageCondCmp extends NodeSwitchComponent {
    @Override
    public String processSwitch() throws Exception {
        PriceContext context = this.getContextBean(PriceContext.class);
        //根据参数oversea来判断是否境外购，转到相应的组件
        boolean oversea = context.isOversea();
        if(oversea){
            return "overseaPostageCmp";
        }else{
            return "postageCmp";
        }
    }
}
