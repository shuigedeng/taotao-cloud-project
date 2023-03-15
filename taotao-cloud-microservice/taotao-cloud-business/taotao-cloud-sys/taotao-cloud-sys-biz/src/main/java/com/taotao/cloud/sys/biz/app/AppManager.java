/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sys.biz.app;

import cn.hutool.core.util.StrUtil;
import com.art.system.api.app.dto.AppDTO;
import com.art.system.api.app.dto.AppPageDTO;
import com.art.system.core.convert.AppConvert;
import com.art.system.dao.dataobject.AppDO;
import com.art.system.dao.mysql.AppMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/22 23:55
 */
@Component
@RequiredArgsConstructor
public class AppManager {

	private final AppMapper appMapper;

	/**
	 * 分页查询appDO
	 * @param appPageDTO 分页参数
	 * @return appDO
	 */
	public Page<AppDO> pageApp(AppPageDTO appPageDTO) {
		LambdaQueryWrapper<AppDO> wrapper = Wrappers.<AppDO>lambdaQuery()
			.like(StrUtil.isNotBlank(appPageDTO.getName()), AppDO::getName, appPageDTO.getName())
			.like(StrUtil.isNotBlank(appPageDTO.getCode()), AppDO::getCode, appPageDTO.getCode())
			.eq(Objects.nonNull(appPageDTO.getId()), AppDO::getId, appPageDTO.getId())
			.orderByAsc(AppDO::getSort);

		return appMapper.selectPage(Page.of(appPageDTO.getCurrent(), appPageDTO.getSize()), wrapper);
	}

	/**
	 * 列出所有appDO
	 * @return 所有appDO
	 */
	public List<AppDO> listApp() {
		return appMapper.selectList(Wrappers.<AppDO>lambdaQuery().orderByAsc(AppDO::getSort));
	}

	/**
	 * 根据Id删除appDO
	 * @param id 主键
	 * @return 影响行数
	 */
	public Integer deleteAppById(Long id) {
		return appMapper.deleteById(id);
	}

	/**
	 * 根据id更新appDO
	 * @param appDTO appDTO
	 * @return 影响条数
	 */
	public Integer updateAppById(AppDTO appDTO) {
		return appMapper.updateById(AppConvert.INSTANCE.convert(appDTO));
	}

	/**
	 * 新增appDO
	 * @param appDTO appDTO
	 * @return 影响条数
	 */
	public Integer addApp(AppDTO appDTO) {
		return appMapper.insert(AppConvert.INSTANCE.convert(appDTO));
	}

	/**
	 * 根据id查询appDO
	 * @param id 主键
	 * @return appDO
	 */
	public AppDO findById(Long id) {
		return appMapper.selectById(id);
	}

}
