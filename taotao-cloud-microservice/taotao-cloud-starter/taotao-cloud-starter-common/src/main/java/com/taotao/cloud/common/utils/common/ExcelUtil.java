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
package com.taotao.cloud.common.utils.common;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * ExcelUtil
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 16:48:42
 */
public final class ExcelUtil {

	private ExcelUtil() {
	}

	/**
	 * excel 导出
	 *
	 * @param list           数据
	 * @param title          标题
	 * @param sheetName      sheet名称
	 * @param pojoClass      pojo类型
	 * @param fileName       文件名称
	 * @param isCreateHeader 是否创建表头
	 * @param response       response
	 * @since 2021-09-02 17:16:17
	 */
	public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass,
		String fileName, boolean isCreateHeader, HttpServletResponse response) throws IOException {
		ExportParams exportParams = new ExportParams(title, sheetName, ExcelType.XSSF);
		exportParams.setCreateHeadRows(isCreateHeader);
		defaultExport(list, pojoClass, fileName, response, exportParams);
	}

	/**
	 * excel 导出
	 *
	 * @param list      数据
	 * @param title     标题
	 * @param sheetName sheet名称
	 * @param pojoClass pojo类型
	 * @param fileName  文件名称
	 * @param response  response
	 * @since 2021-09-02 17:16:05
	 */
	public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass,
		String fileName, HttpServletResponse response) throws IOException {
		defaultExport(list, pojoClass, fileName, response,
			new ExportParams(title, sheetName, ExcelType.XSSF));
	}

	/**
	 * excel 导出
	 *
	 * @param list         数据
	 * @param pojoClass    pojo类型
	 * @param fileName     文件名称
	 * @param response     response
	 * @param exportParams 导出参数
	 * @since 2021-09-02 17:15:54
	 */
	public static void exportExcel(List<?> list, Class<?> pojoClass, String fileName,
		ExportParams exportParams, HttpServletResponse response) throws IOException {
		defaultExport(list, pojoClass, fileName, response, exportParams);
	}

	/**
	 * excel 导出
	 *
	 * @param list     数据
	 * @param fileName 文件名称
	 * @param response response
	 * @since 2021-09-02 17:15:43
	 */
	public static void exportExcel(List<Map<String, Object>> list, String fileName,
		HttpServletResponse response) throws IOException {
		defaultExport(list, fileName, response);
	}

	/**
	 * 默认的 excel 导出
	 *
	 * @param list         数据
	 * @param pojoClass    pojo类型
	 * @param fileName     文件名称
	 * @param response     response
	 * @param exportParams 导出参数
	 * @since 2021-09-02 17:15:33
	 */
	private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName,
		HttpServletResponse response, ExportParams exportParams) throws IOException {
		Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
		downLoadExcel(fileName, response, workbook);
	}

	/**
	 * 默认的 excel 导出
	 *
	 * @param list     数据
	 * @param fileName 文件名称
	 * @param response response
	 * @since 2021-09-02 17:15:22
	 */
	private static void defaultExport(List<Map<String, Object>> list, String fileName,
		HttpServletResponse response) throws IOException {
		Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
		downLoadExcel(fileName, response, workbook);
	}

	/**
	 * 下载
	 *
	 * @param fileName 文件名称
	 * @param response response
	 * @param workbook excel数据
	 * @since 2021-09-02 17:15:13
	 */
	private static void downLoadExcel(String fileName, HttpServletResponse response,
		Workbook workbook) throws IOException {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setHeader("content-Type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder
				.encode(fileName + "." + ExcelTypeEnum.XLSX.getValue(),
					String.valueOf(StandardCharsets.UTF_8)));
			workbook.write(response.getOutputStream());
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}

	/**
	 * excel 导入
	 *
	 * @param filePath   excel文件路径
	 * @param titleRows  标题行
	 * @param headerRows 表头行
	 * @param pojoClass  pojo类型
	 * @return 列表对象
	 * @since 2021-09-02 17:14:57
	 */
	public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows,
		Class<T> pojoClass) throws IOException {
		if (StringUtils.isBlank(filePath)) {
			return null;
		}
		ImportParams params = new ImportParams();
		params.setTitleRows(titleRows);
		params.setHeadRows(headerRows);
		params.setNeedSave(true);
		params.setSaveUrl("/excel/");
		try {
			return ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
		} catch (NoSuchElementException e) {
			throw new IOException("模板不能为空");
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}

	/**
	 * excel 导入
	 *
	 * @param file      excel文件
	 * @param pojoClass pojo类型
	 * @return 列表对象
	 * @since 2021-09-02 17:14:44
	 */
	public static <T> List<T> importExcel(MultipartFile file, Class<T> pojoClass)
		throws IOException {
		return importExcel(file, 1, 1, pojoClass);
	}

	/**
	 * excel 导入
	 *
	 * @param file       excel文件
	 * @param titleRows  标题行
	 * @param headerRows 表头行
	 * @param pojoClass  pojo类型
	 * @return 列表对象
	 * @since 2021-09-02 17:14:28
	 */
	public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows,
		Class<T> pojoClass) throws IOException {
		return importExcel(file, titleRows, headerRows, false, pojoClass);
	}

	/**
	 * excel 导入
	 *
	 * @param file       上传的文件
	 * @param titleRows  标题行
	 * @param headerRows 表头行
	 * @param needVerfiy 是否检验excel内容
	 * @param pojoClass  pojo类型
	 * @return 列表对象
	 * @since 2021-09-02 17:14:16
	 */
	public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows,
		boolean needVerfiy, Class<T> pojoClass) throws IOException {
		if (file == null) {
			return null;
		}
		try {
			return importExcel(file.getInputStream(), titleRows, headerRows, needVerfiy, pojoClass);
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}

	/**
	 * excel 导入
	 *
	 * @param inputStream 文件输入流
	 * @param titleRows   标题行
	 * @param headerRows  表头行
	 * @param needVerfiy  是否检验excel内容
	 * @param pojoClass   pojo类型
	 * @return 列表对象
	 * @since 2021-09-02 17:13:58
	 */
	public static <T> List<T> importExcel(InputStream inputStream, Integer titleRows,
		Integer headerRows, boolean needVerfiy, Class<T> pojoClass) throws IOException {
		if (inputStream == null) {
			return null;
		}
		ImportParams params = new ImportParams();
		params.setTitleRows(titleRows);
		params.setHeadRows(headerRows);
		params.setSaveUrl("/excel/");
		params.setNeedSave(true);
		try {
			return ExcelImportUtil.importExcel(inputStream, pojoClass, params);
		} catch (NoSuchElementException e) {
			throw new IOException("excel文件不能为空");
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}

	/**
	 * Excel 类型枚举
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 17:13:39
	 */
	enum ExcelTypeEnum {
		XLS("xls"),
		XLSX("xlsx");

		private String value;

		ExcelTypeEnum(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
