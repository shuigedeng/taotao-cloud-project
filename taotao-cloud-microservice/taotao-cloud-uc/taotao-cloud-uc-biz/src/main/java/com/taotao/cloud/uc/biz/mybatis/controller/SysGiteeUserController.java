// package com.taotao.cloud.uc.biz.controller;
//
// import com.taotao.cloud.common.model.PageResult;
// import com.taotao.cloud.common.model.Result;
// import com.taotao.cloud.uc.biz.entity.SysGiteeUser;
// import com.taotao.cloud.uc.biz.service.ISysGiteeUserService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiImplicitParam;
// import io.swagger.annotations.ApiImplicitParams;
// import io.swagger.annotations.ApiOperation;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;
//
// import java.util.Map;
//
// /**
//  * gitee用户表
//  *
//  * @author taotao
//  * @date 2020-05-14 14:36:39
//  */
// @Slf4j
// @RestController
// @RequestMapping("/gitee")
// @Api(tags = "gitee用户API")
// public class SysGiteeUserController {
//     @Autowired
//     private ISysGiteeUserService sysGiteeUserService;
//
//     /**
//      * 列表
//      */
//     @ApiOperation(value = "查询列表")
//     @ApiImplicitParams({
//             @ApiImplicitParam(name = "page", value = "分页起始位置", required = true, dataType = "Integer"),
//             @ApiImplicitParam(name = "limit", value = "分页结束位置", required = true, dataType = "Integer")
//     })
//     @GetMapping
//     public PageResult<SysGiteeUser> list(@RequestParam Map<String, Object> params) {
//         return sysGiteeUserService.findList(params);
//     }
//
//     /**
//      * 查询
//      */
//     @ApiOperation(value = "查询")
//     @GetMapping("/{id}")
//     public Result<SysGiteeUser> findUserById(@PathVariable Long id) {
// //        SysGiteeUser model = sysGiteeUserService.getById(id);
//         return Result.succeed(null, "查询成功");
//     }
//
//     /**
//      * 新增or更新
//      */
//     @ApiOperation(value = "保存")
//     @PostMapping
//     public Result<Boolean> save(@RequestBody SysGiteeUser sysGiteeUser) {
//         Boolean result = sysGiteeUserService.add(sysGiteeUser);
//         return Result.succeed(result);
//     }
//
//     /**
//      * 删除
//      */
//     @ApiOperation(value = "删除")
//     @DeleteMapping("/{id}")
//     public Result<Boolean> delete(@PathVariable Long id) {
// //        sysGiteeUserService.removeById(id);
//         return Result.succeed(true);
//     }
// }
