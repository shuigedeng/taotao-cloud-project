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

// import com.taotao.cloud.store.api.model.dto.StoreSettlementDay;
// import com.taotao.cloud.store.biz.service.IBillService;
// import com.taotao.cloud.store.biz.service.IStoreDetailService;
// import com.taotao.boot.webmvc.timetask.EveryDayExecute;
// import java.time.LocalDateTime;
// import java.util.List;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
//
// /** 店铺结算执行 */
// @Component
// public class BillExecute implements EveryDayExecute {
//
//     /** 结算单 */
//     @Autowired
//     private IBillService billService;
//     /** 店铺详情 */
//     @Autowired
//     private IStoreDetailService storeDetailService;
//
//     /** 1.查询今日待结算的商家 2.查询商家上次结算日期，生成本次结算单 3.记录商家结算日 */
//     @Override
//     public void execute() {
//
//         // 获取当前天数
//         int day = DateUtil.date().dayOfMonth();
//
//         // 获取待结算商家列表
//         List<StoreSettlementDay> storeList = storeDetailService.getSettlementStore(day);
//
//         // 获取当前时间
//         LocalDateTime endTime = LocalDateTime.now();
//         // 批量商家结算
//         for (StoreSettlementDay storeSettlementDay : storeList) {
//
//             // 生成结算单
//             billService.createBill(
//                     storeSettlementDay.getStoreId(), storeSettlementDay.getSettlementDay(), LocalDateTime.now());
//
//             // 修改店铺结算时间
//             storeDetailService.updateSettlementDay(storeSettlementDay.getStoreId(), endTime);
//         }
//     }
// }
