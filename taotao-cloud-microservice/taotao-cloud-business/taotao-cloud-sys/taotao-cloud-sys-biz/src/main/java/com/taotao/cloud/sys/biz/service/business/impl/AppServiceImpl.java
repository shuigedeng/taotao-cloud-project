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

package com.taotao.cloud.sys.biz.service.business.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.sys.biz.model.dto.app.AppDTO;
import com.taotao.cloud.sys.biz.model.dto.app.AppPageDTO;
import com.taotao.cloud.sys.biz.manager.AppManager;
import com.taotao.cloud.sys.biz.model.convert.AppConvert;
import com.taotao.cloud.sys.biz.service.business.AppService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 系统应用表
 *
 * @author
 * @since 2022-09-12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {

    private final AppManager appManager;

    /** 添加 */
    @Override
    public Boolean addApp(AppDTO appDTO) {
        return appManager.addApp(appDTO) > 0;
    }

    /** 修改 */
    @Override
    public Boolean updateApp(AppDTO appDTO) {
        return appManager.updateAppById(appDTO) > 0;
    }

    /** 分页 */
    @Override
    public IPage<AppDTO> pageApp(AppPageDTO appPageDTO) {
        return AppConvert.INSTANCE.convertPage(appManager.pageApp(appPageDTO));
    }

    /** 获取单条 */
    @Override
    public AppDTO findById(Long id) {
        return AppConvert.INSTANCE.convert(appManager.findById(id));
    }

    /** 获取全部 */
    @Override
    public List<AppDTO> findAll() {
        return AppConvert.INSTANCE.convertList(appManager.listApp());
    }

    /** 删除 */
    @Override
    public Boolean deleteApp(Long id) {
        return appManager.deleteAppById(id) > 0;
    }
}
