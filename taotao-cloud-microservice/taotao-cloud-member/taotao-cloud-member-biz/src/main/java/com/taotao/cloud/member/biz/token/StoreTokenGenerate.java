package com.taotao.cloud.member.biz.token;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.enums.UserEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.member.biz.connect.token.Token;
import com.taotao.cloud.member.biz.connect.token.base.AbstractTokenGenerate;
import com.taotao.cloud.member.biz.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 商家token生成
 */
@Component
public class StoreTokenGenerate extends AbstractTokenGenerate<Member> {
    @Autowired
    private StoreService storeService;
    @Autowired
    private TokenUtil tokenUtil;

    @Override
    public Token createToken(Member member, Boolean longTerm) {
        if (Boolean.FALSE.equals(member.getHaveStore())) {
            throw new BusinessException(ResultEnum.STORE_NOT_OPEN);
        }
        LambdaQueryWrapper<Store> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Store::getMemberId, member.getId());
        Store store = storeService.getOne(queryWrapper);
        AuthUser authUser = new AuthUser(member.getUsername(), member.getId(), member.getNickName(), store.getStoreLogo(), UserEnum.STORE);

        authUser.setStoreId(store.getId());
        authUser.setStoreName(store.getStoreName());
        return tokenUtil.createToken(member.getUsername(), authUser, longTerm, UserEnum.STORE);
    }

    @Override
    public Token refreshToken(String refreshToken) {
        return tokenUtil.refreshToken(refreshToken, UserEnum.STORE);
    }

}
