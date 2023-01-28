package com.taotao.cloud.workflow.biz.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import jnpf.base.ModuleApi;
import jnpf.base.UserInfo;
import jnpf.base.entity.ModuleEntity;
import jnpf.base.model.module.ModuleModel;
import jnpf.base.vo.PaginationVO;
import jnpf.engine.FlowEngineApi;
import jnpf.engine.entity.FlowEngineEntity;
import jnpf.engine.model.flowengine.FlowAppPageModel;
import jnpf.engine.model.flowengine.FlowPagination;
import jnpf.entity.AppDataEntity;
import jnpf.mapper.AppDataMapper;
import jnpf.model.AppDataListAllVO;
import jnpf.model.AppFlowListAllVO;
import jnpf.model.UserMenuModel;
import jnpf.permission.AuthorizeApi;
import jnpf.permission.model.authorize.AuthorizeVO;
import jnpf.service.AppDataService;
import jnpf.util.JsonUtil;
import jnpf.util.RandomUtil;
import jnpf.util.UserProvider;
import jnpf.util.treeutil.SumTree;
import jnpf.util.treeutil.TreeDotUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * app常用数据
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021-08-08
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
        queryWrapper.lambda().eq(AppDataEntity::getObjectType, type).eq(AppDataEntity::getCreatorUserId, userInfo.getUserId());
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
        queryWrapper.lambda().eq(AppDataEntity::getObjectId, objectId).eq(AppDataEntity::getCreatorUserId, userInfo.getUserId());
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean isExistByObjectId(String objectId) {
        UserInfo userInfo = userProvider.get();
        QueryWrapper<AppDataEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AppDataEntity::getObjectId, objectId).eq(AppDataEntity::getCreatorUserId, userInfo.getUserId());
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
        List<String> objectId = dataList.stream().map(AppDataEntity::getObjectId).collect(Collectors.toList());
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
        List<ModuleEntity> menuList = moduleApi.getList().stream().filter(t -> "App".equals(t.getCategory()) && t.getEnabledMark() == 1).collect(Collectors.toList());
        List<UserMenuModel> list = new LinkedList<>();
        for (ModuleEntity module : menuList) {
            boolean count = buttonList.stream().filter(t -> t.getId().equals(module.getId())).count() > 0;
            UserMenuModel userMenuModel = JsonUtil.getJsonToBean(module, UserMenuModel.class);
            if (count) {
                boolean isData = dataList.stream().filter(t -> t.getObjectId().equals(module.getId())).count() > 0;
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
