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

package com.taotao.cloud.sys.biz.supports.core.db.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.data.mybatis.mybatisplus.BaseManager;
import com.taotao.cloud.sys.biz.model.param.OperateLogParam;
import com.taotao.cloud.sys.biz.supports.core.db.entity.OperateLogDb;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

/**
 * 操作日志
 *
 * @author shuigedeng
 * @since 2021/8/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OperateLogDbManager extends BaseManager<OperateLogDbMapper, OperateLogDb> {

    public IPage<OperateLogDb> page(OperateLogParam operateLogParam) {
        return lambdaQuery()
                .like(
                        StrUtil.isNotBlank(operateLogParam.getUsername()),
                        OperateLogDb::getUsername,
                        operateLogParam.getUsername())
                .like(
                        StrUtil.isNotBlank(operateLogParam.getTitle()),
                        OperateLogDb::getTitle,
                        operateLogParam.getTitle())
                .eq(
                        Objects.nonNull(operateLogParam.getBusinessType()),
                        OperateLogDb::getBusinessType,
                        operateLogParam.getBusinessType())
                .orderByDesc(OperateLogDb::getOperateTime)
                .page(MpUtils.buildMpPage(operateLogParam));
    }
}
