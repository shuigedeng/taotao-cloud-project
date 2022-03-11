package com.taotao.cloud.member.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.member.api.vo.StoreCollectionVO;
import com.taotao.cloud.member.biz.entity.MemberStoreCollection;

/**
 * 店铺收藏业务层
 *
 * 
 * @since 2020/11/18 2:52 下午
 */
public interface StoreCollectionService extends IService<MemberStoreCollection> {

    /**
     * 店铺收藏分页
     * @param pageVo 分页VO
     * @return 店铺收藏分页列表
     */
    IPage<StoreCollectionVO> storeCollection(PageVO pageVo);

    /**
     * 是否收藏此店铺
     *
     * @param storeId 店铺ID
     * @return 是否收藏
     */
    boolean isCollection(String storeId);

    /**
     * 店铺商品收藏
     *
     * @param storeId 店铺ID
     * @return 操作状态
     */
    MemberStoreCollection addStoreCollection(String storeId);

    /**
     * 店铺收藏
     *
     * @param storeId 店铺ID
     * @return 操作状态
     */
    boolean deleteStoreCollection(String storeId);
}
