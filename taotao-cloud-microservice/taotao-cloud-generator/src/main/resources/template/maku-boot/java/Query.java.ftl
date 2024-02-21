package ${package}.${moduleName}.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ${package}.framework.common.query.Query;
import org.springframework.format.annotation.DateTimeFormat;

<#list importList as i>
import ${i!};
</#list>

/**
* ${tableComment}查询
*
* @author ${author} ${email}
* @since ${version} ${date}
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "${tableComment}查询")
public class ${ClassName}Query extends Query {
<#list queryList as field>
    <#if field.fieldComment!?length gt 0>
    @Schema(description = "${field.fieldComment}")
    </#if>
    <#if field.queryFormType == 'date'>
    @DateTimeFormat(pattern="yyyy-MM-dd")
    <#elseif field.queryFormType == 'datetime'>
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    </#if>
    private ${field.attrType}<#if field.queryFormType == 'date' || field.queryFormType == 'datetime'>[]</#if> ${field.attrName};

</#list>
}