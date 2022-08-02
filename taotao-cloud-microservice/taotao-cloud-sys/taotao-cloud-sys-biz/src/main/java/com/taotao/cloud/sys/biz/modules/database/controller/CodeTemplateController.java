package com.taotao.cloud.sys.biz.modules.database.controller;

import com.sanri.tools.modules.core.dtos.DictDto;
import com.sanri.tools.modules.database.controller.dtos.TemplateContent;
import com.sanri.tools.modules.database.service.JdbcMetaService;
import com.sanri.tools.modules.database.service.TableSearchService;
import com.sanri.tools.modules.database.service.code.CodeTemplateService;
import com.sanri.tools.modules.database.service.code.dtos.CodeGeneratorParam;
import com.sanri.tools.modules.database.service.code.dtos.PreviewCodeParam;
import com.sanri.tools.modules.database.service.code.rename.RenameStrategy;
import com.sanri.tools.modules.database.service.dtos.meta.TableMeta;
import com.sanri.tools.modules.database.service.dtos.meta.TableMetaData;
import com.sanri.tools.modules.database.service.meta.dtos.ActualTableName;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.*;

/**
 * 模板与方案代码生成
 */
@RestController
@RequestMapping("/db/code")
public class CodeTemplateController {
    /** 使用模板生成代码 **/
    @Autowired
    private CodeTemplateService codeTemplateService;

    /**
     * 所有的生成方案
     * @return
     */
    @GetMapping("/schemas")
    public List<String> schemas(){
        return codeTemplateService.schemas();
    }

    /**
     * 某一个方案引用的模板列表
     * @param schema 方案名
     * @return
     * @throws IOException
     */
    @GetMapping("/{schema}/templates")
    public List<String> schemaTemplates(@PathVariable("schema") String schema) throws IOException {
        return codeTemplateService.schemaTemplates(schema);
    }

    /**
     * 获取所有的模板列表
     * @return
     */
    @GetMapping("/templates")
    public List<String> templates(){
        return codeTemplateService.templates();
    }

    /**
     * 模板示例列表
     * @return
     */
    @GetMapping("/template/examples")
    public List<DictDto<String>> examples() throws IOException {
        return codeTemplateService.templateExamples();
    }

    /**
     * 模板文件内容
     * @param template 模板名称
     * @return
     */
    @GetMapping("/{template}/content")
    public String templateContent(@PathVariable("template") String template) throws IOException {
        return codeTemplateService.content(template);
    }

    /**
     * 上传一个模板,相同模板直接覆盖,需要注意是否有相同模板
     * 文件名格式为: 模板名称.后缀.模板引擎
     * @param file 模板文件
     */
    @PostMapping("/template/upload")
    public void uploadTemplate(MultipartFile file) throws IOException {
        codeTemplateService.uploadTemplate(file);
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
        codeTemplateService.writeContent(name,content);
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
        return codeTemplateService.previewCode(previewCodeParam);
    }

    /**
     * 使用模板列表生成代码
     * @param codeGeneratorParam
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws TemplateException
     */
    @PostMapping("/template/code/generator")
    public String generator(@RequestBody @Valid CodeGeneratorParam codeGeneratorParam) throws SQLException, IOException, TemplateException {
        Path path = codeTemplateService.codeGenerator(codeGeneratorParam);
        return path.toString();
    }
}
