package com.taotao.cloud.sys.biz.modules.database.service.dtos.data.transfer;

import com.sanri.tools.modules.database.service.dtos.compare.DiffType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.*;

@Data
@Slf4j
public class DataChange {
    private DiffType diffType;
    private String tableName;

    private Insert insert;
    private Update update;
    private Delete delete;

    public DataChange(DiffType diffType, String tableName) {
        this.diffType = diffType;
        this.tableName = tableName;
    }

    @Data
    public static final class Insert{
        private Collection<String> columnNames = new ArrayList<>();
        private List<ColumnValue> columnValues = new ArrayList<>();

        /**
         * 唯一键, 通常是主键 , 用于 oracle 插入数据
         */
        private String uniqueKey;

        public Insert(Collection<String> columnNames) {
            this.columnNames = columnNames;
        }

        public void addColumnValue(ColumnValue columnValue){
            columnValues.add(columnValue);
        }
    }

    @Data
    public static final class Update{
        private Map<String,ColumnValue> columnSet = new HashMap<>();
        private Condition where;

        public void putColumnSet(String columnName,ColumnValue columnValue){
            columnSet.put(columnName,columnValue);
        }
    }

    @Data
    public static final class Delete{
       private Condition where;
    }

    @Data
    public static final class Condition{
        private String columnName;
        private ColumnValue columnValue ;

        public Condition(String columnName, ColumnValue columnValue) {
            this.columnName = columnName;
            this.columnValue = columnValue;
        }
    }

    public static final class ColumnValue{
        private String value;
        private ColumnType type = DEFAULT;

        public ColumnValue(String value, ColumnType type) {
            this.value = value;
            this.type = type;
        }

        public ColumnValue(String value) {
            this.value = value;
        }

        public String getValue() {
            if (type == null){
                return value;
            }
            if (value == null){
                return "NULL";
            }
            return type.format(value);
        }
    }

    public static final ColumnType DEFAULT = new Default();
    public static final ColumnType NUMBER = new Number();
    public static final ColumnType VARCHAR = new Varchar();
    public static final ColumnType DATETIME = new DateTime();


    public interface ColumnType{
        /**
         * 格式化值
         * @param value
         * @return
         */
        String format(String value);

        /**
         * 类型名称
         * @return
         */
        default String getName(){
            return getClass().getSimpleName();
        }
    }

    /**
     * 数字类型
     */
    public static final class Number implements ColumnType{
        @Override
        public String format(String value) {
            return value;
        }
    }

    /**
     * 字符串类型
     */
    public static final class Varchar implements ColumnType{
        @Override
        public String format(String value) {
            if (value == null){
                return "NULL";
            }
            return StringUtils.wrap(value,"'");
        }
    }

    /**
     * 日期类型
     */
    public static final class DateTime implements ColumnType{
        private String[] formats = DEFAULT_FORMATS;

        /**
         * 解析日期格式
         */
        public static final String[] DEFAULT_FORMATS = {"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm:ss.S"};

        /**
         * 插入数据库的标准日期格式
         */
        public static final String DB_STANDARD_DATE = "yyyy-MM-dd HH:mm:ss";

        public DateTime() {
            this.formats = DEFAULT_FORMATS;
        }

        public DateTime(String[] formats) {
            this.formats = formats;
        }

        @Override
        public String format(String value) {
            try {
                final Date date = DateUtils.parseDate(value, formats);

                // 进入数据库的标准日期格式
                return StringUtils.wrap(DateFormatUtils.format(date, DB_STANDARD_DATE),"'");
            } catch (ParseException e) {
                log.error("数据解析时日期异常:{}",e.getMessage());
            }

            // 如果解析异常, 返回原值
            return value;
        }
    }

    /**
     * 默认返回原值
     */
    public static final class Default implements ColumnType{
        @Override
        public String format(String value) {
            return value;
        }
    }

}
