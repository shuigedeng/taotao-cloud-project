package com.taotao.cloud.office.utils.easyexcel.other3;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;

/**
 * EasyExcel解析动态表头及导出
 * <pre class="code">
 *     EasyExcel.write(response.getOutputStream())
 *     .head(head)
 *     .registerWriteHandler(new CellRowHeightStyleStrategy())   //设置行高的策略
 *     .registerWriteHandler(new CellStyleStrategy(Arrays.asList(0,1),new WriteCellStyle(), new WriteCellStyle()))
 *     .registerWriteHandler(new CellWidthStyleStrategy())
 *     .sheet(sheetName)
 *     .doWrite(list);
 * </pre>
 *
 * @author yupf
 * @description
 * @date 2022/9/7 18:48
 */
public class CellWidthStyleStrategy extends AbstractColumnWidthStyleStrategy {

	private Map<Integer, Map<Integer, Integer>> CACHE = new HashMap<>();

	@Override
	protected void setColumnWidth(WriteSheetHolder writeSheetHolder,
		List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex,
		Boolean isHead) {
		Map<Integer, Integer> maxColumnWidthMap = CACHE.get(writeSheetHolder.getSheetNo());
		if (maxColumnWidthMap == null) {
			maxColumnWidthMap = new HashMap<>();
			CACHE.put(writeSheetHolder.getSheetNo(), maxColumnWidthMap);
		}
		if (isHead) {
			if (relativeRowIndex.intValue() == 1) {
				Integer length = cell.getStringCellValue().getBytes().length;
				Integer maxColumnWidth = maxColumnWidthMap.get(cell.getColumnIndex());
				if (maxColumnWidth == null || length > maxColumnWidth) {
					maxColumnWidthMap.put(cell.getColumnIndex(), length);
					writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), length * 300);
				}
			}
		} else {
			Integer columnWidth = this.dataLength(cellDataList, cell, isHead);
			if (columnWidth >= 0) {
				if (columnWidth > 255) {
					columnWidth = 255;
				}
				Integer maxColumnWidth = maxColumnWidthMap.get(cell.getColumnIndex());
				if (maxColumnWidth == null || columnWidth > maxColumnWidth) {
					maxColumnWidthMap.put(cell.getColumnIndex(), columnWidth);
					writeSheetHolder.getSheet()
						.setColumnWidth(cell.getColumnIndex(), columnWidth * 256);
				}
			}
		}
	}

	private Integer dataLength(List<WriteCellData<?>> cellDataList, Cell cell, Boolean isHead) {
		if (isHead) {
			return cell.getStringCellValue().getBytes().length;
		} else {
			CellData cellData = cellDataList.get(0);
			CellDataTypeEnum type = cellData.getType();
			if (type == null) {
				return -1;
			} else {
				switch (type) {
					case STRING:
						return cellData.getStringValue().getBytes().length;
					case BOOLEAN:
						return cellData.getBooleanValue().toString().getBytes().length;
					case NUMBER:
						return cellData.getNumberValue().toString().getBytes().length;
					default:
						return -1;
				}
			}
		}
	}
}

