// package com.taotao.cloud.uc.biz.controller;
//
//
// import com.taotao.cloud.common.model.Result;
// import com.taotao.cloud.log.annotation.SysOperateLog;
// import com.taotao.cloud.uc.api.dto.DeptDTO;
// import com.taotao.cloud.uc.api.vo.SysDeptTreeVo;
// import com.taotao.cloud.uc.biz.entity.SysDept;
// import com.taotao.cloud.uc.biz.service.ISysDeptService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.web.bind.annotation.*;
//
// import java.util.Arrays;
// import java.util.List;
// import java.util.stream.Collectors;
//
// /**
//  * 部门管理API
//  *
//  * @author dengtao
//  * @date 2020/4/30 11:03
//  */
// @RestController
// @RequestMapping("/dept")
// @Api(value = "部门管理API", tags = {"部门管理API"})
// public class SysDeptController {
//
//     @Autowired
//     private ISysDeptService deptService;
//
//     @ApiOperation("添加部门信息")
//     @SysOperateLog(description = "添加部门信息")
//     @PostMapping(value = "/add")
//     @PreAuthorize("hasAuthority('sys:dept:add')")
//     public Result<Boolean> add(@RequestBody SysDept sysDept) {
// //        return Result.succeed(deptService.save(sysDept));
//         return null;
//     }
//
//     @ApiOperation("获取部门信息")
//     @GetMapping("/list")
//     @PreAuthorize("hasAuthority('sys:dept:view')")
//     public Result<List<SysDept>> getDeptList() {
//         return Result.succeed(deptService.selectDeptList());
//     }
//
//     @ApiOperation("获取部门树")
//     @GetMapping("/tree")
//     public Result<List<SysDeptTreeVo>> queryDepartTreeList() {
//         return Result.succeed(deptService.queryDepartTreeList());
//     }
//
//     @ApiOperation("更新部门信息")
//     @SysOperateLog(description = "更新部门信息")
//     @PutMapping(value = "/update")
//     @PreAuthorize("hasAuthority('sys:dept:update')")
//     public Result<Boolean> update(@RequestBody DeptDTO deptDto) {
//         return Result.succeed(deptService.updateDeptById(deptDto));
//     }
//
//     @ApiOperation("根据id删除部门信息")
//     @SysOperateLog(description = "根据id删除部门信息")
//     @PreAuthorize("hasAuthority('sys:dept:delete')")
//     @DeleteMapping("/{id}")
//     public Result<Boolean> delete(@PathVariable("id") Integer id) {
// //        return Result.succeed(deptService.removeById(id));
//         return null;
//     }
//
//     @ApiOperation("根据ids批量删除部门信息")
//     @SysOperateLog(description = "根据ids批量删除部门信息")
//     @PreAuthorize("hasAuthority('sys:dept:delete')")
//     @DeleteMapping("/batch/delete")
//     public Result<Boolean> deleteBatch(@RequestParam(name = "ids") String ids) {
//         List<Integer> listIds = Arrays.stream(ids.split(",")).map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
//         return Result.succeed(deptService.batchDeleteDeptByIds(listIds));
//     }
// }
//
