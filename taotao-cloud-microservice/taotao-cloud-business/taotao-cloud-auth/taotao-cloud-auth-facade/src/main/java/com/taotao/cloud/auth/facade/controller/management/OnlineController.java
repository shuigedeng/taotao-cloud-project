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

package com.taotao.cloud.auth.facade.controller.management; // package com.taotao.cloud.auth.biz.controller;
//
// import java.io.IOException;
// import java.util.Set;
// import jakarta.servlet.http.HttpServletResponse;
// import org.springframework.data.domain.Pageable;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
//
/// **
// * OnlineController
// *
// * @author shuigedeng
// * @version 2021.10
// * @since 2022-02-24 11:21:34
// */
// @RestController
// @RequestMapping("/auth/online")
// @Api(tags = "系统：在线用户管理")
// public class OnlineController {
//
//    private final OnlineUserService onlineUserService;
//
//    public OnlineController(OnlineUserService onlineUserService) {
//        this.onlineUserService = onlineUserService;
//    }
//
//    @ApiOperation("查询在线用户")
//    @GetMapping
//    @PreAuthorize("@el.check()")
//    public ResponseEntity<Object> getAll(@RequestParam(value = "filter", defaultValue = "") String
// filter,
//                                         @RequestParam(value = "type", defaultValue = "0") int
// type,
//                                         Pageable pageable) {
//        return new ResponseEntity<>(onlineUserService.getAll(filter, type, pageable),
// HttpStatus.OK);
//    }
//
//    @Log("导出数据")
//    @ApiOperation("导出数据")
//    @GetMapping(value = "/download")
//    @PreAuthorize("@el.check('user:add')")
//    public void download(HttpServletResponse response,
//                         @RequestParam(value = "filter", defaultValue = "") String filter,
//                         @RequestParam(value = "type", defaultValue = "0") int type) throws
// IOException {
//        onlineUserService.download(onlineUserService.getAll(filter, type), response);
//    }
//
//    @ForbidSubmit
//    @ApiOperation("踢出用户")
//    @DeleteMapping
//    @PreAuthorize("@el.check()")
//    public ResponseEntity<Object> delete(@RequestBody Set<String> keys) throws Exception {
//
//        for (String key : keys) {
//            onlineUserService.kickOut(key);
//        }
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @ForbidSubmit
//    @ApiOperation("踢出移动端用户")
//    @PostMapping("/delete")
//    @PreAuthorize("@el.check()")
//    public ResponseEntity<Object> deletet(@RequestBody Set<String> keys) throws Exception {
//
//        for (String key : keys) {
//            onlineUserService.kickOutT(key);
//        }
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
// }
