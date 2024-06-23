package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.dcloud.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.DomainTypeEnum;
import net.xdclass.manager.DomainManager;
import net.xdclass.mapper.DomainMapper;
import net.xdclass.model.DomainDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description
 * @Author:刘森飚
 **/

@Component
@Slf4j
public class DomainManagerImpl implements DomainManager {

    @Autowired
    private DomainMapper domainMapper;


    /**
     * 查找详情
     * @param id
     * @param accountNO
     * @return
     */
    @Override
    public DomainDO findById(Long id, Long accountNO) {
        return domainMapper.selectOne(new QueryWrapper<DomainDO>()
                .eq("id", id)
                .eq("account_no", accountNO));
    }


    /**
     * 查找详情
     * @param id
     * @param domainTypeEnum
     * @return
     */
    @Override
    public DomainDO findByDomainTypeAndID(Long id, DomainTypeEnum domainTypeEnum) {
        return domainMapper.selectOne(new QueryWrapper<DomainDO>()
                .eq("id", id)
                .eq("domain_type", domainTypeEnum.name()));
    }


    /**
     * 新增
     * @param domainDO
     * @return
     */
    @Override
    public int addDomain(DomainDO domainDO) {
        return domainMapper.insert(domainDO);
    }


    /**
     * 列举全部官方域名
     * @return
     */
    @Override
    public List<DomainDO> listOfficialDomain() {
        return domainMapper.selectList(new QueryWrapper<DomainDO>()
                .eq("domain_type", DomainTypeEnum.OFFICIAL.name()));
    }



    /**
     * 列举全部自定义域名
     * @return
     */
    @Override
    public List<DomainDO> listCustomDomain(Long accountNo) {
        return domainMapper.selectList(new QueryWrapper<DomainDO>()
                .eq("domain_type", DomainTypeEnum.CUSTOM.name())
                .eq("account_no", accountNo));
    }
}
