package com.taotao.cloud.workflow.api.common.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 *
 */
@Data
public class Pagination extends Page{
    @Schema(description =  "每页条数",example = "20")
    private long pageSize=20;
    @Schema(description =  "排序类型")
    private String sort="desc";
    @Schema(description =  "排序列")
    private String sidx="";
    @Schema(description =  "当前页数",example = "1")
    private long currentPage=1;



    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private long total;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private long records;

    public <T> List<T> setData(List<T> data, long records) {
        this.total = records;
        return data;
    }
}
