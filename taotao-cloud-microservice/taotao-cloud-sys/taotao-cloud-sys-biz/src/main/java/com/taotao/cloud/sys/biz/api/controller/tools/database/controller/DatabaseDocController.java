package com.taotao.cloud.sys.biz.api.controller.tools.database.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;

import javax.validation.constraints.NotNull;

import com.taotao.cloud.sys.biz.api.controller.tools.core.service.file.FileManager;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.MetaDocService;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.search.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/db/doc")
public class DatabaseDocController {

    @Autowired
    private MetaDocService metaDocService;
    @Autowired
    private FileManager fileManager;

    /**
     * 导出表信息, 根据搜索出来的表
     * @param connName 连接名称
     * @param catalog 数据库 catalog
     * @param schemas 数据库 schema 列表
     * @param keyword 关键字
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @GetMapping("/export")
    public String exportDoc(@NotNull String connName, SearchParam searchParam) throws Exception {
        final File generateDoc = metaDocService.metaDoc(connName, searchParam);
        Path path = fileManager.relativePath(generateDoc.toPath());
        return path.toString();
    }
}
