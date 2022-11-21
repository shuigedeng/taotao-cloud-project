package com.taotao.cloud.office.utils.easyexcel.convert;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.taotao.cloud.office.utils.easyexcel.EasyExcelUtils;
import com.taotao.cloud.office.utils.easyexcel.annotation.ExcelDictFormat;
import java.lang.reflect.Field;
import org.apache.commons.lang3.StringUtils;

/**
 * 字典格式化转换处理
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-31 20:54:03
 */
public class ExcelDictConvert implements Converter<Object> {

	@Override
	public Class<Object> supportJavaTypeKey() {
		return Object.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return null;
	}

	@Override
	public Object convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
		GlobalConfiguration globalConfiguration) {
		ExcelDictFormat anno = getAnnotation(contentProperty.getField());
		String type = anno.dictType();
		String label = cellData.getStringValue();
		String value;
		if (StringUtils.isBlank(type)) {
			value = EasyExcelUtils.reverseByExp(label, anno.readConverterExp(), anno.separator());
		} else {
			value = "";
			//value = SpringUtils.getBean(DictService.class).getDictValue(type, label, anno.separator());
		}
		return Convert.convert(contentProperty.getField().getType(), value);
	}

	@Override
	public WriteCellData<String> convertToExcelData(Object object,
		ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
		if (ObjectUtil.isNull(object)) {
			return new WriteCellData<>("");
		}
		ExcelDictFormat anno = getAnnotation(contentProperty.getField());
		String type = anno.dictType();
		String value = Convert.toStr(object);
		String label;
		if (StringUtils.isBlank(type)) {
			label = EasyExcelUtils.convertByExp(value, anno.readConverterExp(), anno.separator());
		} else {
			label = "";
			//label = SpringUtils.getBean(DictService.class).getDictLabel(type, value, anno.separator());
		}
		return new WriteCellData<>(label);
	}

	private ExcelDictFormat getAnnotation(Field field) {
		return AnnotationUtil.getAnnotation(field, ExcelDictFormat.class);
	}
}
