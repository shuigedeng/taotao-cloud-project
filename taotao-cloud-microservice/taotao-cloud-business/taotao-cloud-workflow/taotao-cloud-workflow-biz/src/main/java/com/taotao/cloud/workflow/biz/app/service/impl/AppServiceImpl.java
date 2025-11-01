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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import taotao.cloud.workflow.biz.base.UserInfo;
import taotao.cloud.workflow.biz.model.AppPositionVO;
import taotao.cloud.workflow.biz.model.AppUserInfoVO;
import taotao.cloud.workflow.biz.model.AppUsersVO;
import taotao.cloud.workflow.biz.permission.*;
import taotao.cloud.workflow.biz.permission.entity.*;
import taotao.cloud.workflow.biz.service.AppService;
import taotao.cloud.workflow.biz.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * app用户信息
 *
 * @author 
 * 
 *  
 * @since 2021-08-08
 */
@Service
public class AppServiceImpl implements AppService {

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserApi userApi;

    @Autowired
    private PositionApi positionApi;

    @Autowired
    private OrganizeApi organizeApi;

    @Autowired
    private RoleApi roleApi;

    @Autowired
    private UserRelationApi userRelationApi;

    @Override
    public AppUsersVO userInfo() {
        UserInfo userInfo = userProvider.get();
        UserEntity userEntity = userApi.getInfoById(userInfo.getUserId());
        AppUsersVO usersVO = new AppUsersVO();
        usersVO.setBirthday(
                userEntity.getBirthday() != null ? userEntity.getBirthday().getTime() : null);
        usersVO.setEmail(userEntity.getEmail());
        usersVO.setGender(userEntity.getGender());
        usersVO.setMobilePhone(userEntity.getMobilePhone());
        this.data(usersVO, userEntity, userInfo);
        this.userInfo(usersVO, userInfo);
        // 岗位
        PositionEntity position = positionApi.queryInfoById(userEntity.getPositionId());
        AppPositionVO positionVO = new AppPositionVO();
        if (position != null) {
            positionVO.setId(position.getId());
            positionVO.setName(position.getFullName());
            usersVO.setPositionIds(ListUtil.toList(positionVO));
        }
        // 直属主管
        if (StringUtil.isNotEmpty(userEntity.getManagerId())) {
            UserEntity menager = userApi.getInfoById(userEntity.getManagerId());
            usersVO.setManager(menager != null ? menager.getRealName() + "/" + menager.getAccount() : "");
        }
        // 角色
        List<String> roles = roleApi.getAllRoleIdsByUserIdAndOrgId(userInfo.getUserId(), usersVO.getOrganizeId());
        List<RoleEntity> roleList = roleApi.getListByIds(roles);
        usersVO.setRoleName(
                String.join("，", roleList.stream().map(RoleEntity::getFullName).toList()));
        usersVO.setRoleId(
                String.join(".", roleList.stream().map(RoleEntity::getId).toList()));
        return usersVO;
    }

    @Override
    public AppUserInfoVO getInfo(String id) {
        AppUserInfoVO userInfoVO = new AppUserInfoVO();
        UserEntity entity = userApi.getInfoById(id);
        if (entity != null) {
            userInfoVO = JsonUtil.getJsonToBean(entity, AppUserInfoVO.class);
            List<String> positionIds = StringUtil.isNotEmpty(entity.getPositionId())
                    ? Arrays.asList(entity.getPositionId().split(","))
                    : new ArrayList<>();
            List<String> positionName = positionApi.getPositionName(positionIds).stream()
                    .map(t -> t.getFullName())
                    .toList();
            userInfoVO.setPositionName(String.join(",", positionName));
            OrganizeEntity info = organizeApi.getInfoById(entity.getOrganizeId());
            userInfoVO.setOrganizeName(info != null ? info.getFullName() : "");
            userInfoVO.setHeadIcon(UploaderUtil.uploaderImg(userInfoVO.getHeadIcon()));
        }
        return userInfoVO;
    }
    /**
     * 赋值
     *
     * @param userInfo
     * @param userId
     * @param isAdmin
     */
    private void userInfo(UserInfo userInfo, String userId, boolean isAdmin, UserEntity userEntity) {
        List<String> userIdList = new ArrayList() {
            {
                add(userId);
            }
        };
        List<UserRelationEntity> data = userRelationApi.getListByUserIdAll(userIdList);
        // 获取一个字段的值
        List<String> positionList = data.stream()
                .filter(m -> "Position".equals(m.getObjectType()))
                .map(t -> t.getObjectId())
                .toList();
        Set<String> id = new LinkedHashSet<>();
        String[] position = StringUtil.isNotEmpty(userEntity.getPositionId())
                ? userEntity.getPositionId().split(",")
                : new String[] {};
        List<String> positions = positionList.stream()
                .filter(t -> Arrays.asList(position).contains(t))
                .toList();
        id.addAll(positions);
        id.addAll(positionList);
        userInfo.setPositionIds(id.toArray(new String[id.size()]));
        if (!isAdmin) {
            data = data.stream().filter(m -> "Role".equals(m.getObjectType())).toList();
        }
        List<String> roleList = data.stream().map(t -> t.getObjectId()).toList();
        userInfo.setRoleIds(roleList);
    }

    private void data(AppUsersVO usersVO, UserEntity userEntity, UserInfo userInfo) {
        // 组织
        usersVO.setOrganizeId(userEntity.getOrganizeId());
        List<OrganizeEntity> organizeIdList = organizeApi.getOrganizeId(userEntity.getOrganizeId());
        Collections.reverse(organizeIdList);
        usersVO.setOrganizeName(
                organizeIdList.stream().map(OrganizeEntity::getFullName).collect(Collectors.joining("/")));
        OrganizeEntity organizeEntity = organizeIdList.stream()
                .filter(t -> t.getId().equals(userEntity.getOrganizeId()))
                .findFirst()
                .orElse(null);
        if (organizeEntity != null) {
            String[] organizeId = StringUtil.isNotEmpty(organizeEntity.getOrganizeIdTree())
                    ? organizeEntity.getOrganizeIdTree().split(",")
                    : new String[] {};
            if (organizeId.length > 0) {
                userInfo.setOrganizeId(organizeId[0]);
                userInfo.setDepartmentId(organizeId[organizeId.length - 1]);
            }
        }
        userInfo.setManagerId(userInfo.getManagerId());
        boolean b = userInfo.getIsAdministrator();
        List<String> subordinateIdsList = userApi.getListByManagerId(userInfo.getUserId()).stream()
                .map(UserEntity::getId)
                .toList();
        userInfo.setSubordinateIds(subordinateIdsList);
        this.userInfo(userInfo, userInfo.getUserId(), b, userEntity);
        userInfo.setSubOrganizeIds(new String[] {});
        redisUtil.insert(
                userInfo.getId(), userInfo, DateUtil.getTime(userInfo.getOverdueTime()) - DateUtil.getTime(new Date()));
    }

    /**
     * 登录信息
     *
     * @param appUsersVO 返回对象
     * @param userInfo 回话信息
     * @return
     */
    private void userInfo(AppUsersVO appUsersVO, UserInfo userInfo) {
        appUsersVO.setUserId(userInfo.getUserId());
        appUsersVO.setHeadIcon(UploaderUtil.uploaderImg(userInfo.getUserIcon()));
        appUsersVO.setUserName(userInfo.getUserName());
        appUsersVO.setUserAccount(userInfo.getUserAccount());
    }
}
