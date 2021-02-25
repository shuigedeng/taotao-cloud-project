// package com.taotao.cloud.uc.biz.controller;
//
//
// import com.baomidou.mybatisplus.core.toolkit.Wrappers;
// import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
// import com.taotao.cloud.common.model.PageResult;
// import com.taotao.cloud.common.model.Result;
// import com.taotao.cloud.log.annotation.SysOperateLog;
// import com.taotao.cloud.log.model.SysLog;
// import com.taotao.cloud.uc.biz.service.ISysLogService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.web.bind.annotation.*;
//
// import javax.annotation.Resource;
// import javax.servlet.http.HttpServletRequest;
//
// /**
//  * 系统日志管理API
//  *
//  * @author dengtao
//  * @date 2020/4/30 11:34
//  */
// @RestController
// @RequestMapping("/log")
// @Slf4j
// @Api(value = "系统日志管理API", tags = {"系统日志管理API"})
// public class SysLogController {
//
//     @Resource
//     private ISysLogService sysLogService;
//
//     @ApiOperation("分页查询日志列表")
//     @GetMapping
//     public PageResult<SysLog> page(Page page, SysLog sysLog) {
// //        Page result = sysLogService.page(page, Wrappers.query(sysLog).lambda().orderByDesc(SysLog::getStartTime));
//         return PageResult.succeed(null);
//     }
//
//     @ApiOperation("保存日志信息")
//     @PostMapping
//     public Result<Boolean> add(@RequestBody SysLog sysLog, HttpServletRequest request) {
//         log.info(sysLog.getUserName());
// //        return Result.succeed(sysLogService.save(sysLog));
//         return null;
//     }
//
//     @ApiOperation("根据id删除日志")
//     @SysOperateLog(description = "根据id删除日志")
//     @PreAuthorize("hasAuthority('sys:log:delete')")
//     @DeleteMapping("/{logId}")
//     public Result<Boolean> delete(@PathVariable("logId") Integer logId) {
// //        return Result.succeed(sysLogService.removeById(logId));
//         return null;
//     }
// }
//
