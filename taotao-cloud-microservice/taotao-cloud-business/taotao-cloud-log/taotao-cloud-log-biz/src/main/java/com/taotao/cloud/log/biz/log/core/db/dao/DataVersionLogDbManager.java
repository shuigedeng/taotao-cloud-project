package com.taotao.cloud.log.biz.log.core.db.dao;

import cn.bootx.common.core.rest.param.PageParam;
import cn.bootx.common.mybatisplus.impl.BaseManager;
import cn.bootx.common.mybatisplus.util.MpUtil;
import cn.bootx.starter.audit.log.core.db.entity.DataVersionLogDb;
import cn.bootx.starter.audit.log.param.DataVersionLogParam;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
*
* @author xxm
* @date 2022/1/10
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
     * @param pageParam
     * @param param
     * @return
     */
    public Page<DataVersionLogDb> page(PageParam pageParam, DataVersionLogParam param) {
        Page<DataVersionLogDb> mpPage = MpUtil.getMpPage(pageParam, DataVersionLogDb.class);
        return lambdaQuery()
                .orderByDesc(DataVersionLogDb::getId)
                .like(StrUtil.isNotBlank(param.getDataName()), DataVersionLogDb::getDataName,param.getDataName())
                .like(StrUtil.isNotBlank(param.getTableName()), DataVersionLogDb::getTableName,param.getTableName())
                .like(StrUtil.isNotBlank(param.getDataId()), DataVersionLogDb::getDataId,param.getDataId())
                .eq(Objects.nonNull(param.getVersion()), DataVersionLogDb::getVersion,param.getVersion())
                .page(mpPage);
    }
}
