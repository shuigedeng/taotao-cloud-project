package com.taotao.cloud.office.utils.easyexcel.core;

import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

/**
 * 自定义拦截器.新增注释,第一行头加批注 这个类的作用主要是给表头添加批注
 */
public class CommentWriteHandler implements RowWriteHandler {

	@Override
	public void beforeRowCreate(WriteSheetHolder writeSheetHolder,
		WriteTableHolder writeTableHolder, Integer rowIndex, Integer relativeRowIndex,
		Boolean isHead) {

	}

	@Override
	public void afterRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
		Row row, Integer relativeRowIndex, Boolean isHead) {

	}

	@Override
	public void afterRowDispose(WriteSheetHolder writeSheetHolder,
		WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {
		Sheet sheet = writeSheetHolder.getSheet();
		Drawing<?> drawingPatriarch = sheet.createDrawingPatriarch();

		// 在第一行 第二列创建一个批注
		Comment comment1 =
			drawingPatriarch.createCellComment(
				new XSSFClientAnchor(0, 0, 0, 0, (short) 0, 0, (short) 1, 1));
		// 输入批注信息
		comment1.setString(new XSSFRichTextString("批注1"));
		// 将批注添加到单元格对象中
		sheet.getRow(0).getCell(0).setCellComment(comment1);
 
		Comment comment2 =
			drawingPatriarch.createCellComment(
				new XSSFClientAnchor(0, 0, 0, 0, (short) 1, 0, (short) 2, 1));
		// 输入批注信息
		comment2.setString(new XSSFRichTextString("批注2"));
		// 将批注添加到单元格对象中
		sheet.getRow(0).getCell(1).setCellComment(comment2);

		Comment comment3 =
			drawingPatriarch.createCellComment(
				new XSSFClientAnchor(0, 0, 0, 0, (short) 2, 0, (short) 3, 1));
		// 输入批注信息
		comment3.setString(new XSSFRichTextString("批注3"));
		// 将批注添加到单元格对象中
		sheet.getRow(0).getCell(2).setCellComment(comment3);
	}
}
