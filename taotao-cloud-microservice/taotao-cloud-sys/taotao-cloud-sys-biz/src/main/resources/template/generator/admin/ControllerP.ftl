/*
* Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      https://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package ${package}.rest;
import java.util.Arrays;
import co.yixiang.dozer.service.IGenerator;
import lombok.AllArgsConstructor;
import co.yixiang.logging.aop.log.Log;
import ${package}.domain.${className};
import ${package}.service.${className}Service;
import ${package}.service.dto.${className}QueryCriteria;
import ${package}.service.dto.${className}Dto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author ${author}
* @date ${date}
*/
@AllArgsConstructor
@Api(tags = "${apiAlias}管理")
@RestController
@RequestMapping("/api/${changeClassName}")
public class ${className}Controller {

private final ${className}Service ${changeClassName}Service;
private final IGenerator generator;


@Log("导出数据")
@ApiOperation("导出数据")
@GetMapping(value = "/download")
@PreAuthorize("@el.check('admin','${changeClassName}:list')")
public void download(HttpServletResponse response, ${className}QueryCriteria criteria) throws IOException {
${changeClassName}Service.download(generator.convert(${changeClassName}Service.queryAll(criteria), ${className}Dto.class), response);
}

@GetMapping
@Log("查询${apiAlias}")
@ApiOperation("查询${apiAlias}")
@PreAuthorize("@el.check('admin','${changeClassName}:list')")
public ResponseEntity
<Object> get${className}s(${className}QueryCriteria criteria, Pageable pageable){
    return new ResponseEntity<>(${changeClassName}Service.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增${apiAlias}")
    @ApiOperation("新增${apiAlias}")
    @PreAuthorize("@el.check('admin','${changeClassName}:add')")
    public ResponseEntity
    <Object> create(@Validated @RequestBody ${className} resources){
        return new ResponseEntity<>(${changeClassName}Service.save(resources),HttpStatus.CREATED);
        }

        @PutMapping
        @Log("修改${apiAlias}")
        @ApiOperation("修改${apiAlias}")
        @PreAuthorize("@el.check('admin','${changeClassName}:edit')")
        public ResponseEntity
        <Object> update(@Validated @RequestBody ${className} resources){
            ${changeClassName}Service.updateById(resources);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            @Log("删除${apiAlias}")
            @ApiOperation("删除${apiAlias}")
            @PreAuthorize("@el.check('admin','${changeClassName}:del')")
            @DeleteMapping
            public ResponseEntity
            <Object> deleteAll(@RequestBody ${pkColumnType}[] ids) {
                Arrays.asList(ids).forEach(id->{
                ${changeClassName}Service.removeById(id);
                });
                return new ResponseEntity<>(HttpStatus.OK);
                }
                }
