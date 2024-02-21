package ${package}.${moduleName}.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
<#list importList as i>
import ${i!};
</#list>
<#if baseClass??>
import ${baseClass.packageName}.${baseClass.code};
</#if>

/**
 * ${tableComment}
 *
 * @author ${author} ${email}
 * @since ${version} ${date}
 */
<#if baseClass??>@EqualsAndHashCode(callSuper=false)</#if>
@Data
@TableName("${tableName}")
public class ${ClassName}Entity<#if baseClass??> extends ${baseClass.code}</#if> {
<#list fieldList as field>
<#if !field.baseField>
	<#if field.fieldComment!?length gt 0>
	/**
	* ${field.fieldComment}
	*/
	</#if>
    <#if field.autoFill == "INSERT">
	@TableField(fill = FieldFill.INSERT)
	</#if>
	<#if field.autoFill == "INSERT_UPDATE">
	@TableField(fill = FieldFill.INSERT_UPDATE)
	</#if>
	<#if field.autoFill == "UPDATE">
		@TableField(fill = FieldFill.UPDATE)
	</#if>
    <#if field.primaryPk>
	@TableId
	</#if>
	private ${field.attrType} ${field.attrName};
</#if>

</#list>
}