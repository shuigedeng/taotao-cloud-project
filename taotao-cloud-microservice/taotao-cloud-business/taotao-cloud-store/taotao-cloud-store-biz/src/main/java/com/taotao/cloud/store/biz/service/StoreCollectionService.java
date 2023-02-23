package com.taotao.cloud.store.biz.service;// package com.taotao.cloud.store.biz.service;
//
// import com.baomidou.mybatisplus.core.metadata.IPage;
// import com.baomidou.mybatisplus.extension.service.IService;
// import com.taotao.cloud.common.model.PageQuery;
// import com.taotao.cloud.store.api.vo.StoreCollectionVO;
// import com.taotao.cloud.member.biz.entity.MemberStoreCollection;
//
// /**
//  * 店铺收藏业务层
//  */
// public interface StoreCollectionService extends IService<MemberStoreCollection> {
//
//     /**
//      * 店铺收藏分页
//      * @param PageQuery 分页VO
//      * @return 店铺收藏分页列表
//      */
//     IPage<StoreCollectionVO> storeCollection(PageQuery PageQuery);
//
//     /**
//      * 是否收藏此店铺
//      *
//      * @param storeId 店铺ID
//      * @return 是否收藏
//      */
//     Boolean isCollection(String storeId);
//
//     /**
//      * 店铺商品收藏
//      *
//      * @param storeId 店铺ID
//      * @return 操作状态
//      */
//     Boolean addStoreCollection(String storeId);
//
//     /**
//      * 店铺收藏
//      *
//      * @param storeId 店铺ID
//      * @return 操作状态
//      */
//     Boolean deleteStoreCollection(String storeId);
// }
