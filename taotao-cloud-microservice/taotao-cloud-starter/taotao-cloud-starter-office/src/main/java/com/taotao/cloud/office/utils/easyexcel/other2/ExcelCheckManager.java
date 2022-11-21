package com.taotao.cloud.office.utils.easyexcel.other2;

import java.util.List;

public interface ExcelCheckManager<T> {

	ExcelCheckResult checkImportExcel(List<T> objects);
}
