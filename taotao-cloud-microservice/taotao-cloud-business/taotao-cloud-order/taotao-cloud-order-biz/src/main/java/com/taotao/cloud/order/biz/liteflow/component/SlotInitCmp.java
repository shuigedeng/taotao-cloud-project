package com.yomahub.liteflow.example.component;

import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.example.bean.PriceCalcReqVO;
import com.yomahub.liteflow.example.slot.PriceContext;
import org.springframework.stereotype.Component;

/**
 * Slot初始化组件
 */
@Component("slotInitCmp")
public class SlotInitCmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        //把主要参数冗余到slot里
        PriceCalcReqVO req = this.getRequestData();
        PriceContext context = this.getContextBean(PriceContext.class);
        context.setOrderNo(req.getOrderNo());
        context.setOversea(req.isOversea());
        context.setMemberCode(req.getMemberCode());
        context.setOrderChannel(req.getOrderChannel());
        context.setProductPackList(req.getProductPackList());
        context.setCouponId(req.getCouponId());
    }

    @Override
    public boolean isAccess() {
        PriceCalcReqVO req = this.getSlot().getRequestData();
        if(req != null){
            return true;
        }else{
            return false;
        }
    }
}
