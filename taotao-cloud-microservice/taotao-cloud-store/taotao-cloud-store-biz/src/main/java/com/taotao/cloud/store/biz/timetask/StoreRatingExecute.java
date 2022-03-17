package com.taotao.cloud.sys.biz.timetask.xxljob.storerating;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.taotao.cloud.store.api.enums.StoreStatusEnum;
import com.taotao.cloud.store.biz.service.StoreService;
import com.taotao.cloud.web.timetask.EveryDayExecute;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 店铺评分
 */
@Component
public class StoreRatingExecute implements EveryDayExecute {
    /**
     * 店铺
     */
    @Autowired
    private StoreService storeService;
    /**
     * 会员评价
     */
    @Resource
    private MemberEvaluationMapper memberEvaluationMapper;


    @Override
    public void execute() {
        //获取所有开启的店铺
        List<Store> storeList = storeService.list(new LambdaQueryWrapper<Store>().eq(Store::getStoreDisable, StoreStatusEnum.OPEN.name()));
        for (Store store : storeList) {
            //店铺所有开启的评价
            LambdaQueryWrapper<MemberEvaluation> lambdaQueryWrapper = Wrappers.lambdaQuery();
            lambdaQueryWrapper.eq(MemberEvaluation::getStoreId, store.getId());
            lambdaQueryWrapper.eq(MemberEvaluation::getStatus, SwitchEnum.OPEN.name());
            StoreRatingVO storeRatingVO = memberEvaluationMapper.getStoreRatingVO(lambdaQueryWrapper);

            if (storeRatingVO != null) {
                //保存评分
                LambdaUpdateWrapper<Store> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
                lambdaUpdateWrapper.eq(Store::getId, store.getId());
                lambdaUpdateWrapper.set(Store::getDescriptionScore, storeRatingVO.getDescriptionScore());
                lambdaUpdateWrapper.set(Store::getDeliveryScore, storeRatingVO.getDeliveryScore());
                lambdaUpdateWrapper.set(Store::getServiceScore, storeRatingVO.getServiceScore());
                storeService.update(lambdaUpdateWrapper);
            }

        }


    }
}
