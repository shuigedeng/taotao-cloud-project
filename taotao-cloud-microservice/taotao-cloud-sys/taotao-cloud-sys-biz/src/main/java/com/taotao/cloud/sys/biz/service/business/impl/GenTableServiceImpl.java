package com.taotao.cloud.sys.biz.service.business.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.common.utils.io.FileUtils;
import com.taotao.cloud.common.utils.io.StreamUtils;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.sys.api.constant.GenConstants;
import com.taotao.cloud.sys.biz.mapper.IGenTableColumnMapper;
import com.taotao.cloud.sys.biz.mapper.IGenTableMapper;
import com.taotao.cloud.sys.biz.model.entity.gen.GenTable;
import com.taotao.cloud.sys.biz.model.entity.gen.GenTableColumn;
import com.taotao.cloud.sys.biz.repository.cls.GenTableRepository;
import com.taotao.cloud.sys.biz.repository.inf.IGenTableRepository;
import com.taotao.cloud.sys.biz.service.business.IGenTableService;
import com.taotao.cloud.sys.biz.utils.GenUtils;
import com.taotao.cloud.sys.biz.utils.VelocityInitializer;
import com.taotao.cloud.sys.biz.utils.VelocityUtils;
import com.taotao.cloud.web.base.service.impl.BaseSuperServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 业务 服务层实现
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class GenTableServiceImpl extends
	BaseSuperServiceImpl<IGenTableMapper, GenTable, GenTableRepository, IGenTableRepository, Long>
	implements IGenTableService {

	private final IGenTableColumnMapper genTableColumnMapper;

	@Override
	public List<GenTableColumn> selectGenTableColumnListByTableId(Long tableId) {
		return genTableColumnMapper.selectList(new LambdaQueryWrapper<GenTableColumn>()
			.eq(GenTableColumn::getTableId, tableId)
			.orderByAsc(GenTableColumn::getSort));
	}

	@Override
	public GenTable selectGenTableById(Long id) {
		GenTable genTable = baseMapper.selectGenTableById(id);
		setTableFromOptions(genTable);
		return genTable;
	}

	@Override
	public IPage<GenTable> selectPageGenTableList(GenTable genTable, PageQuery pageQuery) {
		return baseMapper.selectPage(pageQuery.buildMpPage(), this.buildGenTableQueryWrapper(genTable));
	}

	@Override
	public List<GenTable> selectGenTableList(GenTable genTable) {
		return baseMapper.selectList(this.buildGenTableQueryWrapper(genTable));
	}

	private QueryWrapper<GenTable> buildGenTableQueryWrapper(GenTable genTable) {
		Map<String, Object> params = genTable.getParams();
		QueryWrapper<GenTable> wrapper = Wrappers.query();
		wrapper.like(StringUtils.isNotBlank(genTable.getTableName()), "lower(table_name)", org.apache.commons.lang3.StringUtils.lowerCase(genTable.getTableName()))
			.like(StringUtils.isNotBlank(genTable.getTableComment()), "lower(table_comment)", org.apache.commons.lang3.StringUtils.lowerCase(genTable.getTableComment()))
			.between(params.get("beginTime") != null && params.get("endTime") != null,
				"create_time", params.get("beginTime"), params.get("endTime"));
		return wrapper;
	}


	@Override
	public IPage<GenTable> selectPageDbTableList(GenTable genTable, PageQuery pageQuery) {
		return baseMapper.selectPageDbTableList(pageQuery.buildMpPage(), this.buildDbTableQueryWrapper(genTable));
	}

	@Override
	public List<GenTable> selectDbTableList(GenTable genTable) {
		return baseMapper.selectDbTableList(this.buildDbTableQueryWrapper(genTable));
	}

	private Wrapper<Object> buildDbTableQueryWrapper(GenTable genTable) {
		Map<String, Object> params = genTable.getParams();
		QueryWrapper<Object> wrapper = Wrappers.query();
		wrapper.apply("table_schema = (select database())")
			.notLike("table_name", "xxl_job_")
			.notLike("table_name", "gen_")
			.notInSql("table_name", "select table_name from gen_table")
			.like(StringUtils.isNotBlank(genTable.getTableName()), "lower(table_name)", org.apache.commons.lang3.StringUtils.lowerCase(genTable.getTableName()))
			.like(StringUtils.isNotBlank(genTable.getTableComment()), "lower(table_comment)", org.apache.commons.lang3.StringUtils.lowerCase(genTable.getTableComment()))
			.between(params.get("beginTime") != null && params.get("endTime") != null,
				"create_time", params.get("beginTime"), params.get("endTime"))
			.orderByDesc("create_time");
		return wrapper;
	}

	@Override
	public List<GenTable> selectDbTableListByNames(String[] tableNames) {
		return baseMapper.selectDbTableListByNames(tableNames);
	}

	@Override
	public List<GenTable> selectGenTableAll() {
		return baseMapper.selectGenTableAll();
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateGenTable(GenTable genTable) {
		String options = JsonUtils.toJSONString(genTable.getParams());
		genTable.setOptions(options);
		int row = baseMapper.updateById(genTable);
		if (row > 0) {
			for (GenTableColumn cenTableColumn : genTable.getColumns()) {
				genTableColumnMapper.updateById(cenTableColumn);
			}
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void deleteGenTableByIds(Long[] tableIds) {
		List<Long> ids = Arrays.asList(tableIds);
		baseMapper.deleteBatchIds(ids);
		genTableColumnMapper.delete(new LambdaQueryWrapper<GenTableColumn>().in(GenTableColumn::getTableId, ids));
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void importGenTable(List<GenTable> tableList) {
		Long userId = SecurityUtils.getCurrentUser().getUserId();
		try {
			for (GenTable table : tableList) {
				String tableName = table.getTableName();
				GenUtils.initTable(table, userId);
				int row = baseMapper.insert(table);
				if (row > 0) {
					// 保存列信息
					List<GenTableColumn> genTableColumns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
					List<GenTableColumn> saveColumns = new ArrayList<>();
					for (GenTableColumn column : genTableColumns) {
						GenUtils.initColumnField(column, table);
						saveColumns.add(column);
					}
					if (CollUtil.isNotEmpty(saveColumns)) {
						int i = genTableColumnMapper.insertBatch(saveColumns);
						LogUtils.info("importGenTable success {}", i);
					}
				}
			}
		} catch (Exception e) {
			throw new BusinessException("导入失败：" + e.getMessage());
		}
	}

	@Override
	public Map<String, String> previewCode(Long tableId) {
		Map<String, String> dataMap = new LinkedHashMap<>();
		// 查询表信息
		GenTable table = baseMapper.selectGenTableById(tableId);
		Snowflake snowflake = IdUtil.getSnowflake();
		List<Long> menuIds = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			menuIds.add(snowflake.nextId());
		}
		table.setMenuIds(menuIds);
		// 设置主子表信息
		setSubTable(table);
		// 设置主键列信息
		setPkColumn(table);
		VelocityInitializer.initVelocity();

		VelocityContext context = VelocityUtils.prepareContext(table);

		// 获取模板列表
		List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
		for (String template : templates) {
			// 渲染模板
			StringWriter sw = new StringWriter();
			Template tpl = Velocity.getTemplate(template, CommonConstant.UTF8);
			tpl.merge(context, sw);
			dataMap.put(template, sw.toString());
		}
		return dataMap;
	}

	@Override
	public byte[] downloadCode(String tableName) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);
		generatorCode(tableName, zip);
		IoUtil.close(zip);
		return outputStream.toByteArray();
	}

	@Override
	public void generatorCode(String tableName) {
		// 查询表信息
		GenTable table = baseMapper.selectGenTableByName(tableName);
		Snowflake snowflake = IdUtil.getSnowflake();
		List<Long> menuIds = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			menuIds.add(snowflake.nextId());
		}
		table.setMenuIds(menuIds);
		// 设置主子表信息
		setSubTable(table);
		// 设置主键列信息
		setPkColumn(table);

		VelocityInitializer.initVelocity();

		VelocityContext context = VelocityUtils.prepareContext(table);

		// 获取模板列表
		List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
		for (String template : templates) {
			if (!StringUtils.containsAny(template, "sql.vm", "api.js.vm", "index.vue.vm", "index-tree.vue.vm")) {
				// 渲染模板
				StringWriter sw = new StringWriter();
				Template tpl = Velocity.getTemplate(template, CommonConstant.UTF8);
				tpl.merge(context, sw);
				try {
					String path = getGenPath(table, template);
					FileUtils.write(sw.toString(), path);
				} catch (Exception e) {
					throw new BusinessException("渲染模板失败，表名：" + table.getTableName());
				}
			}
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void synchDb(String tableName) {
		GenTable table = baseMapper.selectGenTableByName(tableName);
		List<GenTableColumn> tableColumns = table.getColumns();
		Map<String, GenTableColumn> tableColumnMap = StreamUtils.toIdentityMap(tableColumns, GenTableColumn::getColumnName);

		List<GenTableColumn> dbTableColumns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
		if (CollUtil.isEmpty(dbTableColumns)) {
			throw new BusinessException("同步数据失败，原表结构不存在");
		}
		List<String> dbTableColumnNames = StreamUtils.toList(dbTableColumns, GenTableColumn::getColumnName);

		List<GenTableColumn> saveColumns = new ArrayList<>();
		dbTableColumns.forEach(column -> {
			GenUtils.initColumnField(column, table);
			if (tableColumnMap.containsKey(column.getColumnName())) {
				GenTableColumn prevColumn = tableColumnMap.get(column.getColumnName());
				column.setId(prevColumn.getId());
				if (column.isList()) {
					// 如果是列表，继续保留查询方式/字典类型选项
					column.setDictType(prevColumn.getDictType());
					column.setQueryType(prevColumn.getQueryType());
				}
				if (StringUtils.isNotEmpty(prevColumn.getIsRequired()) && !column.isPk()
					&& (column.isInsert() || column.isEdit())
					&& ((column.isUsableColumn()) || (!column.isSuperColumn()))) {
					// 如果是(新增/修改&非主键/非忽略及父属性)，继续保留必填/显示类型选项
					column.setIsRequired(prevColumn.getIsRequired());
					column.setHtmlType(prevColumn.getHtmlType());
				}
				genTableColumnMapper.updateById(column);
			} else {
				genTableColumnMapper.insert(column);
			}
		});
		if (CollUtil.isNotEmpty(saveColumns)) {
			int i = genTableColumnMapper.insertBatch(saveColumns);
			LogUtils.info("importGenTable success {}", i);
		}

		List<GenTableColumn> delColumns = StreamUtils.filter(tableColumns, column -> !dbTableColumnNames.contains(column.getColumnName()));
		if (CollUtil.isNotEmpty(delColumns)) {
			List<Long> ids = StreamUtils.toList(delColumns, GenTableColumn::getId);
			genTableColumnMapper.deleteBatchIds(ids);
		}
	}

	@Override
	public byte[] downloadCode(String[] tableNames) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);
		for (String tableName : tableNames) {
			generatorCode(tableName, zip);
		}
		IoUtil.close(zip);
		return outputStream.toByteArray();
	}

	/**
	 * 查询表信息并生成代码
	 */
	private void generatorCode(String tableName, ZipOutputStream zip) {
		// 查询表信息
		GenTable table = baseMapper.selectGenTableByName(tableName);
		Snowflake snowflake = IdUtil.getSnowflake();
		List<Long> menuIds = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			menuIds.add(snowflake.nextId());
		}
		table.setMenuIds(menuIds);
		// 设置主子表信息
		setSubTable(table);
		// 设置主键列信息
		setPkColumn(table);

		VelocityInitializer.initVelocity();

		VelocityContext context = VelocityUtils.prepareContext(table);

		// 获取模板列表
		List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
		for (String template : templates) {
			// 渲染模板
			StringWriter sw = new StringWriter();
			Template tpl = Velocity.getTemplate(template, CommonConstant.UTF8);
			tpl.merge(context, sw);
			try {
				// 添加到zip
				zip.putNextEntry(new ZipEntry(VelocityUtils.getFileName(template, table)));
				IoUtil.write(zip, StandardCharsets.UTF_8, false, sw.toString());
				IoUtil.close(sw);
				zip.flush();
				zip.closeEntry();
			} catch (IOException e) {
				log.error("渲染模板失败，表名：" + table.getTableName(), e);
			}
		}
	}

	@Override
	public void validateEdit(GenTable genTable) {
		if (GenConstants.TPL_TREE.equals(genTable.getTplCategory())) {
			String options = JsonUtils.toJSONString(genTable.getParams());
			Dict paramsObj = JsonUtils.toObject(options, Dict.class);
			if (StringUtils.isEmpty(paramsObj.getStr(GenConstants.TREE_CODE))) {
				throw new BusinessException("树编码字段不能为空");
			} else if (StringUtils.isEmpty(paramsObj.getStr(GenConstants.TREE_PARENT_CODE))) {
				throw new BusinessException("树父编码字段不能为空");
			} else if (StringUtils.isEmpty(paramsObj.getStr(GenConstants.TREE_NAME))) {
				throw new BusinessException("树名称字段不能为空");
			} else if (GenConstants.TPL_SUB.equals(genTable.getTplCategory())) {
				if (StringUtils.isEmpty(genTable.getSubTableName())) {
					throw new BusinessException("关联子表的表名不能为空");
				} else if (StringUtils.isEmpty(genTable.getSubTableFkName())) {
					throw new BusinessException("子表关联的外键名不能为空");
				}
			}
		}
	}

	/**
	 * 设置主键列信息
	 *
	 * @param table 业务表信息
	 */
	public void setPkColumn(GenTable table) {
		for (GenTableColumn column : table.getColumns()) {
			if (column.isPk()) {
				table.setPkColumn(column);
				break;
			}
		}
		if (ObjectUtil.isNull(table.getPkColumn())) {
			table.setPkColumn(table.getColumns().get(0));
		}
		if (GenConstants.TPL_SUB.equals(table.getTplCategory())) {
			for (GenTableColumn column : table.getSubTable().getColumns()) {
				if (column.isPk()) {
					table.getSubTable().setPkColumn(column);
					break;
				}
			}
			if (ObjectUtil.isNull(table.getSubTable().getPkColumn())) {
				table.getSubTable().setPkColumn(table.getSubTable().getColumns().get(0));
			}
		}
	}

	/**
	 * 设置主子表信息
	 *
	 * @param table 业务表信息
	 */
	public void setSubTable(GenTable table) {
		String subTableName = table.getSubTableName();
		if (StringUtils.isNotEmpty(subTableName)) {
			table.setSubTable(baseMapper.selectGenTableByName(subTableName));
		}
	}

	/**
	 * 设置代码生成其他选项值
	 *
	 * @param genTable 设置后的生成对象
	 */
	public void setTableFromOptions(GenTable genTable) {
		Dict paramsObj = JsonUtils.toObject(genTable.getOptions(), Dict.class);
		if (ObjectUtil.isNotNull(paramsObj)) {
			String treeCode = paramsObj.getStr(GenConstants.TREE_CODE);
			String treeParentCode = paramsObj.getStr(GenConstants.TREE_PARENT_CODE);
			String treeName = paramsObj.getStr(GenConstants.TREE_NAME);
			String parentMenuId = paramsObj.getStr(GenConstants.PARENT_MENU_ID);
			String parentMenuName = paramsObj.getStr(GenConstants.PARENT_MENU_NAME);

			genTable.setTreeCode(treeCode);
			genTable.setTreeParentCode(treeParentCode);
			genTable.setTreeName(treeName);
			genTable.setParentMenuId(parentMenuId);
			genTable.setParentMenuName(parentMenuName);
		}
	}

	/**
	 * 获取代码生成地址
	 *
	 * @param table    业务表信息
	 * @param template 模板文件路径
	 * @return 生成地址
	 */
	public static String getGenPath(GenTable table, String template) {
		String genPath = table.getGenPath();
		if (StringUtils.equals(genPath, "/")) {
			return System.getProperty("user.dir") + File.separator + "src" + File.separator + VelocityUtils.getFileName(template, table);
		}
		return genPath + File.separator + VelocityUtils.getFileName(template, table);
	}
}
