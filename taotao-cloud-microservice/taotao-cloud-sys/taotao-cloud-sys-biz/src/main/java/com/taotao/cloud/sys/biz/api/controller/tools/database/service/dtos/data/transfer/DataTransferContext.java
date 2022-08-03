package com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.data.transfer;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class DataTransferContext {
    @NotNull
    private String sourceConnName;
    @NotNull
    private String targetConnName;
    @Valid
    private Namespace sourceNamespace;
    @Valid
    private Namespace targetNamespace;

    /**
     * 迁移的数据表信息
     */
    private List<String> tableNames = new ArrayList<>();

    /**
     * 数据传输配置
     */
    private TransferConfig transferConfig;

    /**
     * 数据表映射配置 tableName => TableMeta
     */
    private Map<String,TableMeta> tableMirrorConfig = new HashMap<>();

    @Data
    public static final class TransferConfig{
        private TableStruct tableStruct;
        private TableData tableData;

        @Data
        public static final class TableStruct{
            private boolean ddl;
            private boolean dropIfExist;
            private boolean index;
            private boolean tableAndColumn;
        }

        @Data
        public static final class TableData{
            private boolean checkExist;
            private boolean deleteDataBeforeTransfer;

            /**
             * 普通表读写配置
             */
            private DataReadWriteConfig normalTableReadWriteConfig;

            /**
             * 大字段表读写配置
             */
            private DataReadWriteConfig bigColumnTableReadWriteConfig;
        }

        @Data
        public static final class DataReadWriteConfig{
            /**
             * 源库一次读取行数
             */
            private int sourceReadCount;
            /**
             * 目标库一次提交行数
             */
            private int targetCommitCount;
            /**
             * 缓存批次
             */
            private int cacheBatch;
        }
    }
}
