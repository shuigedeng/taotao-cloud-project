package com.taotao.cloud.office.utils.csv;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.office.util.constant.ExportConstant;
import com.univocity.parsers.common.processor.BeanWriterProcessor;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;

public class CsvExportUtil<T> {

	/**
	 * 导出csv文件(表头和数据都以字符串的形式)
	 *
	 * @param response
	 * @param head
	 * @param rowDataList
	 */
	public static <T> void exportCsvWithString(HttpServletResponse response, String fileName,
		List<T> head, List<List<T>> rowDataList) {
		CsvWriter writer = null;
		try {
			response.setContentType(ExportConstant.EXCEL_CONTENT_TYPE);
			response.setCharacterEncoding(ExportConstant.UTF_8);
			response.setHeader(ExportConstant.CONTENT_DISPOSITION,
				ExportConstant.ATTACHMENT_FILENAME + fileName + ExportConstant.CSV_SUFFIX);
			CsvWriterSettings setting = getDefaultWriteSetting();
			writer = new CsvWriter(response.getOutputStream(), setting);
			writer.writeHeaders(head);
			writer.writeStringRows(rowDataList);
			writer.flush();
		} catch (Exception e) {
			LogUtils.error("CsvExportUtil exportCsv in error:{}", e);
		} finally {
			if (Objects.nonNull(writer)) {
				writer.close();
			}
		}
	}

	/**
	 * 导出csv文件(表头和行都以实体的方式)
	 *
	 * @param response
	 * @param head
	 * @param rowDataList
	 */
	public static <T> void exportCsvWithBean(HttpServletResponse response, String fileName, T head,
		List<T> rowDataList) {
		CsvWriter writer = null;
		try {
			// 设置响应头格式
			response.setContentType(ExportConstant.EXCEL_CONTENT_TYPE);
			response.setCharacterEncoding(ExportConstant.UTF_8);
			response.setHeader(ExportConstant.CONTENT_DISPOSITION,
				ExportConstant.ATTACHMENT_FILENAME + fileName + ExportConstant.CSV_SUFFIX);

			// 设置导出格式
			CsvWriterSettings setting = getDefaultWriteSetting();
			// 创见bean处理器，用于处理写入数据
			BeanWriterProcessor<?> beanWriter = new BeanWriterProcessor<>(head.getClass());
			setting.setRowWriterProcessor(beanWriter);

			// 导出数据
			writer = new CsvWriter(response.getOutputStream(), setting);
			writer.processRecords(rowDataList);
			writer.flush();
		} catch (Exception e) {
			LogUtils.error("CsvExportUtil exportCsvWithBean in error:{}", e);
		} finally {
			if (Objects.nonNull(writer)) {
				writer.close();
			}
		}
	}

	/**
	 * 获取默认的CSV写入配置对象
	 *
	 * @return
	 */
	private static CsvWriterSettings getDefaultWriteSetting() {
		CsvWriterSettings settings = new CsvWriterSettings();
		/**
		 * 如果要设置值之间的间隔可以使用下面示例代码
		 * FixedWidthFields lengths = new FixedWidthFields(10, 10, 35, 10, 40);
		 * FixedWidthWriterSettings settings = new FixedWidthWriterSettings(lengths);
		 **/
		// 设置值为null时替换的值
		settings.setNullValue("");
		// 修改分隔符,默认是逗号
		//settings.getFormat().setDelimiter("");
		// 设置是否自动写入标题
		settings.setHeaderWritingEnabled(Boolean.TRUE);
		return settings;
	}

}
