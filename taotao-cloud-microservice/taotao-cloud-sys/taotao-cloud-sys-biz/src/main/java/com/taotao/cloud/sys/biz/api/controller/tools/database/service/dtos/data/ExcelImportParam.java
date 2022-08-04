package com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.data;

import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos.ActualTableName;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@Data
public class ExcelImportParam {
    @NotNull
    private String connName;
    @Valid
    private ActualTableName actualTableName;
    @PositiveOrZero
    private int startRow;
    @Valid
    private List<Mapping> mapping = new ArrayList<>();

    @Data
    public static class Mapping{
        private int index = -1;
        @NotNull
        private String columnName;
        private String random;
        private String constant;
    }
}
