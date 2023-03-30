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

package com.taotao.cloud.member.biz.service.business.impl; // package
                                                           // com.taotao.cloud.member.biz.service.impl;
//
// import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// import com.baomidou.mybatisplus.core.toolkit.Wrappers;
// import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
// import com.taotao.cloud.member.biz.mapper.StoreLogisticsMapper;
// import com.taotao.cloud.member.biz.service.StoreLogisticsService;
// import org.springframework.stereotype.Service;
//
// import java.util.List;
//
// /**
//  * 物流公司业务层实现
//  */
// @Service
// public class StoreLogisticsServiceImpl extends ServiceImpl<StoreLogisticsMapper, StoreLogistics>
// implements
// 	StoreLogisticsService {
//
//     @Override
//     public List<StoreLogisticsVO> getStoreLogistics(String storeId) {
//         return this.baseMapper.getStoreLogistics(storeId);
//     }
//
//     @Override
//     public List<StoreLogisticsVO> getStoreSelectedLogistics(String storeId) {
//         return this.baseMapper.getSelectedStoreLogistics(storeId);
//
//     }
//
//     @Override
//     public List<String> getStoreSelectedLogisticsName(String storeId) {
//         return this.baseMapper.getSelectedStoreLogisticsName(storeId);
//     }
//
//     @Override
//     public StoreLogistics add(String logisticsId, String storeId) {
//         //判断是否已经选择过，如果没有选择则进行添加
//         LambdaQueryWrapper<StoreLogistics> lambdaQueryWrapper = Wrappers.lambdaQuery();
//         lambdaQueryWrapper.eq(StoreLogistics::getLogisticsId, logisticsId);
//         lambdaQueryWrapper.eq(StoreLogistics::getStoreId, storeId);
//         if (this.getOne(lambdaQueryWrapper) == null) {
//             StoreLogistics storeLogistics = new StoreLogistics(storeId, logisticsId);
//             this.save(storeLogistics);
//             return storeLogistics;
//         }
//         return null;
//     }
//
//
// }
