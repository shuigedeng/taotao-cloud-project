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
import com.taotao.cloud.sys.biz.model.dto.app.AppDTO;
import com.taotao.cloud.sys.biz.model.dto.app.AppPageDTO;
import com.taotao.cloud.sys.biz.mapper.AppMapper;
import com.taotao.cloud.sys.biz.model.convert.AppConvert;
import com.taotao.cloud.sys.biz.model.entity.app.App;
import com.taotao.boot.web.annotation.Manager;
import com.taotao.boot.webagg.manager.BaseManager;
import lombok.RequiredArgsConstructor;


import java.util.List;
import java.util.Objects;

/**
 * Manager 层：通用业务处理层，它有如下特征： 对第三方平台封装的层，预处理返回结果及转化异常信息，适配上层接口； 对 Service 层通用能力的下沉，如缓存方案、中间件通用处理； 与
 * DAO 层交互，对多个 DAO 的组合复用。（来源Ailibaba JAVA 开发手册）
 *
 * <p>1 .复杂业务，service提供数据给Manager层，负责业务编排，然后把事务下沉到Manager层，Manager层不允许相互调用，不会出现事务嵌套。
 *
 * <p>2.专注于不带业务sql语言，也可以在manager层进行通用业务的dao层封装。
 *
 * <p>3.避免复杂的join查询，数据库压力比java大很多，所以要严格控制好sql，所以可以在manager层 进行拆分，比如复杂查询
 *
 * <p>
 *
 * <p>4.简单的业务使用，可以不使用Manager层。（此段话借鉴一位前辈）
 *
 * <p>
 */
@Manager
@RequiredArgsConstructor
public class AppManager extends BaseManager {

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
