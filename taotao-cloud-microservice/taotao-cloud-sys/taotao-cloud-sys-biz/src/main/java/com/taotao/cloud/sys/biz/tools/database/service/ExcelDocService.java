package com.taotao.cloud.sys.biz.tools.database.service;
import com.taotao.cloud.sys.biz.tools.core.service.file.FileManager;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.Column;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.PrimaryKey;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.Table;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.TableMetaData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExcelDocService {

    @Autowired
    private JdbcService jdbcService;

    @Autowired
    private FileManager fileManager;

    public Path generate(String connName,String catalog,String [] schemas) throws IOException, SQLException {
        HashSet<String> schemasSet = new HashSet<>(Arrays.asList(schemas));
        List<TableMetaData> tableMetaDataList = jdbcService.filterSchemaTables(connName,catalog,schemasSet);
        Workbook workbook = new XSSFWorkbook();

        for (TableMetaData tableMetaData : tableMetaDataList) {
            Table table = tableMetaData.getTable();
            List<Column> columns = tableMetaData.getColumns();
            String remark = table.getRemark();
            if (StringUtils.isBlank(remark)){
                remark = table.getActualTableName().getTableName();
            }
            Sheet sheet = workbook.createSheet(remark);
            Row head = sheet.createRow(0);
            fillValues(head,"字段名（英文）","字段名（中文）","字段类型","是否为主关键字","是否为空","枚举值","默认值","样例","描述信息","其它信息");

            List<PrimaryKey> primaryKeys = tableMetaData.getPrimaryKeys();
            List<String> primaryKeyColumns = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(primaryKeys)){
                primaryKeyColumns = primaryKeys.stream().map(PrimaryKey::getColumnName).collect(Collectors.toList());
            }
            for (int i = 0; i < columns.size(); i++) {
                Column column = columns.get(i);
                Row row = sheet.createRow(i + 1);
                String typeName = column.getTypeName();
                int columnSize = column.getColumnSize();
                int decimalDigits = column.getDecimalDigits();
                String mergeTypeName = "";
                if (decimalDigits != 0){
                    mergeTypeName = typeName+"("+columnSize+","+decimalDigits+")";
                }else{
                    mergeTypeName = typeName+"("+columnSize+")";
                }
                boolean primaryKey = false;
                if (primaryKeyColumns.contains(column.getColumnName())){
                    primaryKey = true;
                }
                fillValues(row,column.getColumnName(),column.getRemark(),mergeTypeName,primaryKey ? "是":"否",column.isNullable() ? "YES":"NO","","",column.getRemark(),"");
            }
        }
        File dir = fileManager.mkTmpDir("db/meta/generate");
        File file = new File(dir, System.currentTimeMillis() + ".xlsx");

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        Path path = fileManager.relativePath(file.toPath());
        return path;
    }

    private void fillValues(Row row,String... values){
        for (int i = 0; i < values.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(values[i]);
        }
    }
}
