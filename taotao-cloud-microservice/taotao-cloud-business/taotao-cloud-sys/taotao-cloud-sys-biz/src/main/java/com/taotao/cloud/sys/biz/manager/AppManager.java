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

package com.taotao.cloud.sys.biz.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.sys.api.model.dto.app.AppDTO;
import com.taotao.cloud.sys.api.model.dto.app.AppPageDTO;
import com.taotao.cloud.sys.biz.mapper.AppMapper;
import com.taotao.cloud.sys.biz.model.convert.AppConvert;
import com.taotao.cloud.sys.biz.model.entity.app.App;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.stereotype.Component;

/**
 * @author
 * @version 0.0.1
 * @date 2022/11/22 23:55
 */
@Component
@RequiredArgsConstructor
public class AppManager {

    private final AppMapper appMapper;

    /**
     * 分页查询appDO
     *
     * @param appPageDTO 分页参数
     * @return appDO
     */
    public Page<App> pageApp(AppPageDTO appPageDTO) {
        LambdaQueryWrapper<App> wrapper = Wrappers.<App>lambdaQuery()
                .like(StrUtil.isNotBlank(appPageDTO.getName()), App::getName, appPageDTO.getName())
                .like(StrUtil.isNotBlank(appPageDTO.getCode()), App::getCode, appPageDTO.getCode())
                .eq(Objects.nonNull(appPageDTO.getId()), App::getId, appPageDTO.getId())
                .orderByAsc(App::getSort);

        return appMapper.selectPage(Page.of(appPageDTO.getCurrentPage(), appPageDTO.getPageSize()), wrapper);
    }

    /**
     * 列出所有appDO
     *
     * @return 所有appDO
     */
    public List<App> listApp() {
        return appMapper.selectList(Wrappers.<App>lambdaQuery().orderByAsc(App::getSort));
    }

    /**
     * 根据Id删除appDO
     *
     * @param id 主键
     * @return 影响行数
     */
    public Integer deleteAppById(Long id) {
        return appMapper.deleteById(id);
    }

    /**
     * 根据id更新appDO
     *
     * @param appDTO appDTO
     * @return 影响条数
     */
    public Integer updateAppById(AppDTO appDTO) {
        return appMapper.updateById(AppConvert.INSTANCE.convert(appDTO));
    }

    /**
     * 新增appDO
     *
     * @param appDTO appDTO
     * @return 影响条数
     */
    public Integer addApp(AppDTO appDTO) {
        return appMapper.insert(AppConvert.INSTANCE.convert(appDTO));
    }

    /**
     * 根据id查询appDO
     *
     * @param id 主键
     * @return appDO
     */
    public App findById(Long id) {
        return appMapper.selectById(id);
    }
}
