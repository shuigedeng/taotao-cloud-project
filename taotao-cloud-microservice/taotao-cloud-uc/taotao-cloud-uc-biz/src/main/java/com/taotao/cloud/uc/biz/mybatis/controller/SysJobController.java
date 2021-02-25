// package com.taotao.cloud.uc.biz.controller;
//
//
// import com.baomidou.mybatisplus.core.metadata.IPage;
// import com.taotao.cloud.common.model.PageResult;
// import com.taotao.cloud.common.model.Result;
// import com.taotao.cloud.log.annotation.SysOperateLog;
// import com.taotao.cloud.uc.biz.entity.SysJob;
// import com.taotao.cloud.uc.biz.service.ISysJobService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.web.bind.annotation.*;
//
// import javax.servlet.http.HttpServletRequest;
// import java.util.Arrays;
// import java.util.List;
// import java.util.stream.Collectors;
//
// /**
//  * 岗位管理API
//  *
//  * @author dengtao
//  * @date 2020/4/30 11:25
//  */
// @RestController
// @RequestMapping("/job")
// @Api(value = "岗位管理API", tags = { "岗位管理API" })
// public class SysJobController {
//
//     @Autowired
//     private ISysJobService jobService;
//
//     @ApiOperation("获取岗位列表")
//     @GetMapping
//     @SysOperateLog(description = "获取岗位列表")
// //    @PreAuthorize("hasAuthority('sys:job:view')")
//     public PageResult<SysJob> getList(Integer page, Integer pageSize, @RequestParam(defaultValue = "") String jobName,
//                                       HttpServletRequest request) {
//         IPage<SysJob> result = jobService.selectJobList(page, pageSize, jobName);
//         return PageResult.succeed(result);
//     }
//
//     @ApiOperation("添加岗位")
//     @SysOperateLog(description = "添加岗位")
//     @PreAuthorize("hasAuthority('sys:job:add')")
//     @PostMapping
//     public Result<Boolean> add(@RequestBody SysJob sysJob) {
// //        return Result.succeed(jobService.save(sysJob));
//         return null;
//     }
//
//     @ApiOperation("根据id删除岗位")
//     @SysOperateLog(description = "根据id删除岗位")
//     @DeleteMapping("/{id}")
//     @PreAuthorize("hasAuthority('sys:job:delete')")
//     public Result<Boolean> delete(@PathVariable("id") Integer id) {
//         return Result.succeed(jobService.removeById(id));
//     }
//
//     @ApiOperation("批量删除岗位")
//     @SysOperateLog(description = "批量删除岗位")
//     @PreAuthorize("hasAuthority('sys:role:delete')")
//     @DeleteMapping("/batchDelete")
//     public Result<Boolean> deleteBatch(@RequestParam(name = "ids") String ids) {
//         List<Integer> listIds = Arrays.stream(ids.split(",")).map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
//         return Result.succeed(jobService.batchDeleteJobByIds(listIds));
//     }
//
//     @ApiOperation("查询字典详情集合")
//     @SysOperateLog(description = "更新岗位")
//     @PreAuthorize("hasAuthority('sys:job:update')")
//     @PutMapping
//     public Result<Boolean> update(@RequestBody SysJob sysJob) {
//         return Result.succeed(jobService.updateById(sysJob));
//     }
//
//     @ApiOperation("根据部门id查询所属下的岗位信息")
//     @SysOperateLog(description = "根据部门id查询所属下的岗位信息")
//     @GetMapping("/{id}")
//     public Result<List<SysJob>> selectJobListByDeptId(@PathVariable("id") Integer deptId) {
//         return Result.succeed(jobService.selectJobListByDeptId(deptId));
//     }
// }
//
