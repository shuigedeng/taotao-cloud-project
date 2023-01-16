package com.taotao.cloud.payment.biz.bootx.core.paymodel.wallet.service;

import cn.bootx.common.core.exception.DataNotExistException;
import cn.bootx.common.core.rest.PageResult;
import cn.bootx.common.core.rest.param.PageQuery;
import cn.bootx.common.mybatisplus.util.MpUtil;
import cn.bootx.iam.core.user.service.UserAdminService;
import cn.bootx.iam.dto.user.UserInfoDto;
import cn.bootx.iam.param.user.UserInfoParam;
import cn.bootx.payment.core.paymodel.wallet.dao.WalletManager;
import cn.bootx.payment.core.paymodel.wallet.entity.Wallet;
import cn.bootx.payment.dto.paymodel.wallet.WalletDto;
import cn.bootx.payment.dto.paymodel.wallet.WalletInfoDto;
import cn.bootx.payment.param.paymodel.wallet.WalletPayParam;
import cn.bootx.starter.auth.util.SecurityUtil;
import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 钱包
 * @author xxm
 * @date 2022/3/11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WalletQueryService {
    private final WalletManager walletManager;
    private final UserAdminService userAdminService;

    /**
     * 根据ID查询Wallet
     */
    public WalletDto findById(Long walletId) {
        return walletManager.findById(walletId).map(Wallet::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 根据用户ID查询钱包
     */
    public WalletDto findByUser() {
        Long userId = SecurityUtil.getUserId();
        return walletManager.findByUser(userId).map(Wallet::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 获取钱包综合信息
     */
    public WalletInfoDto getWalletInfo(Long walletId){
        Wallet wallet = walletManager.findById(walletId).orElseThrow(DataNotExistException::new);
        UserInfoDto userInfoDto = userAdminService.findById(wallet.getUserId());
        WalletInfoDto walletInfoDto = new WalletInfoDto();
        BeanUtil.copyProperties(wallet,walletInfoDto);
        walletInfoDto.setUserName(userInfoDto.getName());
        return walletInfoDto;
    }

    /**
     * 查询用户 分页
     */
    public PageResult<WalletDto> page(PageQuery PageQuery, WalletPayParam param){
        return MpUtil.convert2DtoPageResult(walletManager.page(PageQuery,param));
    }

    /**
     * 待开通钱包的用户列表
     */
    public PageResult<UserInfoDto> pageByNotWallet(PageQuery PageQuery, UserInfoParam userInfoParam){
        return MpUtil.convert2DtoPageResult(walletManager.pageByNotWallet(PageQuery,userInfoParam));
    }

}
