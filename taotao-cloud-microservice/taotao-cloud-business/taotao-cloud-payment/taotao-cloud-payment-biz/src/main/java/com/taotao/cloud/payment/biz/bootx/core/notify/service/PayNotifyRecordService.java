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

package com.taotao.cloud.payment.biz.bootx.core.notify.service;

import cn.hutool.db.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.payment.biz.bootx.core.notify.dao.PayNotifyRecordManager;
import com.taotao.cloud.payment.biz.bootx.core.notify.entity.PayNotifyRecord;
import com.taotao.cloud.payment.biz.bootx.dto.notify.PayNotifyRecordDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 回调记录
 *
 * @author xxm
 * @date 2021/7/5
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayNotifyRecordService {
    private final PayNotifyRecordManager payNotifyRecordManager;

    /** 分页查询 */
    public PageResult<PayNotifyRecordDto> page(PageQuery PageQuery, PayNotifyRecordDto param) {
        Page<PayNotifyRecord> page = payNotifyRecordManager.page(PageQuery, param);
        return MpUtil.convert2DtoPageResult(page);
    }

    /** 根据id查询 */
    public PayNotifyRecordDto findById(Long id) {
        return payNotifyRecordManager.findById(id).map(PayNotifyRecord::toDto).orElseThrow(DataNotExistException::new);
    }
}
