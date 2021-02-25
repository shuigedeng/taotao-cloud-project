// package com.taotao.cloud.uc.biz.controller;
//
// import com.taotao.cloud.common.model.Result;
// import com.taotao.cloud.uc.api.dto.RepeatCheckDTO;
// import com.taotao.cloud.uc.biz.service.ISysUserService;
// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiOperation;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
//
// /**
//  * 数据重复校验API
//  *
//  * @author dengtao
//  * @date 2020/4/30 11:44
//  */
// @RestController
// @RequestMapping("/repeat")
// @Api(value = "数据重复校验API", tags = { "数据重复校验API" })
// public class SysRepeatCheckController {
//
//     @Autowired
//     private ISysUserService userService;
//
//     @ApiOperation("校验数据是否在系统中是否存在")
//     @GetMapping("/check")
//     public Result<Boolean> doDuplicateCheck(@RequestParam(value = "fieldVal") String fieldVal,
//                                             @RequestParam(value = "dataId", required = false) Integer dataId) {
//         RepeatCheckDTO repeatCheckDTO = new RepeatCheckDTO(fieldVal, dataId);
//         return Result.succeed(userService.repeatCheck(repeatCheckDTO));
//     }
// }
