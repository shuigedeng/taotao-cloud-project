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

package com.taotao.cloud.payment.biz.bootx.core.paymodel.voucher.service;

import cn.hutool.db.PageResult;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.voucher.dao.VoucherManager;
import com.taotao.cloud.payment.biz.bootx.dto.paymodel.voucher.VoucherDto;
import com.taotao.cloud.payment.biz.bootx.param.paymodel.voucher.VoucherParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 储值卡查询
 *
 * @author xxm
 * @date 2022/3/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherQueryService {
    private final VoucherManager voucherManager;

    /** 分页 */
    public PageResult<VoucherDto> page(PageQuery PageQuery, VoucherParam param) {
        return MpUtil.convert2DtoPageResult(voucherManager.page(PageQuery, param));
    }

    /** 根据id查询 */
    public VoucherDto findById(Long id) {
        return voucherManager
                .findById(id)
                .map(Voucher::toDto)
                .orElseThrow(DataNotExistException::new);
    }

    /** 根据卡号查询 */
    public VoucherDto findByCardNo(String cardNo) {
        return voucherManager
                .findByCardNo(cardNo)
                .map(Voucher::toDto)
                .orElseThrow(DataNotExistException::new);
    }
}
