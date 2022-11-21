package com.taotao.cloud.office.utils.easyexcel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.IdUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.taotao.cloud.office.utils.easyexcel.convert.ExcelBigNumberConvert;
import com.taotao.cloud.office.utils.easyexcel.core.CellMergeStrategy;
import com.taotao.cloud.office.utils.easyexcel.core.DefaultExcelListener;
import com.taotao.cloud.office.utils.easyexcel.core.ExcelListener;
import com.taotao.cloud.office.utils.easyexcel.core.ExcelResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * Excel相关处理
 *
 * @author shuigedeng
 * @version 2022.06
 * @ExcelIgnore：忽略掉该字段；
 * @ExcelProperty("用户名")：设置该列的名称为”用户名“；
 * @ColumnWidth(20)：设置表格列的宽度为20；
 * @DateTimeFormat("yyyy-MM-dd")：按照指定的格式对日期进行格式化；
 * @ExcelProperty(value = "性别", converter =
 * GenderConverter.class)：自定义内容转换器，类似枚举的实现，将“男”、“女”转换成“0”、“1”的数值。
 * @since 2022-07-31 20:58:12
 */
public class EasyExcelUtils {

	/**
	 * 同步导入(适用于小数据量)
	 *
	 * @param is 输入流
	 * @return 转换后集合
	 */
	public static <T> List<T> importExcel(InputStream is, Class<T> clazz) {
		return EasyExcel.read(is).head(clazz).autoCloseStream(false).sheet().doReadSync();
	}


	/**
	 * 使用校验监听器 异步导入 同步返回
	 *
	 * @param is         输入流
	 * @param clazz      对象类型
	 * @param isValidate 是否 Validator 检验 默认为是
	 * @return 转换后集合
	 */
	public static <T> ExcelResult<T> importExcel(InputStream is, Class<T> clazz,
		boolean isValidate) {
		DefaultExcelListener<T> listener = new DefaultExcelListener<>(isValidate);
		EasyExcel.read(is, clazz, listener).sheet().doRead();
		return listener.getExcelResult();
	}

	/**
	 * 使用自定义监听器 异步导入 自定义返回
	 *
	 * @param is       输入流
	 * @param clazz    对象类型
	 * @param listener 自定义监听器
	 * @return 转换后集合
	 */
	public static <T> ExcelResult<T> importExcel(InputStream is, Class<T> clazz,
		ExcelListener<T> listener) {
		EasyExcel.read(is, clazz, listener).sheet().doRead();
		return listener.getExcelResult();
	}

	/**
	 * 导出excel
	 *
	 * @param list      导出数据集合
	 * @param sheetName 工作表的名称
	 * @param clazz     实体类
	 * @param response  响应体
	 */
	public static <T> void exportExcel(List<T> list, String sheetName, Class<T> clazz,
		HttpServletResponse response) {
		exportExcel(list, sheetName, clazz, false, response);
	}

	/**
	 * 导出excel
	 *
	 * @param list      导出数据集合
	 * @param sheetName 工作表的名称
	 * @param clazz     实体类
	 * @param merge     是否合并单元格
	 * @param response  响应体
	 */
	public static <T> void exportExcel(List<T> list, String sheetName, Class<T> clazz,
		boolean merge, HttpServletResponse response) {
		try {
			resetResponse(sheetName, response);
			ServletOutputStream os = response.getOutputStream();
			ExcelWriterSheetBuilder builder = EasyExcel.write(os, clazz)
				.autoCloseStream(false)
				// 自动适配
				.registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
				// 大数值自动转换 防止失真
				.registerConverter(new ExcelBigNumberConvert())
				//// 注册通用格式策略
				//.registerWriteHandler(CommonCellStyleStrategy.getHorizontalCellStyleStrategy())
				//// 设置自定义格式策略
				//.registerWriteHandler(new CustomCellWriteHandler())
				.sheet(sheetName);
			if (merge) {
				// 合并处理器
				builder.registerWriteHandler(new CellMergeStrategy(list, true));
			}
			builder.doWrite(list);
		} catch (IOException e) {
			throw new RuntimeException("导出Excel异常");
		}
	}

	/**
	 * 单表多数据模板导出 模板格式为 {.属性}
	 *
	 * @param filename     文件名
	 * @param templatePath 模板路径 resource 目录下的路径包括模板文件名 例如: excel/temp.xlsx 重点: 模板文件必须放置到启动类对应的
	 *                     resource 目录下
	 * @param data         模板需要的数据
	 */
	public static void exportTemplate(List<Object> data, String filename, String templatePath,
		HttpServletResponse response) {
		try {
			resetResponse(filename, response);
			ClassPathResource templateResource = new ClassPathResource(templatePath);
			ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream())
				.withTemplate(templateResource.getStream())
				.autoCloseStream(false)
				// 大数值自动转换 防止失真
				.registerConverter(new ExcelBigNumberConvert())
				.build();
			WriteSheet writeSheet = EasyExcel.writerSheet().build();
			if (CollUtil.isEmpty(data)) {
				throw new IllegalArgumentException("数据为空");
			}
			// 单表多数据导出 模板格式为 {.属性}
			for (Object d : data) {
				excelWriter.fill(d, writeSheet);
			}
			excelWriter.finish();
		} catch (IOException e) {
			throw new RuntimeException("导出Excel异常");
		}
	}

	/**
	 * 多表多数据模板导出 模板格式为 {key.属性}
	 *
	 * @param filename     文件名
	 * @param templatePath 模板路径 resource 目录下的路径包括模板文件名 例如: excel/temp.xlsx 重点: 模板文件必须放置到启动类对应的
	 *                     resource 目录下
	 * @param data         模板需要的数据
	 */
	public static void exportTemplateMultiList(Map<String, Object> data, String filename,
		String templatePath, HttpServletResponse response) {
		try {
			resetResponse(filename, response);
			ClassPathResource templateResource = new ClassPathResource(templatePath);
			ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream())
				.withTemplate(templateResource.getStream())
				.autoCloseStream(false)
				// 大数值自动转换 防止失真
				.registerConverter(new ExcelBigNumberConvert())
				.build();
			WriteSheet writeSheet = EasyExcel.writerSheet().build();
			if (CollUtil.isEmpty(data)) {
				throw new IllegalArgumentException("数据为空");
			}
			for (Map.Entry<String, Object> map : data.entrySet()) {
				// 设置列表后续还有数据
				FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
				if (map.getValue() instanceof Collection) {
					// 多表导出必须使用 FillWrapper
					excelWriter.fill(new FillWrapper(map.getKey(), (Collection<?>) map.getValue()),
						fillConfig, writeSheet);
				} else {
					excelWriter.fill(map.getValue(), writeSheet);
				}
			}
			excelWriter.finish();
		} catch (IOException e) {
			throw new RuntimeException("导出Excel异常");
		}
	}

	/**
	 * 重置响应体
	 */
	private static void resetResponse(String sheetName, HttpServletResponse response)
		throws UnsupportedEncodingException {
		String filename = encodingFilename(sheetName);
		response.reset();
		// todo 此处需要修改
		//FileUtil.setAttachmentResponseHeader(response, filename);
		response.setContentType(
			"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
	}

	/**
	 * 解析导出值 0=男,1=女,2=未知
	 *
	 * @param propertyValue 参数值
	 * @param converterExp  翻译注解
	 * @param separator     分隔符
	 * @return 解析后值
	 */
	public static String convertByExp(String propertyValue, String converterExp, String separator) {
		StringBuilder propertyString = new StringBuilder();
		String[] convertSource = converterExp.split(",");
		for (String item : convertSource) {
			String[] itemArray = item.split("=");
			if (StringUtils.containsAny(propertyValue, separator)) {
				for (String value : propertyValue.split(separator)) {
					if (itemArray[0].equals(value)) {
						propertyString.append(itemArray[1]).append(separator);
						break;
					}
				}
			} else {
				if (itemArray[0].equals(propertyValue)) {
					return itemArray[1];
				}
			}
		}
		return StringUtils.stripEnd(propertyString.toString(), separator);
	}

	/**
	 * 反向解析值 男=0,女=1,未知=2
	 *
	 * @param propertyValue 参数值
	 * @param converterExp  翻译注解
	 * @param separator     分隔符
	 * @return 解析后值
	 */
	public static String reverseByExp(String propertyValue, String converterExp, String separator) {
		StringBuilder propertyString = new StringBuilder();
		String[] convertSource = converterExp.split(",");
		for (String item : convertSource) {
			String[] itemArray = item.split("=");
			if (StringUtils.containsAny(propertyValue, separator)) {
				for (String value : propertyValue.split(separator)) {
					if (itemArray[1].equals(value)) {
						propertyString.append(itemArray[0]).append(separator);
						break;
					}
				}
			} else {
				if (itemArray[1].equals(propertyValue)) {
					return itemArray[0];
				}
			}
		}
		return StringUtils.stripEnd(propertyString.toString(), separator);
	}

	/**
	 * 编码文件名
	 */
	public static String encodingFilename(String filename) {
		return IdUtil.fastSimpleUUID() + "_" + filename + ".xlsx";
	}

	/**
	 * 设置单元格样式(仅用于示例)
	 *
	 * @return 样式策略
	 */
	public static HorizontalCellStyleStrategy getHorizontalCellStyleStrategy() {
		// 表头策略
		WriteCellStyle headerCellStyle = new WriteCellStyle();
		// 表头水平对齐居中
		headerCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
		// 背景色
		headerCellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
		WriteFont headerFont = new WriteFont();
		headerFont.setFontHeightInPoints((short) 15);
		headerCellStyle.setWriteFont(headerFont);
		// 自动换行
		headerCellStyle.setWrapped(Boolean.FALSE);
		// 内容策略
		WriteCellStyle contentCellStyle = new WriteCellStyle();
		// 设置数据允许的数据格式,这里49代表所有可以都允许设置
		//contentCellStyle.setDataFormat((short) 49);
		// 设置背景色: 需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
		contentCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
		contentCellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		// 设置内容靠左对齐
		contentCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
		// 设置字体
		WriteFont contentFont = new WriteFont();
		contentFont.setFontHeightInPoints((short) 12);
		contentCellStyle.setWriteFont(contentFont);
		// 设置自动换行
		contentCellStyle.setWrapped(Boolean.FALSE);
		// 设置边框样式和颜色
		contentCellStyle.setBorderLeft(BorderStyle.MEDIUM);
		contentCellStyle.setBorderTop(BorderStyle.MEDIUM);
		contentCellStyle.setBorderRight(BorderStyle.MEDIUM);
		contentCellStyle.setBorderBottom(BorderStyle.MEDIUM);
		contentCellStyle.setTopBorderColor(IndexedColors.RED.getIndex());
		contentCellStyle.setBottomBorderColor(IndexedColors.GREEN.getIndex());
		contentCellStyle.setLeftBorderColor(IndexedColors.YELLOW.getIndex());
		contentCellStyle.setRightBorderColor(IndexedColors.ORANGE.getIndex());
		// 将格式加入单元格样式策略
		return new HorizontalCellStyleStrategy(headerCellStyle, contentCellStyle);
	}

}
