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
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import com.taotao.cloud.web.base.request.PageParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 导入导出
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/25 08:23
 */
public interface PoiController<Entity, PageQuery> extends PageController<Entity, PageQuery> {

	/**
	 * 获取实体的类型
	 *
	 * @return 实体的类型
	 */
	default Class<?> getExcelClass() {
		return getEntityClass();
	}


	/**
	 * 导出Excel
	 *
	 * @param params   参数
	 * @param request  请求
	 * @param response 响应
	 */
	@Operation(summary = "导出Excel", description = "导出Excel", method = CommonConstant.PUT, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestMapping(value = "/export", method = RequestMethod.POST, produces = "application/octet-stream")
	@RequestOperateLog(
		"'导出Excel:'.concat(#params.extra[" + NormalExcelConstants.FILE_NAME + "]?:'')")
	@PreAuthorize("hasAnyPermission('{}export')")
	default void exportExcel(@RequestBody @Validated PageParams<PageQuery> params,
		HttpServletRequest request, HttpServletResponse response) {
		ExportParams exportParams = getExportParams(params);

		List<?> list = findExportList(params);

		Map<String, Object> map = new HashMap<>(7);
		map.put(NormalExcelConstants.DATA_LIST, list);
		map.put(NormalExcelConstants.CLASS, getExcelClass());
		map.put(NormalExcelConstants.PARAMS, exportParams);
		Object fileName = params.getExtra().getOrDefault(NormalExcelConstants.FILE_NAME, "临时文件");
		map.put(NormalExcelConstants.FILE_NAME, fileName);
		PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
	}


	/**
	 * 查询待导出的数据， 子类可以重写
	 *
	 * @param params params
	 * @return java.util.List<?>
	 * @author tangyh
	 * @date 2021/5/23 10:25 下午
	 * @create [2021/5/23 10:25 下午 ] [tangyh] [初始创建]
	 * @update [2021/5/23 10:25 下午 ] [tangyh] [变更描述]
	 */
	default List<?> findExportList(PageParams<PageQuery> params) {
		params.setSize(
			params.getSize() == -1 ? Convert.toLong(Integer.MAX_VALUE) : params.getSize());
		IPage<Entity> page = query(params);
		return BeanUtil.copyToList(page.getRecords(), getExcelClass());
	}

	/**
	 * 预览Excel
	 *
	 * @param params 预览参数
	 * @return 预览html
	 */
	@Operation(summary = "预览Excel", description = "预览Excel", method = CommonConstant.PUT, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestMapping(value = "/preview", method = RequestMethod.POST)
	@RequestOperateLog("'预览Excel:' + (#params.extra[" + NormalExcelConstants.FILE_NAME + "]?:'')")
	@PreAuthorize("hasAnyPermission('{}export')")
	default Result<String> preview(@RequestBody @Validated PageParams<PageQuery> params) {
		ExportParams exportParams = getExportParams(params);
		List<?> list = findExportList(params);
		Workbook workbook = ExcelExportUtil.exportExcel(exportParams, getExcelClass(), list);
		return success(ExcelXorHtmlUtil.excelToHtml(new ExcelToHtmlParams(workbook)));
	}

	/**
	 * 使用自动生成的实体+注解方式导入 对RemoteData 类型的字段不支持， 建议自建实体使用
	 *
	 * @param simpleFile 上传文件
	 * @param request    请求
	 * @param response   响应
	 * @return 是否导入成功
	 * @throws Exception 异常
	 */
	@Operation(summary = "导入Excel", description = "导入Excel", method = CommonConstant.PUT, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@PostMapping(value = "/import")
	@RequestOperateLog(value = "'导入Excel:' + #simpleFile?.originalFilename", request = false)
	@PreAuthorize("hasAnyPermission('{}import')")
	default Result<Boolean> importExcel(@RequestParam(value = "file") MultipartFile simpleFile,
		HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		ImportParams params = new ImportParams();

		params.setTitleRows(StrUtil.isEmpty(request.getParameter("titleRows")) ? 0
			: Convert.toInt(request.getParameter("titleRows")));
		params.setHeadRows(StrUtil.isEmpty(request.getParameter("headRows")) ? 1
			: Convert.toInt(request.getParameter("headRows")));
		List<Map<String, String>> list = ExcelImportUtil.importExcel(simpleFile.getInputStream(),
			Map.class, params);

		if (list != null && !list.isEmpty()) {
			return handlerImport(list);
		}
		return validFail("导入Excel无有效数据！");
	}

	/**
	 * 转换后保存
	 *
	 * @param list 集合
	 * @return 是否成功
	 */
	default Result<Boolean> handlerImport(List<Map<String, String>> list) {
		return Result.success(true);
	}

	/**
	 * 构建导出参数 子类可以重写
	 *
	 * @param params 分页参数
	 * @return 导出参数
	 */
	default ExportParams getExportParams(PageParams<PageQuery> params) {
		Object title = params.getExtra().get("title");
		Object type = params.getExtra().getOrDefault("type", ExcelType.XSSF.name());
		Object sheetName = params.getExtra().getOrDefault("sheetName", "SheetName");

		ExcelType excelType = ExcelType.XSSF.name().equals(type) ? ExcelType.XSSF : ExcelType.HSSF;
		ExportParams ep = new ExportParams(title == null ? null : String.valueOf(title),
			sheetName.toString(), excelType);
		enhanceExportParams(ep);
		return ep;
	}

	/**
	 * 子类增强ExportParams
	 *
	 * @param ep ep
	 * @author tangyh
	 * @date 2021/5/23 10:27 下午
	 * @create [2021/5/23 10:27 下午 ] [tangyh] [初始创建]
	 */
	default void enhanceExportParams(ExportParams ep) {
	}
}
