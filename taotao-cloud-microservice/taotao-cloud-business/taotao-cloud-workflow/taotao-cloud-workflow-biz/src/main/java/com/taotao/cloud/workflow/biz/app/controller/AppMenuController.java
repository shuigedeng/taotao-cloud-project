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

package com.taotao.cloud.workflow.biz.app.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.stream.Collectors;
import jnpf.base.ActionResult;
import jnpf.base.ModuleApi;
import jnpf.base.Page;
import jnpf.base.model.module.ModuleModel;
import jnpf.base.vo.ListVO;
import jnpf.model.AppMenuListVO;
import jnpf.model.UserMenuModel;
import jnpf.permission.AuthorizeApi;
import jnpf.permission.model.authorize.AuthorizeVO;
import jnpf.util.JsonUtil;
import jnpf.util.StringUtil;
import jnpf.util.treeutil.ListToTreeUtil;
import jnpf.util.treeutil.SumTree;
import jnpf.util.treeutil.newtreeutil.TreeDotUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * app应用
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司（https://www.jnpfsoft.com）
 * @date 2021-07-08
 */
@Api(tags = "app应用", value = "Menu")
@RestController
@RequestMapping("/app/Menu")
public class AppMenuController {

    @Autowired
    private ModuleApi moduleApi;

    @Autowired
    private AuthorizeApi authorizeApi;

    /**
     * app应用
     *
     * @return
     */
    @ApiOperation("获取菜单列表")
    @GetMapping
    public ActionResult list(Page page) {
        AuthorizeVO authorizeModel = authorizeApi.getAuthorize(true);
        List<ModuleModel> buttonListAll = authorizeModel.getModuleList().stream()
                .filter(t -> "App".equals(t.getCategory()))
                .toList();
        List<ModuleModel> buttonList = buttonListAll;
        if (StringUtil.isNotEmpty(page.getKeyword())) {
            buttonList = buttonListAll.stream()
                    .filter(t -> t.getFullName().contains(page.getKeyword()))
                    .toList();
        }
        List<UserMenuModel> list =
                JsonUtil.getJsonToList(ListToTreeUtil.treeWhere(buttonList, buttonListAll), UserMenuModel.class);
        List<SumTree<UserMenuModel>> menuAll = TreeDotUtils.convertListToTreeDot(list, "-1");
        List<AppMenuListVO> data = JsonUtil.getJsonToList(menuAll, AppMenuListVO.class);
        ListVO listVO = new ListVO();
        listVO.setList(data);
        return ActionResult.success(listVO);
    }
}
