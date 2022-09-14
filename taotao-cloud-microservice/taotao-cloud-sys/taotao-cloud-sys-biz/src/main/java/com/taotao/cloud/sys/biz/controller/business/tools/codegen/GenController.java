package com.taotao.cloud.sys.biz.controller.business.tools.codegen;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageQuery;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.sys.biz.model.entity.gen.GenTable;
import com.taotao.cloud.sys.biz.model.entity.gen.GenTableColumn;
import com.taotao.cloud.sys.biz.service.business.IGenTableService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成 操作处理
 */
@Validated
@RequiredArgsConstructor
@RequestMapping("/gen")
@RestController
public class GenController {

	private final IGenTableService genTableService;

	@Operation(summary = "查询代码生成列表", description = "查询代码生成列表")
	@RequestLogger("查询代码生成列表")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping("/list")
	public Result<PageResult<GenTable>> genList(GenTable genTable, PageQuery pageQuery) {
		IPage<GenTable> genTablePage = genTableService.selectPageGenTableList(genTable, pageQuery);
		return Result.success(PageResult.convertMybatisPage(genTablePage, GenTable.class));
	}

	@Operation(summary = "修改代码生成业务", description = "修改代码生成业务")
	@RequestLogger("修改代码生成业务")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/{tableId}")
	public Result<Map<String, Object>> getInfo(@PathVariable Long tableId) {
		GenTable table = genTableService.selectGenTableById(tableId);
		List<GenTable> tables = genTableService.selectGenTableAll();
		List<GenTableColumn> list = genTableService.selectGenTableColumnListByTableId(tableId);
		Map<String, Object> map = new HashMap<>();
		map.put("info", table);
		map.put("rows", list);
		map.put("tables", tables);
		return Result.success(map);
	}

	@Operation(summary = "查询数据库列表", description = "查询数据库列表")
	@RequestLogger("查询数据库列表")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping("/db/list")
	public Result<PageResult<GenTable>> dataList(GenTable genTable, PageQuery pageQuery) {
		IPage<GenTable> genTablePage = genTableService.selectPageDbTableList(genTable, pageQuery);
		return Result.success(PageResult.convertMybatisPage(genTablePage, GenTable.class));
	}

	@Operation(summary = "查询数据表字段列表", description = "查询数据表字段列表")
	@RequestLogger("查询数据表字段列表")
	@GetMapping(value = "/column/{tableId}")
	public Result<List<GenTableColumn>> columnList(Long tableId) {
		List<GenTableColumn> list = genTableService.selectGenTableColumnListByTableId(tableId);
		return Result.success(list);
	}

	@Operation(summary = "导入表结构（保存）", description = "导入表结构（保存）")
	@RequestLogger("导入表结构（保存）")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping("/importTable")
	public Result<Boolean> importTableSave(String tables) {
		String[] tableNames = Convert.toStrArray(tables);
		List<GenTable> tableList = genTableService.selectDbTableListByNames(tableNames);
		genTableService.importGenTable(tableList);
		return Result.success(true);
	}

	@Operation(summary = "修改保存代码生成业务", description = "修改保存代码生成业务")
	@RequestLogger("修改保存代码生成业务")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PutMapping
	public Result<Boolean> editSave(@Validated @RequestBody GenTable genTable) {
		genTableService.validateEdit(genTable);
		genTableService.updateGenTable(genTable);
		return Result.success(true);
	}

	@Operation(summary = "删除代码生成", description = "删除代码生成")
	@RequestLogger("删除代码生成")
	@PreAuthorize("@el.check('admin','timing:list')")
	@DeleteMapping("/{tableIds}")
	public Result<Boolean> remove(@PathVariable Long[] tableIds) {
		genTableService.deleteGenTableByIds(tableIds);
		return Result.success(true);
	}

	@Operation(summary = "预览代码", description = "预览代码")
	@RequestLogger("预览代码")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping("/preview/{tableId}")
	public Result<Map<String, String>> preview(@PathVariable("tableId") Long tableId) throws IOException {
		Map<String, String> dataMap = genTableService.previewCode(tableId);
		return Result.success(dataMap);
	}

	@Operation(summary = "生成代码（下载方式）", description = "生成代码（下载方式）")
	@RequestLogger("生成代码（下载方式）")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping("/download/{tableName}")
	public void download(HttpServletResponse response, @PathVariable("tableName") String tableName) throws IOException {
		byte[] data = genTableService.downloadCode(tableName);
		genCode(response, data);
	}

	@Operation(summary = "生成代码（自定义路径）", description = "生成代码（自定义路径）")
	@RequestLogger("生成代码（自定义路径）")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping("/genCode/{tableName}")
	public Result<Boolean> genCode(@PathVariable("tableName") String tableName) {
		genTableService.generatorCode(tableName);
		return Result.success(true);
	}


	@PreAuthorize("@el.check('admin','timing:list')")
	@Operation(summary = "同步数据库", description = "同步数据库")
	@RequestLogger("同步数据库")
	@GetMapping("/synchDb/{tableName}")
	public Result<Boolean> synchDb(@PathVariable("tableName") String tableName) {
		genTableService.synchDb(tableName);
		return Result.success(true);
	}

	@PreAuthorize("@el.check('admin','timing:list')")
	@Operation(summary = "批量生成代码", description = "批量生成代码")
	@RequestLogger("批量生成代码")
	@GetMapping("/batchGenCode")
	public void batchGenCode(HttpServletResponse response, String tables) throws IOException {
		String[] tableNames = Convert.toStrArray(tables);
		byte[] data = genTableService.downloadCode(tableNames);
		genCode(response, data);
	}

	/**
	 * 生成zip文件
	 */
	private void genCode(HttpServletResponse response, byte[] data) throws IOException {
		response.reset();
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
		response.setHeader("Content-Disposition", "attachment; filename=\"code.zip\"");
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream; charset=UTF-8");
		IoUtil.write(response.getOutputStream(), false, data);
	}
}
