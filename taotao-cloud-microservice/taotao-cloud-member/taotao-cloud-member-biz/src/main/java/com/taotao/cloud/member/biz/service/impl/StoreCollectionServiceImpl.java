package com.taotao.cloud.member.biz.service.impl;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.member.api.dto.CollectionDTO;
import com.taotao.cloud.member.api.vo.StoreCollectionVO;
import com.taotao.cloud.member.biz.entity.MemberStoreCollection;
import com.taotao.cloud.member.biz.mapper.StoreCollectionMapper;
import com.taotao.cloud.member.biz.service.StoreCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 会员店铺收藏业务层实现
 *
 *
 * @since 2020/11/18 2:52 下午
 */
@Service
public class StoreCollectionServiceImpl extends ServiceImpl<StoreCollectionMapper, MemberStoreCollection> implements
	StoreCollectionService {


    @Autowired
    private StoreService storeService;

    @Override
    public IPage<StoreCollectionVO> storeCollection(PageVO pageVo) {
        QueryWrapper<StoreCollectionVO> queryWrapper = new QueryWrapper();
        queryWrapper.eq("sc.member_id", UserContext.getCurrentUser().getId());
        queryWrapper.orderByDesc("sc.create_time");
        return this.baseMapper.storeCollectionVOList(PageUtil.initPage(pageVo), queryWrapper);
    }

    @Override
    public boolean isCollection(String storeId) {
        QueryWrapper<MemberStoreCollection> queryWrapper = new QueryWrapper();
        queryWrapper.eq("member_id", UserContext.getCurrentUser().getId());
        queryWrapper.eq("store_id", storeId);
        return Optional.ofNullable(this.getOne(queryWrapper)).isPresent();
    }

    @Override
    public MemberStoreCollection addStoreCollection(String storeId) {
        if (this.getOne(new LambdaUpdateWrapper<MemberStoreCollection>()
                .eq(MemberStoreCollection::getMemberId, UserContext.getCurrentUser().getId())
                .eq(MemberStoreCollection::getStoreId, storeId)) == null) {
            MemberStoreCollection memberStoreCollection = new MemberStoreCollection(UserContext.getCurrentUser().getId(), storeId);
            this.save(memberStoreCollection);
            storeService.updateStoreCollectionNum(new CollectionDTO(storeId, 1));
            return memberStoreCollection;
        }
        throw new ServiceException(ResultCode.USER_COLLECTION_EXIST);
    }

    @Override
    public boolean deleteStoreCollection(String storeId) {
        QueryWrapper<MemberStoreCollection> queryWrapper = new QueryWrapper();
        queryWrapper.eq("member_id", UserContext.getCurrentUser().getId());
        queryWrapper.eq("store_id", storeId);
        storeService.updateStoreCollectionNum(new CollectionDTO(storeId, -1));
        return this.remove(queryWrapper);
    }
}
