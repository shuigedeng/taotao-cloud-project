package ${package}.${moduleName}.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import ${package}.framework.common.utils.DateUtils;
<#list importList as i>
import ${i!};
</#list>

/**
* ${tableComment}
*
* @author ${author} ${email}
* @since ${version} ${date}
*/
@Data
@Schema(description = "${tableComment}")
public class ${ClassName}VO implements Serializable {
	private static final long serialVersionUID = 1L;

<#list fieldList as field>
	<#if field.fieldComment!?length gt 0>
	@Schema(description = "${field.fieldComment}")
	</#if>
	<#if field.attrType == 'Date'>
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	</#if>
	private ${field.attrType} ${field.attrName};

</#list>

}