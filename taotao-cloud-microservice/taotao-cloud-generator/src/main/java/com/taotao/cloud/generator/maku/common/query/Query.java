package com.taotao.cloud.generator.maku.common.query;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;
import lombok.Data;

/**
 * 查询公共参数
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
public class Query {
    String code;
    String tableName;
    String attrType;
    String columnType;
    String connName;
    String dbType;
    String projectName;

    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小值为 1")
    Integer page;

    @NotNull(message = "每页条数不能为空")
    @Range(min = 1, max = 1000, message = "每页条数，取值范围 1-1000")
    Integer limit;
}
