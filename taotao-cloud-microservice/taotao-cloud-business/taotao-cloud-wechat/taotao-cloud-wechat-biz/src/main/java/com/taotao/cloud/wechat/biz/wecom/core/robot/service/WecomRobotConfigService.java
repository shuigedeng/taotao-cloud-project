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

package com.taotao.cloud.wechat.biz.wecom.core.robot.service;

import cn.bootx.common.core.exception.DataNotExistException;
import cn.bootx.common.core.rest.PageResult;
import cn.bootx.common.core.rest.param.PageQuery;
import cn.bootx.common.core.util.ResultConvertUtil;
import cn.bootx.common.mybatisplus.util.MpUtil;
import cn.bootx.starter.wecom.core.robot.dao.WecomRobotConfigManager;
import cn.bootx.starter.wecom.core.robot.entity.WecomRobotConfig;
import cn.bootx.starter.wecom.dto.robot.WecomRobotConfigDto;
import cn.bootx.starter.wecom.param.robot.WecomRobotConfigParam;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 企业微信机器人配置
 *
 * @author bootx
 * @since 2022-07-23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WecomRobotConfigService {
    private final WecomRobotConfigManager robotConfigManager;

    /** 添加 */
    public void add(WecomRobotConfigParam param) {
        WecomRobotConfig wecomRobotConfig = WecomRobotConfig.init(param);
        robotConfigManager.save(wecomRobotConfig);
    }

    /** 修改 */
    public void update(WecomRobotConfigParam param) {
        WecomRobotConfig wecomRobotConfig =
                robotConfigManager.findById(param.getId()).orElseThrow(DataNotExistException::new);

        BeanUtil.copyProperties(param, wecomRobotConfig, CopyOptions.create().ignoreNullValue());
        robotConfigManager.updateById(wecomRobotConfig);
    }

    /** 分页 */
    public PageResult<WecomRobotConfigDto> page(PageQuery PageQuery, WecomRobotConfigParam wecomRobotConfigParam) {
        return MpUtil.convert2DtoPageResult(robotConfigManager.page(PageQuery, wecomRobotConfigParam));
    }

    /** 获取单条 */
    public WecomRobotConfigDto findById(Long id) {
        return robotConfigManager.findById(id).map(WecomRobotConfig::toDto).orElseThrow(DataNotExistException::new);
    }

    /** 获取全部 */
    public List<WecomRobotConfigDto> findAll() {
        return ResultConvertUtil.dtoListConvert(robotConfigManager.findAll());
    }

    /** 删除 */
    public void delete(Long id) {
        robotConfigManager.deleteById(id);
    }

    /** 编码是否已经存在 */
    public boolean existsByCode(String code) {
        return robotConfigManager.existsByCode(code);
    }

    /** 编码是否已经存在(不包含自身) */
    public boolean existsByCode(String code, Long id) {
        return robotConfigManager.existsByCode(code, id);
    }
}
