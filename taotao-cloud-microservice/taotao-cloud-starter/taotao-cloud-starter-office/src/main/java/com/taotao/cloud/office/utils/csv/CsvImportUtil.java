package com.taotao.cloud.office.utils.csv;


import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.office.util.constant.ImportConstant;
import com.taotao.cloud.office.util.refactor.ThrowingConsumer;
import com.taotao.cloud.office.util.valid.ImportValid;
import com.univocity.parsers.common.DataProcessingException;
import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.RetryableErrorHandler;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import java.io.InputStream;
import java.util.List;

public class CsvImportUtil<T> {

	/**
	 * @param inputStream
	 * @param errorList
	 * @param rowDto
	 * @param rowAction
	 * @param <T>
	 */
	public static <T extends Object> void importCsvWithString(InputStream inputStream,
		List<String> errorList, Class rowDto, ThrowingConsumer<List<String[]>> rowAction) {
		// 定义bean解析者：用于将csv中数据绑定到实体属性中，然后存储带list集合上
		RowListProcessor rowListProcessor = new RowListProcessor();
		CsvParserSettings setting = getDefaultSetting(errorList);
		setting.setProcessor(rowListProcessor);
		// 创建csv文件解析
		CsvParser csvParser = new CsvParser(setting);
		csvParser.parse(inputStream);
		// 获取数据映射后的集合
		List<String[]> rowDataList = rowListProcessor.getRows();
		// 执行数据持久化
		persistentStringDataToDb(rowDataList, rowAction);
	}

	/**
	 * 将数据持久化到数据库中 具体数据落库的业务逻辑方法：此处的逻辑是将数据从csv中读取出来后，然后进行自己的业务处理，最后进行落库操作
	 * 不懂的可以参考：UserServiceImpl下的uploadUserListWithCsv方法案例
	 *
	 * @param data
	 * @param persistentActionMethod
	 */
	private static <T> void persistentStringDataToDb(List<String[]> data,
		ThrowingConsumer<List<String[]>> persistentActionMethod) {
		// 对数据分组，批量插入
		List<List<String[]>> dataList = ListUtil.split(data, ImportConstant.MAX_INSERT_COUNT);
		dataList.stream().forEach(persistentActionMethod);
	}

	/**
	 * 使用实体bean接收csv数据文件并进行数据落盘
	 *
	 * @param inputStream
	 * @param errorList
	 * @param rowDtoClass
	 * @param rowAction
	 * @param <T>
	 */
	public static <T> void importCsvWithBean(InputStream inputStream, List<String> errorList,
		Class rowDtoClass, ThrowingConsumer<List<T>> rowAction) {
		// 定义bean解析者：用于将csv中数据绑定到实体属性中，然后存储带list集合上
		BeanListProcessor<T> rowProcessor = new BeanListProcessor<>(rowDtoClass);
		CsvParserSettings setting = getDefaultSetting(errorList);
		setting.setProcessor(rowProcessor);
		// 创建csv文件解析
		CsvParser csvParser = new CsvParser(setting);
		csvParser.parse(inputStream);
		// 获取数据映射后的集合
		List<T> dataList = rowProcessor.getBeans();
		// 校验必填字段
		for (T row : dataList) {
			// 校验导入字段
			ImportValid.validRequireField(row, errorList);
		}
		// 执行数据持久化
		persistentBeanDataToDb(dataList, rowAction);
	}

	/**
	 * 将数据持久化到数据库中 具体数据落库的业务逻辑方法：此处的逻辑是将数据从csv中读取出来后，然后进行自己的业务处理，最后进行落库操作
	 * 不懂的可以参考：UserServiceImpl下的uploadUserListWithCsv方法案例
	 *
	 * @param data
	 * @param persistentActionMethod
	 */
	private static <T> void persistentBeanDataToDb(List<T> data,
		ThrowingConsumer<List<T>> persistentActionMethod) {
		// 对数据分组，批量插入
		List<List<T>> dataList = ListUtil.split(data, ImportConstant.MAX_INSERT_COUNT);
		dataList.stream().forEach(persistentActionMethod);
	}

	/**
	 * 获取导入默认setting对象
	 *
	 * @param errorList
	 * @return
	 */
	private static CsvParserSettings getDefaultSetting(List<String> errorList) {
		CsvParserSettings settings = new CsvParserSettings();
		// 配置行分隔符
		settings.getFormat().setLineSeparator(StrUtil.LF);
		// 配置自动检查行分隔符序列
		settings.setLineSeparatorDetectionEnabled(Boolean.TRUE);
		// 设置将文件第一行解析为：标题
		settings.setHeaderExtractionEnabled(true);
		// 处理转换中出现的问题
		settings.setProcessorErrorHandler(new RetryableErrorHandler<ParsingContext>() {
			@Override
			public void handleError(DataProcessingException error, Object[] inputRow,
				ParsingContext context) {
				String errorLog =
					"row error details: column '" + error.getColumnName() + "' (index "
						+ error.getColumnIndex() + ") has value '"
						+ inputRow[error.getColumnIndex()] + " transfer error";
				errorList.add(errorLog);
			}
		});
		return settings;
	}
}
