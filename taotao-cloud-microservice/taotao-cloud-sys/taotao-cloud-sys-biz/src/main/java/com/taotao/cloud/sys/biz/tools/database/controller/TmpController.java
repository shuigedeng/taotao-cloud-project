package com.taotao.cloud.sys.biz.tools.database.controller;

import com.taotao.cloud.sys.biz.tools.database.service.JdbcService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TmpController {

	@Autowired
	private JdbcService jdbcService;

	@RequestMapping("/comments")
	public void writeComments() throws IOException, SQLException {
//        List<TableMetaData> tableMetaDataList = jdbcService.searchTables("10.134.179.119", null, null, "column","court_uuid");
//        Collections.sort(tableMetaDataList,(a,b) -> a.getActualTableName().getSchema().compareTo(b.getActualTableName().getSchema()));
//        Set<String> updateSchema = new HashSet<>();
//        for (TableMetaData tableMetaData : tableMetaDataList) {
//            ActualTableName actualTableName = tableMetaData.getActualTableName();
//            String schema = actualTableName.getSchema();
//            updateSchema.add(schema);
//            String sql = "update "+schema+"."+actualTableName.getTableName()+" set court_uuid='hhdtrailsit201908090a70ab36ccb64';";
//            System.out.println(sql);
//        }
//        System.out.println(updateSchema);
//        // 读取 Excel , 找到有注释的列
//        List<ExcelData> excelDatas = readExcel();
//        // 合并数据
//        Map<String, ExcelData> columnComments = excelDatas.stream().collect(Collectors.toMap(ExcelData::getColumnName, e -> e));
//
//        String connName = "lo";
//        String catalog = null;
//
//        List<TableMetaData> tableMetaDataListAcc = jdbcService.filterSchemaTables(connName, catalog, "acc");
//        List<TableMetaData> tableMetaDataListUam = jdbcService.filterSchemaTables(connName, catalog, "uam");
//        Collection<TableMetaData> union = CollectionUtils.union(tableMetaDataListAcc, tableMetaDataListUam);
//        for (TableMetaData tableMetaData : union) {
//            String tableName = tableMetaData.getActualTableName().getTableName();
//            String schema = tableMetaData.getActualTableName().getSchema();
//            List<Column> columns = tableMetaData.getColumns();
//            for (Column column : columns) {
//                String columnName = column.getColumnName();
//                ExcelData excelData = columnComments.get(columnName);
//                if (excelData != null){
//                    StringBuilder builder = new StringBuilder();
//                    if (StringUtils.isNotBlank(excelData.getDescribe())){
//                        builder.append("[describe]"+excelData.getDescribe());
//                    }
//                    if (StringUtils.isNotBlank(excelData.getEnumValues())){
//                        builder.append("\t[enums]"+excelData.getEnumValues());
//                    }
//                    if (StringUtils.isNotBlank(excelData.getRemark())){
//                        builder.append("\t[remark]"+excelData.getRemark());
//                    }
//                    if (StringUtils.isNotBlank(excelData.getExample())){
//                        builder.append("\t[example]"+excelData.getExample());
//                    }
//                    if (StringUtils.isNotBlank(excelData.getUpdateDescribe())){
//                        builder.append("\t[updateDescribe]"+excelData.getUpdateDescribe());
//                    }
//                    if (StringUtils.isBlank(builder.toString())){
//                        continue;
//                    }
//                    String sql = "COMMENT ON COLUMN "+schema+"."+tableName+"."+columnName+" IS '"+builder.toString()+"';";
//                    System.out.println(sql.replaceAll("\n"," "));
//                }
//            }
//        }
	}

	public List<ExcelData> readExcel() throws IOException {
		List<ExcelData> excelDatas = new ArrayList<>();

		Workbook workbook = new XSSFWorkbook("D:\\doc\\门禁类1\\设计相关/门禁表设计.xlsx");
		int numberOfSheets = workbook.getNumberOfSheets();
		Set<String> columnNames = new HashSet<>();
		for (int i = 1; i < numberOfSheets; i++) {
			Sheet sheetAt = workbook.getSheetAt(i);
			int physicalNumberOfRows = sheetAt.getPhysicalNumberOfRows();
			for (int j = 1; j < physicalNumberOfRows; j++) {
				Row row = sheetAt.getRow(j);
				if (row == null) {
					continue;
				}
				Cell cell1 = row.getCell(1);
				Cell cell2 = row.getCell(2);
				if (cell1 == null) {
					continue;
				}
				String columnName = cell1.getStringCellValue();
				String describe = cellStringValue(cell2);

				Cell cell8 = row.getCell(8);
				String updateDescribe = cellStringValue(cell8);

				Cell cell9 = row.getCell(9);
				String example = cellStringValue(cell9);

				Cell cell10 = row.getCell(10);
				String enumValues = cellStringValue(cell10);
				enumValues = enumValues.replaceAll("\n", ",");
				Cell cell11 = row.getCell(11);
				String remark = cellStringValue(cell11);

				if (columnNames.contains(columnName)) {
					System.out.println(columnName + " 已有注释 ");
					continue;
				}
				excelDatas.add(
					new ExcelData(columnName, describe, updateDescribe, example, enumValues,
						remark));
				columnNames.add(columnName);
//                System.out.println(StringUtils.join(Arrays.asList(columnName,describe,updateDescribe,example,enumValues,remark),"\t"));
			}
		}
		return excelDatas;
	}

	public static class ExcelData {

		private String columnName;
		private String describe;
		private String updateDescribe;
		private String example;
		private String enumValues;
		private String remark;

		public ExcelData() {
		}

		public ExcelData(String columnName, String describe, String updateDescribe, String example,
			String enumValues, String remark) {
			this.columnName = columnName;
			this.describe = describe;
			this.updateDescribe = updateDescribe;
			this.example = example;
			this.enumValues = enumValues;
			this.remark = remark;
		}

		public String getColumnName() {
			return columnName;
		}

		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}

		public String getDescribe() {
			return describe;
		}

		public void setDescribe(String describe) {
			this.describe = describe;
		}

		public String getUpdateDescribe() {
			return updateDescribe;
		}

		public void setUpdateDescribe(String updateDescribe) {
			this.updateDescribe = updateDescribe;
		}

		public String getExample() {
			return example;
		}

		public void setExample(String example) {
			this.example = example;
		}

		public String getEnumValues() {
			return enumValues;
		}

		public void setEnumValues(String enumValues) {
			this.enumValues = enumValues;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}
	}

	private String cellStringValue(Cell cell) {
		if (cell != null) {
			//switch (cell.getCellType()) {
			//	case CellType.STRING:
			//		return cell.getStringCellValue();
			//	case CellType.NUMERIC:
			//		return cell.getNumericCellValue() + "";
			//	case CellType.BLANK:
			//		break;
			//	default:
			//		System.out.println(
			//			"未实现的单元格类型[" + cell.getRow().getRowNum() + ":" + cell.getColumnIndex()
			//				+ "] : " + cell.getCellType());
			//}
		}
		return "";
	}
}
