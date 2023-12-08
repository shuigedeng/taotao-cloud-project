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
import taotao.cloud.workflow.biz.base.ActionResult;
import taotao.cloud.workflow.biz.base.ModuleApi;
import taotao.cloud.workflow.biz.base.Page;
import taotao.cloud.workflow.biz.base.model.module.ModuleModel;
import taotao.cloud.workflow.biz.base.vo.ListVO;
import taotao.cloud.workflow.biz.model.AppMenuListVO;
import taotao.cloud.workflow.biz.model.UserMenuModel;
import taotao.cloud.workflow.biz.permission.AuthorizeApi;
import taotao.cloud.workflow.biz.permission.model.authorize.AuthorizeVO;
import taotao.cloud.workflow.biz.util.JsonUtil;
import taotao.cloud.workflow.biz.util.StringUtil;
import taotao.cloud.workflow.biz.util.treeutil.ListToTreeUtil;
import taotao.cloud.workflow.biz.util.treeutil.SumTree;
import taotao.cloud.workflow.biz.util.treeutil.newtreeutil.TreeDotUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * app应用
 *
 * @author 
 * 
 * 
 * @since 2021-07-08
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
