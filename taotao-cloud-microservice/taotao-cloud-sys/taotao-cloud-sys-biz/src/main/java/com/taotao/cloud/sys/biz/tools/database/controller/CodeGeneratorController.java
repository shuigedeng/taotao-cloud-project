package com.taotao.cloud.sys.biz.tools.database.controller;

import com.taotao.cloud.sys.biz.tools.core.service.file.FileManager;
import com.taotao.cloud.sys.biz.tools.database.dtos.CodeGeneratorConfig;
import com.taotao.cloud.sys.biz.tools.database.dtos.CodeGeneratorParam;
import com.taotao.cloud.sys.biz.tools.database.dtos.JavaBeanBuildConfig;
import com.taotao.cloud.sys.biz.tools.database.dtos.MapperBuildConfig;
import com.taotao.cloud.sys.biz.tools.database.dtos.TemplateContent;
import com.taotao.cloud.sys.biz.tools.database.service.CodeGeneratorService;
import com.taotao.cloud.sys.biz.tools.database.service.PreviewCodeParam;
import com.taotao.cloud.sys.biz.tools.database.service.TemplateService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/db/code")
public class CodeGeneratorController {
    @Autowired
    private CodeGeneratorService codeGeneratorService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private FileManager fileManager;

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
     * 构建一个 mapper 文件
     * @param mapperBuildConfig 构建配置
     * @return
     * @throws InterruptedException
     * @throws SQLException
     * @throws IOException
     */
    @PostMapping("/build/mapper")
    public String buildMapper(@RequestBody @Valid MapperBuildConfig mapperBuildConfig) throws InterruptedException, SQLException, IOException {
        File file = codeGeneratorService.mapperBuild(mapperBuildConfig);
        Path path = fileManager.relativePath(file.toPath());
        return path.toString();
    }

    @PostMapping("/build/mybatisPlus")
    public String buildMybatisPlus(){

        return null;
    }

    /**
     * 代码生成配置
     * @param codeGeneratorConfig 代码生成配置参数
     * @return
     * @throws IOException
     * @throws SQLException
     * @throws InterruptedException
     */
    @PostMapping("/build/project")
    public String buildProject(@RequestBody @Valid CodeGeneratorConfig codeGeneratorConfig) throws IOException, SQLException, InterruptedException {
        File file = codeGeneratorService.projectBuild(codeGeneratorConfig);
        Path path = fileManager.relativePath(file.toPath());
        return path.toString();
    }

    /**
     * 所有的生成方案
     * @return
     */
    @GetMapping("/schemas")
    public List<String> schemas(){
        return templateService.schemas();
    }

    /**
     * 某一个方案引用的模板列表
     * @param schema 方案名
     * @return
     * @throws IOException
     */
    @GetMapping("/{schema}/templates")
    public List<String> schemaTemplates(@PathVariable("schema") String schema) throws IOException {
        return templateService.schemaTemplates(schema);
    }

    /**
     * 获取所有的模板列表
     * @return
     */
    @GetMapping("/templates")
    public List<String> templates(){
        return templateService.templates();
    }

    /**
     * 模板文件内容
     * @param template 模板名称
     * @return
     */
    @GetMapping("/{template}/content")
    public String templateContent(@PathVariable("template") String template) throws IOException {
        return templateService.content(template);
    }

    /**
     * 上传一个模板,相同模板直接覆盖,需要注意是否有相同模板
     * 文件名格式为: 模板名称.后缀.模板引擎
     * @param file 模板文件
     */
    @PostMapping("/template/upload")
    public void uploadTemplate(MultipartFile file) throws IOException {
        templateService.uploadTemplate(file);
    }

    /**
     * 重写模板或方案
     * @param name 模板名称
     * @param content 内容
     */
    @PostMapping("/override")
    public void override(@RequestBody @Valid TemplateContent templateContent) throws IOException {
        String name = templateContent.getName();
        String content = templateContent.getContent();
        templateService.writeContent(name,content);
    }

    /**
     * 使用模板生成代码的预览
     * @param previewCodeParam
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws TemplateException
     */
    @PostMapping("/template/code/preview")
    public String previewCode(@RequestBody @Valid PreviewCodeParam previewCodeParam) throws SQLException, IOException, TemplateException {
        return codeGeneratorService.previewCode(previewCodeParam);
    }

    @PostMapping("/template/code/generator")
    public String generator(@RequestBody @Valid CodeGeneratorParam codeGeneratorParam) throws SQLException, IOException, TemplateException {
        Path path = codeGeneratorService.codeGenerator(codeGeneratorParam);
        return path.toString();
    }
}
