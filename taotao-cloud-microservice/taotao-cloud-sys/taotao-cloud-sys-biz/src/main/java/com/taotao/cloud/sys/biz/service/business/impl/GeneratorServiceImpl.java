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

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ZipUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.sys.api.web.vo.generator.TableInfo;
import com.taotao.cloud.sys.biz.model.entity.config.ColumnConfig;
import com.taotao.cloud.sys.biz.model.entity.config.GenConfig;
import com.taotao.cloud.sys.biz.mapper.IColumnInfoMapper;
import com.taotao.cloud.sys.biz.service.business.IGeneratorService;
import com.taotao.cloud.sys.biz.utils.GenUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

/**
 * GeneratorServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-15 09:04:29
 */
@Service
public class GeneratorServiceImpl extends ServiceImpl<IColumnInfoMapper, ColumnConfig> implements
	IGeneratorService {

	@Override
	public Object getTables() {
		return baseMapper.selectTables();
	}

	@Override
	public Object getTables(String name, Integer page, Integer size) {
		IPage<TableInfo> pages = null;
		if (page >= 0) {
			page = page + 1;
		}
		Page<TableInfo> pageModel = new Page<>(page, size);
		pages = baseMapper.selectTablePage(pageModel, name);
		Integer totalElements = 0;
		//return PageUtil.toPage(pages.getRecords(), pages.getTotal());
		return null;
	}

	@Override
	public List<ColumnConfig> getColumns(String tableName) {
		List<ColumnConfig> columnInfos = this.list(new LambdaQueryWrapper<ColumnConfig>()
			.eq(ColumnConfig::getTableName, tableName).orderByAsc(ColumnConfig::getId));
		if (CollectionUtil.isNotEmpty(columnInfos)) {
			return columnInfos;
		} else {
			columnInfos = query(tableName);
			this.saveBatch(columnInfos);
			return columnInfos;
		}
	}

	@Override
	public List<ColumnConfig> query(String tableName) {
		List<ColumnConfig> columnInfos = new ArrayList<>();
		List<Map<String, Object>> result = baseMapper.queryByTableName(tableName);
		for (Map<String, Object> map : result) {
			columnInfos.add(
				new ColumnConfig(
					tableName,
					map.get("COLUMN_NAME").toString(),
					"NO".equals(map.get("IS_NULLABLE").toString()),
					map.get("DATA_TYPE").toString(),
					ObjectUtil.isNotNull(map.get("COLUMN_COMMENT")) ? map.get("COLUMN_COMMENT")
						.toString() : null,
					ObjectUtil.isNotNull(map.get("COLUMN_KEY")) ? map.get("COLUMN_KEY").toString()
						: null,
					ObjectUtil.isNotNull(map.get("EXTRA")) ? map.get("EXTRA").toString() : null)
			);
		}
		return columnInfos;
	}

	@Override
	public void sync(List<ColumnConfig> columnInfos, List<ColumnConfig> columnInfoList) {
		// 第一种情况，数据库类字段改变或者新增字段
		for (ColumnConfig columnInfo : columnInfoList) {
			// 根据字段名称查找
			List<ColumnConfig> columns = new ArrayList<>(columnInfos.stream()
				.filter(c -> c.getColumnName().equals(columnInfo.getColumnName()))
				.collect(Collectors.toList()));
			// 如果能找到，就修改部分可能被字段
			if (CollectionUtil.isNotEmpty(columns)) {
				ColumnConfig column = columns.get(0);
				column.setColumnType(columnInfo.getColumnType());
				column.setExtra(columnInfo.getExtra());
				column.setKeyType(columnInfo.getKeyType());
				if (StringUtils.isBlank(column.getRemark())) {
					column.setRemark(columnInfo.getRemark());
				}
				this.saveOrUpdate(column);
			} else {
				// 如果找不到，则保存新字段信息
				this.save(columnInfo);
			}
		}
		// 第二种情况，数据库字段删除了
		for (ColumnConfig columnInfo : columnInfos) {
			// 根据字段名称查找
			List<ColumnConfig> columns = new ArrayList<ColumnConfig>(columnInfoList.stream()
				.filter(c -> c.getColumnName().equals(columnInfo.getColumnName()))
				.collect(Collectors.toList()));
			// 如果找不到，就代表字段被删除了，则需要删除该字段
			if (CollectionUtil.isEmpty(columns)) {
				this.removeById(columnInfo.getId());
			}
		}
	}

	@Override
	public void save(List<ColumnConfig> columnInfos) {
		this.saveOrUpdateBatch(columnInfos);
	}

	@Override
	public void generator(GenConfig genConfig, List<ColumnConfig> columns) {
		if (genConfig.getId() == null) {
			throw new BusinessException("请先配置生成器");
		}
		try {
			GenUtil.generatorCode(columns, genConfig);
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException("生成失败，请手动处理已生成的文件");
		}
	}

	@Override
	public List<Map<String, Object>> preview(GenConfig genConfig, List<ColumnConfig> columns) {
		if (genConfig.getId() == null) {
			throw new BusinessException("请先配置生成器");
		}
		return GenUtil.preview(columns, genConfig);
	}

	@Override
	public void download(GenConfig genConfig, List<ColumnConfig> columns,
		HttpServletRequest request, HttpServletResponse response) {
		if (genConfig.getId() == null) {
			throw new BusinessException("请先配置生成器");
		}
		try {
			File file = new File(GenUtil.download(columns, genConfig));
			String zipPath = file.getPath() + ".zip";
			ZipUtil.zip(file.getPath(), zipPath);
			//FileUtil.downloadFile(request, response, new File(zipPath), true);
		} catch (IOException e) {
			throw new BusinessException("打包失败");
		}
	}
}
