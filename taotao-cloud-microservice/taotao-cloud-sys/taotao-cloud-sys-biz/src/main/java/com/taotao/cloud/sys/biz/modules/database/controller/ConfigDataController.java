package com.taotao.cloud.sys.biz.modules.database.controller;

import com.sanri.tools.modules.database.service.ConfigDataService;
import com.sanri.tools.modules.database.service.meta.dtos.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * 用于支持像 nacos ,diamond 等阿里系统的分布式配置数据的读取,它们表结构基本都是一致的
 * 都是从数据库来存储数据
 */
@RestController
@RequestMapping("/db/data/config")
@Validated
public class ConfigDataController {
    @Autowired
    private ConfigDataService configDataService;

    @GetMapping("/groups")
    public List<String> groups(@NotNull String connName, Namespace namespace) throws SQLException, IOException {
        return configDataService.groups(connName,namespace);
    }

    @GetMapping("/dataIds")
    public List<String> dataIds(@NotNull String connName,Namespace namespace,@NotNull String groupId) throws SQLException, IOException {
        return configDataService.dataIds(connName,namespace,groupId);
    }

    @GetMapping("/content")
    public String content(@NotNull String connName,Namespace namespace,@NotNull String groupId,@NotNull String dataId) throws SQLException, IOException {
        return configDataService.content(connName,namespace,groupId,dataId);
    }
}
