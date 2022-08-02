package com.taotao.cloud.sys.biz.modules.database.service.dtos.processors;

import com.sanri.tools.modules.database.service.code.dtos.ProjectGenerateConfig;
import com.sanri.tools.modules.database.service.dtos.data.transfer.TransferDataRow;
import com.sanri.tools.modules.database.service.meta.dtos.Column;
import lombok.Data;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.AbstractListHandler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListDataRowProcessor extends AbstractListHandler<ListDataRowProcessor.Row> {

    @Override
    protected Row handleRow(ResultSet rs) throws SQLException {
        Row row = new Row();
        ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();

        for (int i = 1; i <= cols; i++) {
            final Column column = new Column();
            column.setColumnName(rsmd.getColumnName(i));
            column.setDataType(rsmd.getColumnType(i));
            row.addField(new TransferDataRow.TransferDataField(column,rs.getObject(i)));
        }
        return row;
    }

    @Data
    public static final class Row{
        private List<TransferDataRow.TransferDataField> fields = new ArrayList<>();

        public void addField(TransferDataRow.TransferDataField field){
            fields.add(field);
        }
        /**
         * 列名列表
         * @return
         */
        public List<String> getColumnNames(){
            return fields.stream().map(TransferDataRow.TransferDataField::getColumn).map(Column::getColumnName).collect(Collectors.toList());
        }
    }

    public static final ListDataRowProcessor INSTANCE = new ListDataRowProcessor();
}
