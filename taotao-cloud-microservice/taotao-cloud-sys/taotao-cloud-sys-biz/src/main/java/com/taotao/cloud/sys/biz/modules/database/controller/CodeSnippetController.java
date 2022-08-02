package com.taotao.cloud.sys.biz.modules.database.controller;

import com.sanri.tools.modules.core.service.file.FileManager;
import com.sanri.tools.modules.database.service.code.CodeGenerateService;
import com.sanri.tools.modules.database.service.code.CodeMybatisGenerateService;
import com.sanri.tools.modules.database.service.code.CodeSqlService;
import com.sanri.tools.modules.database.service.code.dtos.*;
import net.sf.jsqlparser.JSQLParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Set;

@RestController
@RequestMapping("/db/code")
public class CodeSnippetController {
    @Autowired
    private CodeGenerateService codeGeneratorService;
    @Autowired
    private FileManager fileManager;
    @Autowired
    private CodeMybatisGenerateService codeMybatisGenerateService;
    @Autowired
    private CodeSqlService codeSqlService;

    @GetMapping("/renameStrategies")
    public Set<String> renameStrategies(){
        return codeGeneratorService.renameStrategies();
    }

    /**
     * 构建一个 javaBean
     * @param javaBeanBuildConfig 配置参数
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @PostMapping("/build/javaBean")
    public String javaBeanBuild(@RequestBody @Valid JavaBeanBuildConfig javaBeanBuildConfig) throws IOException, SQLException {
        File file = codeGeneratorService.javaBeanBuild(javaBeanBuildConfig);
        Path path = fileManager.relativePath(file.toPath());
        return path.toString();
    }

    /**
     *  构建 mybatis mapper 方式
     * @param mapperBuildConfig 构建配置
     * @return
     * @throws InterruptedException
     * @throws SQLException
     * @throws IOException
     */
    @PostMapping("/build/mapper")
    public String buildMapper(@RequestBody @Valid MapperBuildConfig mapperBuildConfig) throws InterruptedException, SQLException, IOException {
        File file = codeMybatisGenerateService.mapperBuild(mapperBuildConfig);
        Path path = fileManager.relativePath(file.toPath());
        return path.toString();
    }

    /**
     *  使用 mybatis plus 方式构建
     * @param mybatisPlusBuildConfig
     * @return
     * @throws IOException
     */
    @PostMapping("/build/mybatisPlus")
    public String buildMybatisPlus(@RequestBody @Validated MybatisPlusBuildConfig mybatisPlusBuildConfig) throws IOException {
        final File file = codeMybatisGenerateService.mybatisPlusBuild(mybatisPlusBuildConfig);
        Path path = fileManager.relativePath(file.toPath());
        return path.toString();
    }

    /**
     * 通过 sql语句 生成代码
     * @param codeFromSqlParam
     * @return
     * @throws JSQLParserException
     * @throws SQLException
     * @throws IOException
     */
    @PostMapping("/generatorCodeFromSql")
    public String generatorCodeFromSql(@Validated @RequestBody CodeFromSqlParam codeFromSqlParam) throws JSQLParserException, SQLException, IOException {
        final File file = codeSqlService.generateCode(codeFromSqlParam);
        Path path = fileManager.relativePath(file.toPath());
        return path.toString();
    }
}
