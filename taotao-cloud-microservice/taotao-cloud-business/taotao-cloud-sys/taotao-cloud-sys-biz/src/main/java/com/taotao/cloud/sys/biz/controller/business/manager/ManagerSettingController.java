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

package com.taotao.cloud.sys.biz.controller.business.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.common.utils.bean.BeanUtils;
import com.taotao.boot.security.spring.annotation.NotAuth;
import com.taotao.cloud.sys.biz.model.vo.setting.SettingVO;
import com.taotao.cloud.sys.biz.model.entity.setting.Setting;
import com.taotao.cloud.sys.biz.service.business.ISettingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * CronController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-02 15:48:47
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "工具管理端-配置管理API", description = "工具管理端-配置管理API")
@RequestMapping("/sys/tools/setting")
public class ManagerSettingController {

    private final ISettingService settingService;

    @NotAuth
    public Result<SettingVO> getByKey(@RequestParam String key) {
        Setting setting = settingService.get(key);
        return Result.success(BeanUtils.copy(setting, SettingVO.class));
    }

    @NotAuth
    @GetMapping("/all")
    public Result<List<Setting>> getAll() {
        List<Setting> list = settingService.list();

        List<Setting> settings = settingService.im().selectList(new QueryWrapper<>());
        List<Setting> all = settingService.cr().findAll();
        List<Setting> all1 = settingService.ir().findAll();
        return Result.success(list);
    }

    @NotAuth
    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody Setting setting) {

        settingService.im().insert(setting);
        settingService.cr().save(setting);
        settingService.ir().save(setting);

        return Result.success(true);
    }
}
