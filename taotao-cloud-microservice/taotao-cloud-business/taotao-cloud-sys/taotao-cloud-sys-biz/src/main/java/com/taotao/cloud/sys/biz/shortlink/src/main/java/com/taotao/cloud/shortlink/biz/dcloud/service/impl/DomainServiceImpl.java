package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.dcloud.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.manager.DomainManager;
import net.xdclass.model.DomainDO;
import net.xdclass.service.DomainService;
import net.xdclass.vo.DomainVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 刘森飚
 * @since 2023-01-18
 */

@Service
@Slf4j
public class DomainServiceImpl implements DomainService {

    @Autowired
    private DomainManager domainManager;


    /**
     * 列举全部可用域名
     * @return
     */
    @Override
    public List<DomainVO> listAll() {
        Long accountNo = LoginInterceptor.threadLocal.get().getAccountNo();

        List<DomainDO> customDomainList = domainManager.listCustomDomain(accountNo);
        List<DomainDO> officialDomainList = domainManager.listOfficialDomain();

        customDomainList.addAll(officialDomainList);

        return customDomainList.stream().map(obj-> beanProcess(obj)).toList();
    }


    private DomainVO beanProcess(DomainDO domainDO){

        DomainVO domainVO = new DomainVO();

        BeanUtils.copyProperties(domainDO,domainVO);

        return domainVO;

    }

}
