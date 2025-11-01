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
import com.taotao.cloud.sys.biz.supports.core.db.entity.DataVersionLogDb;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;

/**
 * @author shuigedeng
 * @since 2022/1/10
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class DataVersionLogDbManager extends BaseManager<DataVersionLogDbMapper, DataVersionLogDb> {
    private final DataVersionLogDbMapper mapper;

    /**
     * 获取最新版本号
     */
    public int getMaxVersion(String dataName, String dataId) {
        Integer maxVersion = mapper.getMaxVersion(dataName, dataId);
        return Objects.isNull(maxVersion) ? 0 : maxVersion;
    }

    /**
     * 分页
     *
     * @param pageParam
     * @param param
     * @return
     */
    public IPage<DataVersionLogDb> page(DataVersionLogParam param) {
        return lambdaQuery()
                .orderByDesc(DataVersionLogDb::getId)
                .like(StrUtil.isNotBlank(param.getDataName()), DataVersionLogDb::getDataName, param.getDataName())
                .like(StrUtil.isNotBlank(param.getTableName()), DataVersionLogDb::getTableName, param.getTableName())
                .like(StrUtil.isNotBlank(param.getDataId()), DataVersionLogDb::getDataId, param.getDataId())
                .eq(Objects.nonNull(param.getVersion()), DataVersionLogDb::getVersion, param.getVersion())
                .page( MpUtils.buildMpPage(param));
    }
}
