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

package com.taotao.cloud.workflow.biz.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import taotao.cloud.workflow.biz.base.ModuleApi;
import taotao.cloud.workflow.biz.base.UserInfo;
import taotao.cloud.workflow.biz.base.entity.ModuleEntity;
import taotao.cloud.workflow.biz.base.model.module.ModuleModel;
import taotao.cloud.workflow.biz.base.vo.PaginationVO;
import taotao.cloud.workflow.biz.engine.FlowEngineApi;
import taotao.cloud.workflow.biz.engine.entity.FlowEngineEntity;
import taotao.cloud.workflow.biz.engine.model.flowengine.FlowAppPageModel;
import taotao.cloud.workflow.biz.engine.model.flowengine.FlowPagination;
import taotao.cloud.workflow.biz.entity.AppDataEntity;
import taotao.cloud.workflow.biz.mapper.AppDataMapper;
import taotao.cloud.workflow.biz.model.AppDataListAllVO;
import taotao.cloud.workflow.biz.model.AppFlowListAllVO;
import taotao.cloud.workflow.biz.model.UserMenuModel;
import taotao.cloud.workflow.biz.permission.AuthorizeApi;
import taotao.cloud.workflow.biz.permission.model.authorize.AuthorizeVO;
import taotao.cloud.workflow.biz.service.AppDataService;
import taotao.cloud.workflow.biz.util.JsonUtil;
import taotao.cloud.workflow.biz.util.RandomUtil;
import taotao.cloud.workflow.biz.util.UserProvider;
import taotao.cloud.workflow.biz.util.treeutil.SumTree;
import taotao.cloud.workflow.biz.util.treeutil.TreeDotUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * app常用数据
 *
 * @author 
 * 
 *  
 * @since 2021-08-08
 */
@Service
public class AppDataServiceImpl extends ServiceImpl<AppDataMapper, AppDataEntity> implements AppDataService {

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private ModuleApi moduleApi;

    @Autowired
    private AuthorizeApi authorizeApi;

    @Autowired
    private FlowEngineApi flowEngineApi;

    @Override
    public List<AppDataEntity> getList(String type) {
        UserInfo userInfo = userProvider.get();
        QueryWrapper<AppDataEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .lambda()
                .eq(AppDataEntity::getObjectType, type)
                .eq(AppDataEntity::getCreatorUserId, userInfo.getUserId());
        return this.list(queryWrapper);
    }

    @Override
    public List<AppDataEntity> getList() {
        QueryWrapper<AppDataEntity> queryWrapper = new QueryWrapper<>();
        return this.list(queryWrapper);
    }

    @Override
    public AppDataEntity getInfo(String objectId) {
        UserInfo userInfo = userProvider.get();
        QueryWrapper<AppDataEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .lambda()
                .eq(AppDataEntity::getObjectId, objectId)
                .eq(AppDataEntity::getCreatorUserId, userInfo.getUserId());
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean isExistByObjectId(String objectId) {
        UserInfo userInfo = userProvider.get();
        QueryWrapper<AppDataEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .lambda()
                .eq(AppDataEntity::getObjectId, objectId)
                .eq(AppDataEntity::getCreatorUserId, userInfo.getUserId());
        return this.count(queryWrapper) > 0 ? true : false;
    }

    @Override
    public void create(AppDataEntity entity) {
        UserInfo userInfo = userProvider.get();
        entity.setId(RandomUtil.uuId());
        entity.setCreatorUserId(userInfo.getUserId());
        entity.setCreatorTime(new Date());
        entity.setEnabledMark(1);
        this.save(entity);
    }

    @Override
    public void delete(AppDataEntity entity) {
        this.removeById(entity.getId());
    }

    @Override
    public void delete(String objectId) {
        QueryWrapper<AppDataEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AppDataEntity::getObjectId, objectId);
        this.remove(queryWrapper);
    }

    @Override
    public List<AppFlowListAllVO> getFlowList(FlowPagination pagination) {
        List<AppDataEntity> dataList = getList("1");
        List<String> objectId =
                dataList.stream().map(AppDataEntity::getObjectId).toList();
        FlowAppPageModel pageModel = flowEngineApi.getAppPageList(pagination);
        List<FlowEngineEntity> pageList = pageModel.getList();
        PaginationVO paginaModel = pageModel.getPaginationVO();
        List<AppFlowListAllVO> result = new ArrayList<>();
        for (FlowEngineEntity entity : pageList) {
            AppFlowListAllVO vo = JsonUtil.getJsonToBean(entity, AppFlowListAllVO.class);
            vo.setIsData(objectId.contains(vo.getId()));
            result.add(vo);
        }
        long records = paginaModel.getTotal() != null ? paginaModel.getTotal() : 0;
        return pagination.setData(result, records);
    }

    @Override
    public List<AppDataListAllVO> getDataList(String type) {
        List<AppDataEntity> dataList = getList(type);
        AuthorizeVO authorizeModel = authorizeApi.getAuthorize(true);
        List<ModuleModel> buttonList = authorizeModel.getModuleList();
        List<ModuleEntity> menuList = moduleApi.getList().stream()
                .filter(t -> "App".equals(t.getCategory()) && t.getEnabledMark() == 1)
                .toList();
        List<UserMenuModel> list = new LinkedList<>();
        for (ModuleEntity module : menuList) {
            boolean count = buttonList.stream()
                            .filter(t -> t.getId().equals(module.getId()))
                            .count()
                    > 0;
            UserMenuModel userMenuModel = JsonUtil.getJsonToBean(module, UserMenuModel.class);
            if (count) {
                boolean isData = dataList.stream()
                                .filter(t -> t.getObjectId().equals(module.getId()))
                                .count()
                        > 0;
                userMenuModel.setIsData(isData);
                list.add(userMenuModel);
            }
        }
        List<SumTree<UserMenuModel>> menuAll = TreeDotUtils.convertListToTreeDot(list);
        List<AppDataListAllVO> menuListAll = JsonUtil.getJsonToList(menuAll, AppDataListAllVO.class);
        List<AppDataListAllVO> data = new LinkedList<>();
        for (AppDataListAllVO appMenu : menuListAll) {
            if ("-1".equals(appMenu.getParentId())) {
                data.add(appMenu);
            }
        }
        return data;
    }
}
