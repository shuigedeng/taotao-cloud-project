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

package com.taotao.cloud.sys.biz.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taotao.boot.common.model.result.PageResult;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.common.model.ValidList;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.security.spring.annotation.NotAuth;
import com.taotao.cloud.sys.api.model.vo.UploadFileVO;
import com.taotao.cloud.sys.biz.config.properties.TestProperties;
import com.taotao.cloud.sys.biz.service.IFileService;
import com.taotao.boot.web.annotation.BusinessApi;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 文件管理管理接口
 *
 * @author Chopper
 * @since 2020/11/26 15:41
 */
@BusinessApi
@Validated
@RestController
@AllArgsConstructor
@Tag(name = "文件管理接口11111")
@RequestMapping("/file/common")
public class FileController {

    private final IFileService fileService;
    private final ISeataTccService seataTccService;
    private final TestProperties testProperties;

    // @Autowired
    // private Cache cache;

    @NotAuth
    @Operation(summary = "testATSeata", description = "testATSeata")
    @GetMapping("/testATSeata")
    public Result<Integer> testATSeata() {
//        fileService.testSeata();
        Integer signInFailureLimited = testProperties.getSignInFailureLimited();
        return Result.success(signInFailureLimited);
    }

    @NotAuth
    @Operation(summary = "testTccSeata", description = "testTccSeata")
    @PostMapping("/testTccSeata")
    public Result<Boolean> testTccSeata() {
        fileService.test(1L);

        return Result.success(true);
    }


    @NotAuth
    @Operation(summary = "测试校验", description = "测试校验")
    @PostMapping("/qqqqqq/batch")
    public Result<Boolean> qqqqqqBatch(
            @Valid @NotEmpty(message = "数据不能为空") @RequestBody ValidList<Student> ids ) {
        return Result.success(true);
    }

    @NotAuth
    @Operation(summary = "批量删除", description = "批量删除")
    @Parameters({
            @Parameter(name = "ids", required = true, description = "id列表"),
    })
    @DeleteMapping("/11111/batch")
    public Result<Boolean> delAllByIds(
            @Valid @NotNull(message = "id列表不能为空") @Size(min = 1, max = 3, message = "id个数只能在1至3个")
            @RequestParam List<Long> ids ) {
        return Result.success(true);
    }

    @NotAuth
    @Operation(summary = "更改规格", description = "更改规格")
    @Parameters({
            @Parameter(name = "id", required = true, description = "id", in = ParameterIn.PATH),
    })
    @PutMapping("/2222/{id}")
    public Result<Boolean> update( @Valid @RequestBody Student specificationDTO,
            @NotNull(message = "id不能为空") @PathVariable Long id ) {
        return Result.success(true);
    }

    @NotAuth
    @Operation(summary = "更改规格222", description = "更改规格222")
    @PutMapping("/33334")
    public Result<Boolean> updateBatch( @Valid @RequestBody List<Student> specificationDTO ) {
        return Result.success(true);
    }


    @NotAuth
    @Operation(summary = "文件上传", description = "文件上传",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json")),
            responses = {@ApiResponse(description = "是否成功", content = @Content(mediaType = "application/json"))})
    @Parameters({
            @Parameter(name = "type", required = true, description = "类型", schema = @Schema(type = "integer")),
            @Parameter(name = "file", required = true, description = "文件信息", schema = @Schema(type = "file"))
    })
    @PostMapping(value = "/upload")
    public Result<UploadFileVO> upload( @NotNull(message = "类型不能为空") @RequestParam("type") Integer type,
            @NotNull(message = "文件不能为空") @RequestPart("file") MultipartFile file ) {
//        return Result.success(fileService.uploadFile(String.valueOf(type), file));
        return null;
    }

    @NotAuth
    @Operation(summary = "给属性分配权限", description = "给属性分配权限，方便权限数据操作")
    @Parameters({
            @Parameter(name = "attributeId", required = true, description = "attributeId"),
    })
    @PutMapping("/xxxxxx")
    public Result<String> assign( @RequestParam(name = "attributeId") String attributeId,
            @Validated @NotEmpty(message = "权限不能为空") @RequestBody String[] permissions ) {
        return Result.success("sdfasdf");
    }

    @Operation(summary = "根据用户id更新角色信息(用户分配角色)", description = "后台页面-用户信息页面-根据用户id更新角色信息(用户分配角色)")
    @Parameters({
            @Parameter(name = "userId", description = "用户id", required = true, example = "123", in = ParameterIn.PATH),
    })
    @PutMapping("/roles/{userId}")
    @NotAuth
    public Result<Boolean> updateUserRoles(
            @NotNull(message = "用户id不能为空") @PathVariable(name = "userId") Long userId,
            @Validated @NotEmpty(message = "角色id列表不能为空") @RequestBody List<String> roleIds ) {
        LogUtils.info("请求参数： name = {}, age = {}", userId, roleIds);
        return Result.success();
    }

    @NotAuth
    @Operation(summary = "给用户分配角色", description = "给用户分配角色")
    @Parameters({
            @Parameter(name = "userId", required = true, description = "userId"),
            @Parameter(name = "roles", required = true, description = "角色对象组成的数组")
    })
    @GetMapping("/sss")
    public Result<String> assign1111( @RequestParam(name = "userId") String userId,
            @RequestParam(name = "roles") List<String> roles ) {
        return Result.success("sdfasdf");
    }

    @NotAuth
    @Operation(summary = "helloParam", description = "helloParam")
    @Parameters({
            @Parameter(name = "name", required = true, description = "名称"),
            @Parameter(name = "age", required = true, description = "年龄"),
            @Parameter(name = "birthDay", required = true, description = "生日"),
            @Parameter(name = "plays", required = true, description = "plays游戏"),
            @Parameter(name = "plays2", required = true, description = "plays2玩家")
    })
    @GetMapping(value = "/helloParam")
    public Result<String> hello( @RequestParam("name") String name,
            @RequestParam("age") Integer age,
            @RequestParam("birthDay") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime birthDay,
            @RequestParam("plays") String[] plays,
            @RequestParam("plays2") List<String> plays2 ) {
        LogUtils.info("请求参数： name = {}, age = {}, birthDay = {}, plays = {}, plays2 = {}", name, age, birthDay,
                plays, plays2);
        return Result.success("scesccc");
    }

    @NotAuth
    @Operation(summary = "helloParam3", description = "helloParam3",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json")),
            responses = {@ApiResponse(description = "helloParam", content = @Content(mediaType = "application/json"))})
    @GetMapping(value = "/helloParam3")
    public Result<String> hello( @Validated Page page ) {
        LogUtils.info("请求参数： name = {}, age = {}, birthDay = {}, 分页参数：page = {}", "", "", "", page);
        return Result.success("scesccc");
    }

    @Operation(summary = "分页获取商品列表", description = "分页获取商品列表")
    @RequestLogger("分页获取商品列表")
    @NotAuth
    @GetMapping(value = "/sku/page")
    public Result<PageResult<Integer>> getSkuByPage( @Validated GoodsPageQuery goodsPageQuery ) {
        return Result.success(new PageResult<Integer>());
    }

    @Operation(summary = "helloParam2", description = "helloParam2")
    @NotAuth
    @GetMapping(value = "/helloParam2")
    public Result<String> hello( Page page, Student student ) {
        LogUtils.info("请求对象参数： student = {}, 分页参数 = {}", student, page);
        return Result.success("scesccc");
    }

    @NotAuth
    @Operation(summary = "form1", description = "form1")
    @GetMapping(value = "/form1")
//    @Parameters({
//            @Parameter(name = "name", required = true, description = "name"),
//            @Parameter(name = "age", required = true, description = "age"),
//            @Parameter(name = "birthDay", required = true, description = "birthDay", schema = @Schema(implementation = LocalDateTime.class)),
//            @Parameter(name = "plays", required = true, description = "plays"),
//    })
    public Result<String> form( Student student ) {
        LogUtils.info("obj请求参数： name = {}, age = {}", student.getName(), student.getAge());
        return Result.success("scesccc");
    }

    @NotAuth
    @Operation(summary = "application", description = "application",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = APPLICATION_JSON_VALUE)),
            responses = {
                    @ApiResponse(description = "application", responseCode = "200", content = @Content(mediaType = APPLICATION_JSON_VALUE))})
    @PostMapping("/application")
    public Result<String> hello( @RequestBody Student student ) {
        LogUtils.info("请求参数： student对象：{}", student);
        return Result.success("success" + student.getBirthDay());
    }

//    @Operation(summary = "分页获取商品列表", description = "分页获取商品列表")
//    @RequestLogger("分页获取商品列表")
//    @NotAuth
//    @GetMapping(value = "/sku/page")
//    public Result<PageResult<Integer>> getSkuByPage(@Validated GoodsPageQuery goodsPageQuery) {
//        return Result.success(new PageResult<Integer>());
//    }

    /**
     * Page
     *
     * @author shuigedeng
     * @version 2026.01
     * @since 2025-12-19 09:30:45
     */
    @Data
    @Schema(description = "Page参数")
    public static class Page {

        @Schema(name = "pageSize", type = "integer", description = "当前页", requiredMode = Schema.RequiredMode.REQUIRED)
        private Integer pageSize;

        @Schema(name = "size", type = "integer", description = "每页数量", requiredMode = Schema.RequiredMode.REQUIRED)
        private Integer size;
    }

    /**
     * Student
     *
     * @author shuigedeng
     * @version 2026.01
     * @since 2025-12-19 09:30:45
     */
    @Data
    public static class Student {

        @NotEmpty(message = "名称不能为空")
        @Schema(name = "name", title = "名称")
        private String name;

        @Schema(name = "age", type = "integer", title = "年龄")
        private Integer age;

        @Schema(name = "birthDay", title = "生日")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime birthDay;

        @NotEmpty(message = "plays数据不能为空")
        @Schema(name = "plays", title = "plays数据")
        private List<String> plays;

    }

    // @ApiOperation(value = "获取自己的图片资源")
    // @GetMapping
    // @ApiImplicitParam(name = "title", value = "名称模糊匹配")
    // public ResultMessage<IPage<File>> getFileList(@RequestHeader String accessToken, File file,
    //	SearchVO searchVO, PageVO pageVo) {
    //
    //	AuthUser authUser = UserContext.getAuthUser(cache, accessToken);
    //	FileOwnerDTO fileOwnerDTO = new FileOwnerDTO();
    //	//只有买家才写入自己id
    //	if (authUser.getRole().equals(UserEnums.MEMBER)) {
    //		fileOwnerDTO.setOwnerId(authUser.getId());
    //	}//如果是店铺，则写入店铺id
    //	else if (authUser.getRole().equals(UserEnums.STORE)) {
    //		fileOwnerDTO.setOwnerId(authUser.getStoreId());
    //	}
    //	fileOwnerDTO.setUserEnums(authUser.getRole().name());
    //	return ResultUtil.data(fileService.customerPageOwner(fileOwnerDTO, file, searchVO, pageVo));
    // }
    //
    // @ApiOperation(value = "文件重命名")
    // @PostMapping(value = "/rename")
    // public ResultMessage<File> upload(@RequestHeader String accessToken, String id,
    //	String newName) {
    //	AuthUser authUser = UserContext.getAuthUser(cache, accessToken);
    //	File file = fileService.getById(id);
    //	file.setName(newName);
    //	//操作图片属性判定
    //	switch (authUser.getRole()) {
    //		case MEMBER:
    //			if (file.getOwnerId().equals(authUser.getId()) && file.getUserEnums()
    //				.equals(authUser.getRole().name())) {
    //				break;
    //			}
    //			throw new ServiceException(ResultCode.USER_AUTHORITY_ERROR);
    //		case STORE:
    //			if (file.getOwnerId().equals(authUser.getStoreId()) && file.getUserEnums()
    //				.equals(authUser.getRole().name())) {
    //				break;
    //			}
    //			throw new ServiceException(ResultCode.USER_AUTHORITY_ERROR);
    //		case MANAGER:
    //			break;
    //		default:
    //			throw new ServiceException(ResultCode.USER_AUTHORITY_ERROR);
    //	}
    //	fileService.updateById(file);
    //	return ResultUtil.data(file);
    // }
    //
    // @ApiOperation(value = "文件删除")
    // @DeleteMapping(value = "/delete/{ids}")
    // public ResultMessage delete(@RequestHeader String accessToken, @PathVariable List<String>
    // ids) {
    //
    //	AuthUser authUser = UserContext.getAuthUser(cache, accessToken);
    //	fileService.batchDelete(ids, authUser);
    //	return ResultUtil.success();
    // }

}
