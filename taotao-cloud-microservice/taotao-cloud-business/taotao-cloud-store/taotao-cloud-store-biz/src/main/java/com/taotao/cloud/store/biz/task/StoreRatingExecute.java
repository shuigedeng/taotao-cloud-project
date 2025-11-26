/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.store.biz.task;

// import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
// import com.baomidou.mybatisplus.core.toolkit.Wrappers;
// import com.taotao.boot.common.enums.SwitchEnum;
// import com.taotao.cloud.member.api.feign.IFeignMemberEvaluationApi;
// import com.taotao.cloud.member.api.model.vo.StoreRatingVO;
// import com.taotao.cloud.store.api.enums.StoreStatusEnum;
// import com.taotao.cloud.store.biz.model.entity.Store;
// import com.taotao.cloud.store.biz.service.IStoreService;
// import com.taotao.boot.web.timetask.EveryDayExecute;
// import jakarta.annotation.Resource;
// import java.util.List;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
//
// /** 店铺评分 */
// @Component
// public class StoreRatingExecute implements EveryDayExecute {
//
//     /** 店铺 */
//     @Autowired
//     private IStoreService storeService;
//     /** 会员评价 */
//     @Resource
//     private IFeignMemberEvaluationApi memberEvaluationApi;
//
//     @Override
//     public void execute() {
//         // 获取所有开启的店铺
//         List<Store> storeList = storeService.list(
//                 new LambdaQueryWrapper<Store>().eq(Store::getStoreDisable, StoreStatusEnum.OPEN.name()));
//         for (Store store : storeList) {
//             // 店铺所有开启的评价
//             StoreRatingVO storeRatingVO = memberEvaluationApi.getStoreRatingVO(store.getId(), SwitchEnum.OPEN.name());
//
//             if (storeRatingVO != null) {
//                 // 保存评分
//                 LambdaUpdateWrapper<Store> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
//                 lambdaUpdateWrapper.eq(Store::getId, store.getId());
//                 lambdaUpdateWrapper.set(Store::getDescriptionScore, storeRatingVO.getDescriptionScore());
//                 lambdaUpdateWrapper.set(Store::getDeliveryScore, storeRatingVO.getDeliveryScore());
//                 lambdaUpdateWrapper.set(Store::getServiceScore, storeRatingVO.getServiceScore());
//                 storeService.update(lambdaUpdateWrapper);
//             }
//         }
//     }
// }
