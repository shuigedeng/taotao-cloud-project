package com.taotao.cloud.member.biz.service.business.impl;// package com.taotao.cloud.member.biz.service.impl;
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
// public class StoreLogisticsServiceImpl extends ServiceImpl<StoreLogisticsMapper, StoreLogistics> implements
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
