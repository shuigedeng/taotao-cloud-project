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
// import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
// import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
// import com.baomidou.mybatisplus.core.metadata.IPage;
// import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
// import com.taotao.cloud.common.enums.ResultEnum;
// import com.taotao.cloud.common.exception.BusinessException;
// import com.taotao.cloud.common.model.PageQuery;
// import com.taotao.cloud.common.utils.common.SecurityUtil;
// import com.taotao.cloud.store.api.vo.StoreCollectionVO;
// import com.taotao.cloud.member.biz.entity.MemberStoreCollection;
// import com.taotao.cloud.member.biz.mapper.StoreCollectionMapper;
// import com.taotao.cloud.store.api.dto.CollectionDTO;
// import com.taotao.cloud.store.api.feign.IFeignStoreService;
// import java.util.Optional;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
//
// /**
//  * 会员店铺收藏业务层实现
//  */
// @Service
// public class StoreCollectionServiceImpl extends
// 	ServiceImpl<StoreCollectionMapper, MemberStoreCollection> implements
// 	StoreCollectionService {
//
// 	@Autowired
// 	private IFeignStoreService feignStoreService;
//
// 	@Override
// 	public IPage<StoreCollectionVO> storeCollection(PageQuery PageQuery) {
// 		QueryWrapper<StoreCollectionVO> queryWrapper = new QueryWrapper();
// 		queryWrapper.eq("sc.member_id", SecurityUtil.getUserId());
// 		queryWrapper.orderByDesc("sc.create_time");
// 		return this.baseMapper.storeCollectionVOList(
// 			PageQuery.buildMpPage(), queryWrapper);
// 	}
//
// 	@Override
// 	public Boolean isCollection(String storeId) {
// 		QueryWrapper<MemberStoreCollection> queryWrapper = new QueryWrapper();
// 		queryWrapper.eq("member_id", SecurityUtil.getUserId());
// 		queryWrapper.eq("store_id", storeId);
// 		return Optional.ofNullable(this.getOne(queryWrapper)).isPresent();
// 	}
//
// 	@Override
// 	public Boolean addStoreCollection(String storeId) {
// 		if (this.getOne(new LambdaUpdateWrapper<MemberStoreCollection>()
// 			.eq(MemberStoreCollection::getMemberId, SecurityUtil.getUserId())
// 			.eq(MemberStoreCollection::getStoreId, storeId)) == null) {
// 			MemberStoreCollection memberStoreCollection = new MemberStoreCollection(
// 				SecurityUtil.getUserId(), storeId);
// 			this.save(memberStoreCollection);
// 			feignStoreService.updateStoreCollectionNum(new CollectionDTO(storeId, 1));
// 			return true;
// 		}
// 		throw new BusinessException(ResultEnum.USER_COLLECTION_EXIST);
// 	}
//
// 	@Override
// 	public Boolean deleteStoreCollection(String storeId) {
// 		QueryWrapper<MemberStoreCollection> queryWrapper = new QueryWrapper<>();
// 		queryWrapper.eq("member_id", SecurityUtil.getUserId());
// 		queryWrapper.eq("store_id", storeId);
// 		feignStoreService.updateStoreCollectionNum(new CollectionDTO(storeId, -1));
// 		return this.remove(queryWrapper);
// 	}
// }
