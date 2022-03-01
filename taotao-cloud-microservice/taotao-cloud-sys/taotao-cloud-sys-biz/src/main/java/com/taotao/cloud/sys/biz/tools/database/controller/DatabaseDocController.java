package com.taotao.cloud.sys.biz.tools.database.controller;

import com.taotao.cloud.sys.biz.tools.core.service.file.FileManager;
import com.taotao.cloud.sys.biz.tools.database.service.JdbcDocService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;

@RestController
@RequestMapping("/db/doc")
public class DatabaseDocController {

    @Autowired
    private JdbcDocService jdbcDocService;
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
    public String exportDoc(@NotNull String connName, String catalog, String[] schemas, String keyword) throws Exception {
        final File generateDoc = jdbcDocService.generateDoc(connName, catalog, schemas, keyword);
        Path path = fileManager.relativePath(generateDoc.toPath());
        return path.toString();
    }
}
