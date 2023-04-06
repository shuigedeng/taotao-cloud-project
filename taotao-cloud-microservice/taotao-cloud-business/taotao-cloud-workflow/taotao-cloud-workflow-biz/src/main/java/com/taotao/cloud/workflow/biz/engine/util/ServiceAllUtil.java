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

package com.taotao.cloud.workflow.biz.engine.util;

import com.taotao.cloud.workflow.api.vo.OrganizeEntity;
import com.taotao.cloud.workflow.api.vo.PositionEntity;
import com.taotao.cloud.workflow.api.vo.RoleEntity;
import com.taotao.cloud.workflow.api.vo.UserEntity;
import com.taotao.cloud.workflow.api.vo.UserRelationEntity;
import com.taotao.cloud.workflow.api.vo.entity.DictionaryDataEntity;
import com.taotao.cloud.workflow.biz.common.base.Pagination;
import com.taotao.cloud.workflow.biz.common.database.model.entity.DbLinkEntity;
import com.taotao.cloud.workflow.biz.common.util.StringUtil;
import com.taotao.cloud.workflow.biz.common.util.enums.DictionaryDataEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** */
@Component
public class ServiceAllUtil {

    @Autowired
    private DblinkService dblinkService;

    @Autowired
    private DbTableService dbTableService;

    @Autowired
    private DictionaryDataService dictionaryDataService;

    @Autowired
    private UserRelationService userRelationService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OrganizeService organizeService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private BillRuleService billRuleService;

    @Autowired
    private DataInterfaceService dataInterfaceService;

    // --------------------------------数据连接------------------------------
    public DbLinkEntity getDbLink(String dbLink) {
        return StringUtil.isNotEmpty(dbLink) ? dblinkService.getInfo(dbLink) : null;
    }

    public void createTable(DbTableCreate dbTable) throws DataException {
        dbTableService.createTable(dbTable);
    }

    // --------------------------------数据字典------------------------------
    public List<DictionaryDataEntity> getDiList() {
        return dictionaryDataService.getList(DictionaryDataEnum.FLOWWOEK_ENGINE.getDictionaryTypeId());
    }

    public List<DictionaryDataEntity> getDictionName(List<String> id) {
        return dictionaryDataService.getDictionName(id);
    }

    // --------------------------------用户关系表------------------------------
    public List<UserRelationEntity> getListByUserIdAll(List<String> id) {
        return userRelationService.getListByUserIdAll(id);
    }

    public List<UserRelationEntity> getListByObjectIdAll(List<String> id) {
        return userRelationService.getListByObjectIdAll(id);
    }

    // --------------------------------用户------------------------------
    public List<UserEntity> getUserName(List<Long> id) {
        return userService.getUserName(id);
    }

    public List<UserEntity> getUserName(List<Long> id, Pagination pagination) {
        return userService.getUserName(id, pagination);
    }

    public UserEntity getUserInfo(Long id) {
        return StringUtils.isNotEmpty(id) ? userService.getInfo(id) : null;
    }

    public UserEntity getByRealName(String realName) {
        return StringUtils.isNotEmpty(realName) ? userService.getByRealName(realName) : null;
    }

    // --------------------------------单据规则------------------------------
    public String getBillNumber(String enCode) {
        String billNo = "";
        try {
            billNo = billRuleService.getBillNumber(enCode, false);
        } catch (Exception ignored) {

        }
        return billNo;
    }

    // --------------------------------角色------------------------------
    public List<RoleEntity> getListByIds(List<String> id) {
        return roleService.getListByIds(id);
    }

    // --------------------------------组织------------------------------
    public List<OrganizeEntity> getOrganizeName(List<String> id) {
        return organizeService.getOrganizeName(id);
    }

    public OrganizeEntity getOrganizeInfo(String id) {
        return StringUtils.isNotEmpty(id) ? organizeService.getInfo(id) : null;
    }

    public OrganizeEntity getOrganizeFullName(String fullName) {
        return organizeService.getByFullName(fullName);
    }

    public List<OrganizeEntity> getOrganizeId(String organizeId) {
        List<OrganizeEntity> organizeList = new ArrayList<>();
        organizeService.getOrganizeId(organizeId, organizeList);
        return organizeList;
    }

    // --------------------------------岗位------------------------------
    public List<PositionEntity> getPositionName(List<String> id) {
        return positionService.getPositionName(id);
    }

    public PositionEntity getPositionFullName(String fullName) {
        return positionService.getByFullName(fullName);
    }

    public PositionEntity getPositionInfo(String id) {
        return StringUtils.isNotEmpty(id) ? positionService.getInfo(id) : null;
    }

    // --------------------------------远端------------------------------
    public void infoToId(String interId, Map<String, String> parameterMap) {
        dataInterfaceService.infoToId(interId, null, parameterMap);
    }
}
