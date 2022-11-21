package com.taotao.cloud.office.utils.easyexcel.core;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import java.util.Map;
import java.util.TreeMap;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;

/**
 * 这个类的作用主要是给列增加下拉框 主要是为了方便用户填写数据
 */
public class CustomSheetWriteHandler implements SheetWriteHandler {

	/**
	 * 存放下拉内容的集合 key为列的下标， value为下拉内容数组
	 */
	private final Map<Integer, String[]> map = new TreeMap<>();

	/**
	 * 工作簿下标，从0开始
	 */
	private int index = 0;

	/**
	 * 给多少行添加下拉框，这里默认给2000行
	 */
	private final int batchSize = 2000;


	@Override
	public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder,
		WriteSheetHolder writeSheetHolder) {

	}

	/**
	 * 宝藏在此：如果下拉框内容总的长度超过255，会导致Cell有下拉框，但是下拉内容显示不了， 这时我们可以新建一个sheet，将其隐藏，然后将里面的内容引用到我们的下拉框列就可以。
	 * 值得细品
	 *
	 * @param writeWorkbookHolder
	 * @param writeSheetHolder
	 */
	@Override
	public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder,
		WriteSheetHolder writeSheetHolder) {
		// excel下标从0开始，这里第二列的下拉选择内容
		map.put(1, new String[]{"下拉内容一", "下拉内容二"});
		// excel下标从0开始，这里第三列的下拉选择内容
		map.put(3, new String[]{"北京市", "上海市", "重庆市", "天津市"});

		DataValidationHelper helper = writeSheetHolder.getSheet().getDataValidationHelper();
		map.forEach((k, v) -> {
			// 创建sheet，突破下拉框255的限制
			// 获取一个workbook
			Workbook workbook = writeWorkbookHolder.getWorkbook();
			// 定义sheet的名称
			String sheetName = "sheet" + k;
			// 1.创建一个隐藏的sheet 名称为 proviceSheet
			Sheet proviceSheet = workbook.createSheet(sheetName);
			// 从第二个工作簿开始隐藏
			this.index++;
			// 设置隐藏
			workbook.setSheetHidden(this.index, true);
			// 2.循环赋值（为了防止下拉框的行数与隐藏域的行数相对应，将隐藏域加到结束行之后）
			for (int i = 0, length = v.length; i < length; i++) {
				// i:表示你开始的行数 0表示你开始的列数
				proviceSheet.createRow(i).createCell(0).setCellValue(v[i]);
			}
			Name category1Name = workbook.createName();
			category1Name.setNameName(sheetName);
			// 4 $A$1:$A$N代表 以A列1行开始获取N行下拉数据
			category1Name.setRefersToFormula(sheetName + "!$A$1:$A$" + (v.length));
			// 5 将刚才设置的sheet引用到你的下拉列表中,1表示从行的序号1开始（开始行，通常行的序号为0的行是表头），50表示行的序号50（结束行），表示从行的序号1到50，k表示开始列序号和结束列序号
			CellRangeAddressList addressList = new CellRangeAddressList(1, batchSize, k, k);
			DataValidationConstraint constraint8 = helper.createFormulaListConstraint(sheetName);
			DataValidation dataValidation3 = helper.createValidation(constraint8, addressList);

			// 阻止输入非下拉选项的值
			dataValidation3.setErrorStyle(DataValidation.ErrorStyle.STOP);
			dataValidation3.setShowErrorBox(true);
			dataValidation3.setSuppressDropDownArrow(true);
			dataValidation3.createErrorBox("提示", "此值与单元格定义格式不一致");
			// validation.createPromptBox("填写说明：","填写内容只能为下拉数据集中的单位，其他单位将会导致无法入仓");
			writeSheetHolder.getSheet().addValidationData(dataValidation3);
		});
	}
}
