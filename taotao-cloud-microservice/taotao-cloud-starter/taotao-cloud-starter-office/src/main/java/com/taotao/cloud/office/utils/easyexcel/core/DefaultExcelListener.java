package com.taotao.cloud.office.utils.easyexcel.core;

import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.validator.ValidatorUtils;
import java.util.Map;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * Excel 导入监听
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-31 21:01:29
 */
public class DefaultExcelListener<T> extends AnalysisEventListener<T> implements ExcelListener<T> {

	/**
	 * 是否Validator检验，默认为是
	 */
	private final Boolean isValidate;

	/**
	 * excel 表头数据
	 */
	private Map<Integer, String> headMap;

	/**
	 * 导入回执
	 */
	private final ExcelResult<T> excelResult;

	public DefaultExcelListener(boolean isValidate) {
		this.excelResult = new DefautExcelResult<>();
		this.isValidate = isValidate;
	}

	public DefaultExcelListener() {
		this.excelResult = new DefautExcelResult<>();
		this.isValidate = true;
	}

	/**
	 * 处理异常
	 *
	 * @param exception ExcelDataConvertException
	 * @param context   Excel 上下文
	 */
	@Override
	public void onException(Exception exception, AnalysisContext context) throws Exception {
		String errMsg = null;
		if (exception instanceof ExcelDataConvertException excelDataConvertException) {
			// 如果是某一个单元格的转换异常 能获取到具体行号
			Integer rowIndex = excelDataConvertException.getRowIndex();
			Integer columnIndex = excelDataConvertException.getColumnIndex();
			errMsg = StrUtil.format("第{}行-第{}列-表头{}: 解析异常<br/>",
				rowIndex + 1, columnIndex + 1, headMap.get(columnIndex));
			if (LogUtils.isDebugEnabled()) {
				LogUtils.error(errMsg);
			}
		}

		if (exception instanceof ConstraintViolationException constraintViolationException) {
			Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();
			String constraintViolationsMsg = StreamUtil.join(constraintViolations.stream(), ", ",
				ConstraintViolation::getMessage);
			errMsg = StrUtil.format("第{}行数据校验异常: {}",
				context.readRowHolder().getRowIndex() + 1, constraintViolationsMsg);
			if (LogUtils.isDebugEnabled()) {
				LogUtils.error(errMsg);
			}
		}

		excelResult.getErrorList().add(errMsg);
		throw new ExcelAnalysisException(errMsg);
	}

	@Override
	public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
		this.headMap = headMap;
		LogUtils.debug("解析到一条表头数据: {}", JsonUtils.toJSONString(headMap));
	}

	@Override
	public void invoke(T data, AnalysisContext context) {
		if (isValidate) {
			ValidatorUtils.validate(data);
		}
		excelResult.getList().add(data);
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		LogUtils.debug("所有数据解析完成！");
	}

	@Override
	public ExcelResult<T> getExcelResult() {
		return excelResult;
	}

}
