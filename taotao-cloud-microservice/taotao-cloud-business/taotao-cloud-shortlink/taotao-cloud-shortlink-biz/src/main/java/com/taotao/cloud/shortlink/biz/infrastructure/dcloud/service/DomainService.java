package com.taotao.cloud.shortlink.biz.infrastructure.dcloud.service;

import net.xdclass.vo.DomainVO;

import java.util.List;

public interface DomainService {


    /**
     * 列举全部可用域名
     * @return
     */
    List<DomainVO> listAll();

}
