package ${package}.${moduleName}.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import ${package}.framework.common.utils.PageResult;
import ${package}.framework.common.utils.Result;
import ${package}.${moduleName}.convert.${ClassName}Convert;
import ${package}.${moduleName}.entity.${ClassName}Entity;
import ${package}.${moduleName}.service.${ClassName}Service;
import ${package}.${moduleName}.query.${ClassName}Query;
import ${package}.${moduleName}.vo.${ClassName}VO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
* ${tableComment}
*
* @author ${author} ${email}
* @since ${version} ${date}
*/
@RestController
@RequestMapping("${moduleName}/${functionName}")
@Tag(name="${tableComment}")
@AllArgsConstructor
public class ${ClassName}Controller {
    private final ${ClassName}Service ${className}Service;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('${moduleName}:${functionName}:page')")
    public Result<PageResult<${ClassName}VO>> page(@ParameterObject @Valid ${ClassName}Query query){
        PageResult<${ClassName}VO> page = ${className}Service.page(query);

        return Result.ok(page);
    }

    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('${moduleName}:${functionName}:info')")
    public Result<${ClassName}VO> get(@PathVariable("id") Long id){
        ${ClassName}Entity entity = ${className}Service.getById(id);

        return Result.ok(${ClassName}Convert.INSTANCE.convert(entity));
    }

    @PostMapping
    @Operation(summary = "保存")
    @PreAuthorize("hasAuthority('${moduleName}:${functionName}:save')")
    public Result<String> save(@RequestBody ${ClassName}VO vo){
        ${className}Service.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @PreAuthorize("hasAuthority('${moduleName}:${functionName}:update')")
    public Result<String> update(@RequestBody @Valid ${ClassName}VO vo){
        ${className}Service.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('${moduleName}:${functionName}:delete')")
    public Result<String> delete(@RequestBody List<Long> idList){
        ${className}Service.delete(idList);

        return Result.ok();
    }
}
