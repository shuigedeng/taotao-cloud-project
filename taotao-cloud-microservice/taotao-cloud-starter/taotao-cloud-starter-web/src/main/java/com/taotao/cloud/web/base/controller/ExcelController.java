/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.web.base.controller;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.ExcelXorHtmlUtil;
import cn.afterturn.easypoi.excel.entity.ExcelToHtmlParams;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.BaseQuery;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.data.mybatis.plus.conditions.query.QueryWrap;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.web.base.entity.SuperEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * PoiController
 *
 * @param <T>        实体
 * @param <I>        id
 * @param <QueryDTO> 查询参数
 * @param <QueryVO>  查询返回参数
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:07:59
 */
public interface ExcelController<T extends SuperEntity<T, I>, I extends Serializable, QueryDTO, QueryVO> extends
	PageController<T, I, QueryDTO, QueryVO> {

	/**
	 * 通用导出Excel
	 *
	 * @param params   参数
	 * @param request  请求
	 * @param response 响应
	 * @since 2021-09-02 21:08:22
	 */
	@Operation(summary = "通用导出Excel", description = "通用导出Excel")
	@PostMapping(value = "/excel/export", produces = "application/octet-stream")
	@RequestLogger(description = "'导出Excel:'.concat([" + NormalExcelConstants.FILE_NAME + "]?:'')")
	//@PreAuthorize("@permissionVerifier.hasPermission('export')")
	default void export(
		@Parameter(description = "查询DTO", required = true)
		@RequestBody @Validated QueryDTO params,
		HttpServletRequest request, HttpServletResponse response) {
		ExportParams exportParams = getExportParams(params);

		List<T> list = findExportList(params);

		Map<String, Object> map = new HashMap<>(7);
		if (params instanceof BaseQuery baseQuery) {
			String fileName = baseQuery.execlQuery().fileName();
			map.put(NormalExcelConstants.FILE_NAME, fileName);
		}
		map.put(NormalExcelConstants.DATA_LIST, list);
		map.put(NormalExcelConstants.CLASS, getExcelClass());
		map.put(NormalExcelConstants.PARAMS, exportParams);

		PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
	}


	/**
	 * 通用预览Excel
	 *
	 * @param params 预览参数
	 * @return 预览html
	 * @since 2021-09-02 21:08:45
	 */
	@Operation(summary = "通用预览Excel", description = "通用预览Excel")
	@PostMapping(value = "/excel/preview")
	@RequestLogger(description = "'通用预览Excel:' + ([" + NormalExcelConstants.FILE_NAME + "]?:'')")
	//@PreAuthorize("@permissionVerifier.hasPermission('preview')")
	default Result<String> preview(
		@Parameter(description = "查询DTO", required = true)
		@RequestBody @Validated QueryDTO params) {
		ExportParams exportParams = getExportParams(params);
		List<T> list = findExportList(params);
		Workbook workbook = ExcelExportUtil.exportExcel(exportParams, getExcelClass(), list);
		return success(ExcelXorHtmlUtil.excelToHtml(new ExcelToHtmlParams(workbook)));
	}

	/**
	 * 通用导入Excel
	 *
	 * @param file     上传文件
	 * @param request  请求
	 * @param response 响应
	 * @return 是否导入成功
	 * @since 2021-09-02 21:09:09
	 */
	@Operation(summary = "通用导入Excel", description = "通用导入Excel")
	@PostMapping(value = "/excel/import", headers = "content-type=multipart/form-data")
	@RequestLogger(description = "通用导入Excel")
	//@PreAuthorize("@permissionVerifier.hasPermission('import')")
	default Result<Boolean> importExcel(
		@Parameter(description = "文件", required = true) @NotNull(message = "文件不能为空")
		@RequestPart("file") MultipartFile file,
		HttpServletRequest request, HttpServletResponse response) {
		ImportParams params = new ImportParams();

		params.setTitleRows(StrUtil.isEmpty(request.getParameter("titleRows")) ? 0
			: Convert.toInt(request.getParameter("titleRows")));
		params.setHeadRows(StrUtil.isEmpty(request.getParameter("headRows")) ? 1
			: Convert.toInt(request.getParameter("headRows")));

		try {
			List<Map<String, String>> list = ExcelImportUtil.importExcel(
				file.getInputStream(), Map.class, params);

			if (list != null && !list.isEmpty()) {
				return success(handlerImport(list));
			}
		} catch (Exception e) {
			LogUtil.error(e);
		}

		throw new BusinessException("导入Excel失败");
	}

	/**
	 * 转换后保存
	 *
	 * @param list 集合
	 * @return 保存结果
	 * @since 2021-09-02 21:09:35
	 */
	default Boolean handlerImport(List<Map<String, String>> list) {
		return true;
	}

	/**
	 * 构建导出参数 子类可以重写
	 *
	 * @param params 分页参数
	 * @return 分页参数对象
	 * @since 2021-09-02 21:09:45
	 */
	default ExportParams getExportParams(QueryDTO params) {
		String title = "title";
		String type = "HSSF";
		String sheetName = "sheetName";

		if (params instanceof BaseQuery baseQuery) {
			if (Objects.isNull(baseQuery.execlQuery())) {
				throw new BusinessException("execl参数不能为空");
			}
			title = baseQuery.execlQuery().title();
			type = baseQuery.execlQuery().type();
			sheetName = baseQuery.execlQuery().sheetName();
		}

		ExcelType excelType = ExcelType.XSSF.name().equals(type) ? ExcelType.XSSF : ExcelType.HSSF;
		ExportParams ep = new ExportParams(title, sheetName, excelType);
		enhanceExportParams(ep);
		return ep;
	}

	/**
	 * 子类增强ExportParams
	 *
	 * @param params 参数
	 * @since 2021-09-02 21:09:51
	 */
	default void enhanceExportParams(ExportParams params) {
	}

	/**
	 * 查询待导出的数据， 子类可以重写
	 *
	 * @param params 参数
	 * @return 数据列表
	 * @since 2021-09-02 21:08:33
	 */
	default List<T> findExportList(QueryDTO params) {
		QueryWrap<T> tQueryWrap = handlerWrapper(params);
		return service().list(tQueryWrap);
	}

	/**
	 * 获取实体的类型
	 *
	 * @return 实体类型
	 * @since 2021-09-02 21:08:15
	 */
	default Class<T> getExcelClass() {
		return getEntityClass();
	}
}
