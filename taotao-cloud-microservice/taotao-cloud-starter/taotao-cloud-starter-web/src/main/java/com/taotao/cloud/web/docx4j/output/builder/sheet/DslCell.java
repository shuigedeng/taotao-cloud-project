package com.taotao.cloud.web.docx4j.output.builder.sheet;

import com.taotao.cloud.web.docx4j.output.OutputConstants;
import com.taotao.cloud.web.docx4j.output.utils.StringConverterUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.Units;

/**
 * {@link Cell} dsl
 *
 */
public class DslCell {

	public final Cell cell;
	public int rowspan = 1;
	public int colspan = 1;
	public boolean diagonalUp = false;
	/**
	 * 使用ThreadLocal缓存最大列宽 每个sheet渲染结束时回收
	 */
	protected static final ThreadLocal<Map<Integer, Integer>> COLUMN_WIDTH = ThreadLocal.withInitial(
		HashMap::new);
	protected static final ThreadLocal<Map<Integer, Set<DslCell>>> DIAGONAL_COLUMNS =
		ThreadLocal.withInitial(HashMap::new);

	DslCell(Cell cell) {
		this.cell = cell;
	}

	/**
	 * 设置单元格内容
	 *
	 * @param o 单元格内容对象
	 * @return {@link DslCell}
	 */
	public DslCell text(Object o) {
		if (Objects.isNull(o)) {
			this.cell.setBlank();
			return this;
		}

		return this.doSetText(StringConverterUtil.convert(o));
	}

	/**
	 * 以红星开头文本
	 *
	 * @param o 文本对象
	 * @return {@link DslCell}
	 */
	public DslCell redAster(Object o) {
		RichTextString rich =
			this.getWorkBook()
				.getCreationHelper()
				.createRichTextString(OutputConstants.ASTER + StringConverterUtil.convert(o));
		Font aster = CellStyleUtil.defaultHeadFont(this.getWorkBook());
		aster.setColor(Font.COLOR_RED);
		rich.applyFont(0, 1, aster);
		rich.applyFont(1, rich.length(), CellStyleUtil.defaultHeadFont(this.getWorkBook()));

		return this.rich(rich);
	}

	/**
	 * 富文本
	 *
	 * @param text 富文本内容
	 * @return {@link DslCell}
	 */
	public DslCell rich(RichTextString text) {
		this.cell.setCellValue(text);
		return this.doUpdateLength(DslCell.width(text.getString()));
	}

	/**
	 * 设置单元格内容
	 *
	 * @param supplier 单元格内容提供器
	 * @return {@link DslCell}
	 */
	public DslCell text(Supplier<Object> supplier) {
		return this.text(supplier.get());
	}

	/**
	 * 添加图片到单元格
	 *
	 * @return {@link DslCell}
	 */
	public DslCell picture(File file, int width, int height) {
		CreationHelper helper = this.getWorkBook().getCreationHelper();
		Drawing<?> drawing = this.cell.getSheet().createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();

		try (FileInputStream is = new FileInputStream(file)) {
			int format = SpreadSheetPictureType.getFormat(file.getName());
			int index = this.getWorkBook().addPicture(IOUtils.toByteArray(is), format);
			anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
			int c = this.cell.getColumnIndex(), r = this.cell.getRowIndex();
			// 设置图片起止行列
			anchor.setCol1(c);
			anchor.setCol2(c + 1);
			anchor.setRow1(r);
			anchor.setRow2(r + 1);
			// 宽度单位转像素
			double cellHeight = Units.columnWidthToEMU(height * 256) / (Units.EMU_PER_PIXEL * 1.0);
			// 若图片高度大于当前行高度
			if (this.cell.getRow().getHeightInPoints() < cellHeight) {
				// 以图片高度设置单元格高度
				this.cell.getRow().setHeightInPoints((float) Units.pixelToPoints(cellHeight));
			}
			// 插入图片，如果原图宽度大于最终要求的图片宽度，就按比例缩小，否则展示原图
			drawing.createPicture(anchor, index).resize(1);
			// 设置列最大宽度
			this.doUpdateLength(width);
		} catch (IOException e) {
			throw new SpreadSheetExportException(e);
		}

		return this;
	}

	/**
	 * 合并列
	 *
	 * @param colSpan 合并列数
	 * @return {@link DslCell}
	 */
	public DslCell colspan(int colSpan) {
		if (colSpan > 1) {
			this.colspan = colSpan;
		}
		return this;
	}

	/**
	 * 合并行
	 *
	 * @param rowspan 合并行数
	 * @return {@link DslCell}
	 */
	public DslCell rowspan(int rowspan) {
		if (rowspan > 1) {
			this.rowspan = rowspan;
		}

		return this;
	}

	/**
	 * 设置单元格样式
	 *
	 * @param style {@link CellStyle}
	 * @return {@link DslCell}
	 */
	public DslCell style(CellStyle style) {
		this.cell.setCellStyle(style);

		return this;
	}

	/**
	 * 设置单元格为表头样式
	 *
	 * @return {@link DslCell}
	 */
	public DslCell headStyle() {
		return
			this.style(
				Optional.ofNullable(SpreadSheetExporter.getCustomStyle(CustomStyleType.HEAD))
					.orElseGet(this::defaultHeadStyle)
			);
	}

	/**
	 * 设置表头单元格斜线样式
	 *
	 * @return {@link DslCell}
	 */
	public DslCell diagonalHeadStyle(boolean isDiagonalUp) {
		this.setDiagonal(isDiagonalUp);

		return
			this.style(
				Optional.ofNullable(
						SpreadSheetExporter.getCustomStyle(CustomStyleType.SEPARATED_HEAD))
					.orElseGet(() -> this.defaultDiagonalHeadStyle(isDiagonalUp))
			);
	}

	/**
	 * 设置数据单元格斜线样式
	 *
	 * @return {@link DslCell}
	 */
	public DslCell diagonalDataStyle(boolean isDiagonalUp) {
		this.setDiagonal(isDiagonalUp);

		return
			this.style(
				Optional.ofNullable(
						SpreadSheetExporter.getCustomStyle(CustomStyleType.SEPARATED_DATA))
					.orElseGet(() -> this.defaultDiagonalDataStyle(isDiagonalUp))
			);
	}

	/**
	 * 设置单元格为数据行样式
	 *
	 * @return {@link DslCell}
	 */
	public DslCell dataStyle() {
		return
			this.style(
				Optional.ofNullable(SpreadSheetExporter.getCustomStyle(CustomStyleType.DATA))
					.orElseGet(this::defaultDataStyle)
			);
	}

	/**
	 * 带斜线单元格
	 *
	 * @param left  第一部分
	 * @param right 第二部分
	 * @return {@link DslCell}
	 */
	protected DslCell text(String left, String right) {
		this.text(left + OutputConstants.BREAK_LINE + right);
		// 重新更新最大宽度
		return this.doUpdateLength(DslCell.width(left + right));
	}

	/**
	 * 单元格合并范围
	 *
	 * @return {@link CellRangeAddress}
	 */
	protected CellRangeAddress getCellRangeAddress() {
		CellRangeAddress cellAddresses = new CellRangeAddress(
			this.cell.getRowIndex(),
			this.cell.getRowIndex() + this.rowspan - 1,
			this.cell.getColumnIndex(),
			this.cell.getColumnIndex() + this.colspan - 1
		);

		// 单元格合并边框样式
		CellStyle style = this.cell.getCellStyle();
		Sheet sheet = this.cell.getSheet();
		RegionUtil.setBorderLeft(style.getBorderLeft(), cellAddresses, sheet);
		RegionUtil.setBorderRight(style.getBorderRight(), cellAddresses, sheet);
		RegionUtil.setBorderTop(style.getBorderTop(), cellAddresses, sheet);
		RegionUtil.setBorderBottom(style.getBorderBottom(), cellAddresses, sheet);
		RegionUtil.setLeftBorderColor(style.getLeftBorderColor(), cellAddresses, sheet);
		RegionUtil.setRightBorderColor(style.getRightBorderColor(), cellAddresses, sheet);
		RegionUtil.setTopBorderColor(style.getTopBorderColor(), cellAddresses, sheet);
		RegionUtil.setBottomBorderColor(style.getBottomBorderColor(), cellAddresses, sheet);

		return cellAddresses;
	}

	/**
	 * 设置单元格内容并缓存列最长字符串字节数
	 *
	 * @param text 内容
	 * @return {@link DslCell}
	 */
	protected DslCell doSetText(String text) {
		this.cell.setCellValue(text);

		Integer length = Optional.of(text)
			.filter(it -> it.contains(OutputConstants.BREAK_LINE))
			.map(it ->
				// 若存在换行符
				Stream.of(it.split(OutputConstants.BREAK_LINE))
					.mapToInt(DslCell::width)
					.boxed()
					.max(Comparator.comparingInt(t -> t))
					.orElse(0)
			)
			.orElseGet(() -> DslCell.width(text));

		return this.doUpdateLength(length);
	}

	/**
	 * 更新最大宽度
	 *
	 * @param length 宽度
	 * @return {@link DslCell}
	 */
	protected DslCell doUpdateLength(int length) {
		COLUMN_WIDTH.get().merge(this.cell.getColumnIndex(), length, Math::max);
		return this;
	}

	/**
	 * 斜线设置
	 *
	 * @param isDiagonalUp 斜线方向
	 */
	protected void setDiagonal(boolean isDiagonalUp) {
		this.diagonalUp = isDiagonalUp;
		DIAGONAL_COLUMNS.get()
			.merge(this.cell.getColumnIndex(), new HashSet<>(Collections.singletonList(this)),
				(o, n) -> {
					o.addAll(n);

					return o;
				});
	}

	/**
	 * 默认表头样式
	 *
	 * @return {@link CellStyle}
	 */
	protected CellStyle defaultHeadStyle() {
		return CellStyleUtil.defaultHeadStyle(this.getWorkBook());
	}

	/**
	 * 默认斜线表头单元格
	 *
	 * @param isDiagonalUp 斜线方向
	 * @return {@link CellStyle}
	 */
	protected CellStyle defaultDiagonalHeadStyle(boolean isDiagonalUp) {
		return CellStyleUtil.diagonalStyle(this.getWorkBook(), this.defaultHeadStyle(),
			isDiagonalUp);
	}

	/**
	 * 默认数据样式
	 *
	 * @return {@link CellStyle}
	 */
	protected CellStyle defaultDataStyle() {
		return CellStyleUtil.defaultDataStyle(this.getWorkBook());
	}

	/**
	 * 默认斜线数据单元格
	 *
	 * @param isDiagonalUp 斜线方向
	 * @return {@link CellStyle}
	 */
	protected CellStyle defaultDiagonalDataStyle(boolean isDiagonalUp) {
		return CellStyleUtil.diagonalStyle(this.getWorkBook(), this.defaultDataStyle(),
			isDiagonalUp);
	}

	/**
	 * 获得单元格所在的{@link Workbook}
	 *
	 * @return {@link Workbook}
	 */
	protected Workbook getWorkBook() {
		return this.cell.getSheet().getWorkbook();
	}

	/**
	 * 移除线程变量
	 */
	protected static void removeThreadLocal() {
		COLUMN_WIDTH.remove();
		DIAGONAL_COLUMNS.remove();
	}

	protected static int width(String s) {
		return (s.getBytes(StandardCharsets.UTF_8).length + s.length()) / 2;
	}
}
