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

package com.taotao.cloud.payment.biz.bootx.core.paymodel.wallet.service;

import cn.bootx.common.core.rest.PageResult;
import cn.bootx.common.core.rest.param.PageQuery;
import cn.bootx.common.mybatisplus.util.MpUtil;
import cn.bootx.payment.core.paymodel.wallet.dao.WalletLogManager;
import cn.bootx.payment.dto.paymodel.wallet.WalletLogDto;
import cn.bootx.payment.param.paymodel.wallet.WalletLogQueryParam;
import cn.bootx.starter.auth.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 钱包日志
 *
 * @author xxm
 * @date 2020/12/8
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WalletLogService {
    private final WalletLogManager walletLogManager;

    /** 个人钱包日志分页 */
    public PageResult<WalletLogDto> pageByPersonal(PageQuery PageQuery, WalletLogQueryParam param) {
        Long userId = SecurityUtil.getUserId();
        return MpUtil.convert2DtoPageResult(
                walletLogManager.pageByUserId(PageQuery, param, userId));
    }

    /** 钱包日志分页 */
    public PageResult<WalletLogDto> page(PageQuery PageQuery, WalletLogQueryParam param) {
        return MpUtil.convert2DtoPageResult(walletLogManager.page(PageQuery, param));
    }

    /** 根据钱包id查询钱包日志(分页) */
    public PageResult<WalletLogDto> pageByWalletId(PageQuery PageQuery, WalletLogQueryParam param) {
        return MpUtil.convert2DtoPageResult(walletLogManager.pageByWalletId(PageQuery, param));
    }
}
