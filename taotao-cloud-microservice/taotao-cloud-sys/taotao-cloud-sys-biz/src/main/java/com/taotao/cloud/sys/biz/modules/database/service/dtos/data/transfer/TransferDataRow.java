package com.taotao.cloud.sys.biz.modules.database.service.dtos.data.transfer;

import com.sanri.tools.modules.database.service.meta.dtos.ActualTableName;
import com.sanri.tools.modules.database.service.meta.dtos.Column;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 数据迁移时, 传输的数据
 */
@Data
public class TransferDataRow {
    private ActualTableName actualTableName;
    private List<TransferDataField> fields = new ArrayList<>();

    public TransferDataRow(ActualTableName actualTableName) {
        this.actualTableName = actualTableName;
    }

    public void addField(TransferDataField transferDataField) {
        fields.add(transferDataField);
    }

    @Data
    public static final class TransferDataField{
        /**
         * 来源列
         */
        private Column column;
        /**
         * 来源列的值
         */
        private Object value;

        public TransferDataField(Column column, Object value) {
            this.column = column;
            this.value = value;
        }

        public DataChange.ColumnType getColumnType(){
            final int dataType = column.getDataType();
            switch (dataType){
                case 12:
                    // varchar
                    return DataChange.VARCHAR;
                case -6:
                case 5:
                case 4:
                case -5:
                case 6:
                case 8:
                case 2:
                case 3:
                    // number
                    return DataChange.NUMBER;
                case 91:
                case 92:
                case 93:
                    // datetime
                    return DataChange.DATETIME;
                default:

            }
            return DataChange.DEFAULT;
        }
    }

    /**
     * 获取列名列表
     * @return
     */
    public List<String> getColumnNames(){
        return fields.stream().map(TransferDataField::getColumn).map(Column::getColumnName).collect(Collectors.toList());
    }
}
