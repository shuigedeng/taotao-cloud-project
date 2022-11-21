package com.taotao.cloud.office.utils.easyexcel.core;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import java.util.List;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 实现CellWriteHandler接口, 实现对单元格样式的精确控制
 **/
public class CustomCellWriteHandler implements CellWriteHandler {

	/**
	 * 创建单元格之前的操作
	 */
	@Override
	public void beforeCellCreate(WriteSheetHolder writeSheetHolder,
		WriteTableHolder writeTableHolder, Row row,
		Head head, Integer integer, Integer integer1, Boolean aBoolean) {

	}

	/**
	 * 创建单元格之后的操作
	 */
	@Override
	public void afterCellCreate(WriteSheetHolder writeSheetHolder,
		WriteTableHolder writeTableHolder, Cell cell,
		Head head, Integer integer, Boolean aBoolean) {

	}

	/**
	 * 单元格内容转换之后的操作
	 */
	@Override
	public void afterCellDataConverted(WriteSheetHolder writeSheetHolder,
		WriteTableHolder writeTableHolder, WriteCellData<?> cellData, Cell cell, Head head,
		Integer relativeRowIndex, Boolean isHead) {

		CellWriteHandler.super.afterCellDataConverted(writeSheetHolder, writeTableHolder, cellData,
			cell, head, relativeRowIndex, isHead);
	}

	/**
	 * 单元格处理后(已写入值)的操作
	 */
	@Override
	public void afterCellDispose(WriteSheetHolder writeSheetHolder,
		WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell,
		Head head, Integer relativeRowIndex, Boolean isHead) {

		// 设置超链接
		if (isHead && cell.getRowIndex() == 0 && cell.getColumnIndex() == 0) {
			CreationHelper helper = writeSheetHolder.getSheet().getWorkbook().getCreationHelper();
			Hyperlink hyperlink = helper.createHyperlink(HyperlinkType.URL);
			hyperlink.setAddress("https://github.com/alibaba/easyexcel");
			cell.setHyperlink(hyperlink);
		}

		// 精确设置单元格格式
		boolean bool = isHead && cell.getRowIndex() == 1;
		if (bool) {
			// 获取工作簿
			Workbook workbook = writeSheetHolder.getSheet().getWorkbook();
			CellStyle cellStyle = workbook.createCellStyle();

			Font cellFont = workbook.createFont();
			cellFont.setBold(Boolean.TRUE);
			cellFont.setFontHeightInPoints((short) 14);
			cellFont.setColor(IndexedColors.SEA_GREEN.getIndex());
			cellStyle.setFont(cellFont);
			cell.setCellStyle(cellStyle);
		}
	}


}
