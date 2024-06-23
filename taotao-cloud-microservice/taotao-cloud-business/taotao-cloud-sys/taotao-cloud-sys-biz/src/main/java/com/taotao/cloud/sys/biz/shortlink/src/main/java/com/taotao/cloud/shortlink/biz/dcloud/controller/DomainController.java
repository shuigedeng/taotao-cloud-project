package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.dcloud.controller;


import net.xdclass.service.DomainService;
import net.xdclass.util.JsonData;
import net.xdclass.vo.DomainVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 刘森飚
 * @since 2023-01-22
 */
@RestController
@RequestMapping("/api/domain/v1")
public class DomainController {

    @Autowired
    private DomainService domainService;

    @GetMapping("list")
    public JsonData listAll() {
        List<DomainVO> domainVOS = domainService.listAll();
        return JsonData.buildSuccess(domainVOS);
    }
}

