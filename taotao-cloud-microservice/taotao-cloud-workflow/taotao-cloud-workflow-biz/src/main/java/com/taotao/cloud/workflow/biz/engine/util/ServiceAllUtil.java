package com.taotao.cloud.workflow.biz.engine.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jnpf.base.Pagination;
import jnpf.base.entity.DictionaryDataEntity;
import jnpf.base.model.dbtable.DbTableCreate;
import jnpf.database.model.entity.DbLinkEntity;
import jnpf.exception.DataException;
import jnpf.util.StringUtil;
import jnpf.util.enums.DictionaryDataEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ：JNPF开发平台组
 * @version: V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date ：2022/4/9 13:28
 */
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

    //--------------------------------数据连接------------------------------
    public DbLinkEntity getDbLink(String dbLink) {
        DbLinkEntity link = StringUtil.isNotEmpty(dbLink) ? dblinkService.getInfo(dbLink) : null;
        return link;
    }

    public void createTable(DbTableCreate dbTable) throws DataException {
        dbTableService.createTable(dbTable);
    }

    //--------------------------------数据字典------------------------------
    public List<DictionaryDataEntity> getDiList() {
        List<DictionaryDataEntity> dictionList = dictionaryDataService.getList(DictionaryDataEnum.FLOWWOEK_ENGINE.getDictionaryTypeId());
        return dictionList;
    }

    public List<DictionaryDataEntity> getDictionName(List<String> id) {
        List<DictionaryDataEntity> dictionList = dictionaryDataService.getDictionName(id);
        return dictionList;
    }

    //--------------------------------用户关系表------------------------------
    public List<UserRelationEntity> getListByUserIdAll(List<String> id) {
        List<UserRelationEntity> list = userRelationService.getListByUserIdAll(id);
        return list;
    }

    public List<UserRelationEntity> getListByObjectIdAll(List<String> id) {
        List<UserRelationEntity> list = userRelationService.getListByObjectIdAll(id);
        return list;
    }

    //--------------------------------用户------------------------------
    public List<UserEntity> getUserName(List<String> id) {
        List<UserEntity> list = userService.getUserName(id);
        return list;
    }

    public List<UserEntity> getUserName(List<String> id, Pagination pagination) {
        List<UserEntity> list = userService.getUserName(id, pagination);
        return list;
    }

    public UserEntity getUserInfo(String id) {
        UserEntity entity = StringUtil.isNotEmpty(id) ? userService.getInfo(id) : null;
        return entity;
    }

    public UserEntity getByRealName(String realName) {
        UserEntity entity = StringUtil.isNotEmpty(realName) ? userService.getByRealName(realName) : null;
        return entity;
    }

    //--------------------------------单据规则------------------------------
    public String getBillNumber(String enCode) {
        String billNo = "";
        try {
            billNo = billRuleService.getBillNumber(enCode, false);
        } catch (Exception e) {

        }
        return billNo;
    }

    //--------------------------------角色------------------------------
    public List<RoleEntity> getListByIds(List<String> id) {
        List<RoleEntity> list = roleService.getListByIds(id);
        return list;
    }

    //--------------------------------组织------------------------------
    public List<OrganizeEntity> getOrganizeName(List<String> id) {
        List<OrganizeEntity> list = organizeService.getOrganizeName(id);
        return list;
    }

    public OrganizeEntity getOrganizeInfo(String id) {
        OrganizeEntity entity = StringUtil.isNotEmpty(id) ? organizeService.getInfo(id) : null;
        return entity;
    }

    public OrganizeEntity getOrganizeFullName(String fullName) {
        OrganizeEntity entity = organizeService.getByFullName(fullName);
        return entity;
    }

    public List<OrganizeEntity>  getOrganizeId(String organizeId) {
        List<OrganizeEntity> organizeList = new ArrayList<>();
        organizeService.getOrganizeId(organizeId, organizeList);
        return organizeList;
    }

    //--------------------------------岗位------------------------------
    public List<PositionEntity> getPositionName(List<String> id) {
        List<PositionEntity> list = positionService.getPositionName(id);
        return list;
    }

    public PositionEntity getPositionFullName(String fullName) {
        PositionEntity entity = positionService.getByFullName(fullName);
        return entity;
    }

    public PositionEntity getPositionInfo(String id) {
        PositionEntity entity = StringUtil.isNotEmpty(id) ? positionService.getInfo(id) : null;
        return entity;
    }

    //--------------------------------远端------------------------------
    public void infoToId(String interId, Map<String, String> parameterMap) {
        dataInterfaceService.infoToId(interId, null, parameterMap);
    }

}
