package com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.data;

import com.taotao.cloud.sys.biz.api.controller.tools.core.exception.ToolException;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.compare.DiffType;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class DataChangeParam {
    /**
     * 连接名
     */
    @NotBlank
    private String connName;
    /**
     * 操作的数据表
     */
    @Valid
    private ActualTableName actualTableName;
    /**
     * insert 语句的选择列
     */
    private List<String> selectItems = new ArrayList<>();
    /**
     * 查询过滤条件
     */
    private String condition;
    /**
     * 生成 sql 的变更类型 insert update delete
     */
    @NotBlank
    private String changeType;

    /**
     * 生成的方言列表 mysql , oracle
     */
    private List<String> dbTypes = new ArrayList<>();

    /**
     * 获取变更类型
     * @return
     */
    public DiffType getDiffType(){
        switch (changeType){
            case "insert":
                return DiffType.ADD;
            case "update":
                return DiffType.MODIFY;
            case "delete":
                return DiffType.DELETE;
            default:
        }
        throw new ToolException("不支持的变更类型:"+changeType);
    }
}
