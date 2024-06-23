package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.dcloud.manager;

import net.xdclass.enums.DomainTypeEnum;
import net.xdclass.model.DomainDO;

import java.util.List;

public interface DomainManager {


    /**
     * 查找详情
     * @param id
     * @param accountNO
     * @return
     */
    DomainDO findById(Long id, Long accountNO);


    /**
     * 查找详情
     * @param id
     * @param domainTypeEnum
     * @return
     */
    DomainDO findByDomainTypeAndID(Long id, DomainTypeEnum domainTypeEnum);


    /**
     * 新增
     * @param domainDO
     * @return
     */
    int addDomain(DomainDO domainDO);


    /**
     * 列举全部官方域名
     * @return
     */
    List<DomainDO> listOfficialDomain();


    /**
     * 列举全部自定义域名
     * @return
     */
    List<DomainDO> listCustomDomain(Long accountNo);
}
