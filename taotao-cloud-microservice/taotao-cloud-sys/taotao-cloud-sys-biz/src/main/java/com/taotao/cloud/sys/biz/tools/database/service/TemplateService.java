package com.taotao.cloud.sys.biz.tools.database.service;

import com.taotao.cloud.sys.biz.tools.core.service.file.FileManager;
import com.taotao.cloud.sys.biz.tools.database.dtos.CodeGeneratorConfig;
import com.taotao.cloud.sys.biz.tools.database.dtos.CodeGeneratorParam;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.ActualTableName;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.TableMetaData;
import com.taotao.cloud.sys.biz.tools.database.service.rename.JavaBeanInfo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 代码生成的模板管理
 * 分为单个模板管理和方案管理
 * 方案表示为组合多个模板
 */
@Service
public class TemplateService {
    @Autowired
    private FileManager fileManager;
    @Autowired
    private Configuration configuration;

    private FreeMarkerTemplate freeMarkerTemplate = new FreeMarkerTemplate();

    // 数据都存储在这个路径下,使用后缀来区分是方案还是模板 *.schema 是方案 , *.ftl 是 freemarker 模板 ,*.vm 是 velocity 模板
    private static final String basePath = "code/templates";
    private static final String[] SCHEMA_EXTENSION = {"schema"};
    private static final String[] TEMPLATE_EXTENSION = {"ftl","vm"};

    /** 方案,模板的增删改查 **/
    public List<String> schemas(){
        File dir = fileManager.mkConfigDir(basePath);
        Collection<File> files = FileUtils.listFiles(dir, SCHEMA_EXTENSION, false);
        List<String> collect = files.stream().map(File::getName).collect(Collectors.toList());
        return collect;
    }
    public List<String> templates(){
        File dir = fileManager.mkConfigDir(basePath);
        Collection<File> files = FileUtils.listFiles(dir, TEMPLATE_EXTENSION, false);
        List<String> collect = files.stream().map(File::getName).collect(Collectors.toList());
        return collect;
    }
    // 模板或者方案内容
    public String content(String name) throws IOException {
        File dir = fileManager.mkConfigDir(basePath);
        return FileUtils.readFileToString(new File(dir, name), StandardCharsets.UTF_8);
    }
    // 方案依赖的模板列表
    public List<String> schemaTemplates(String name) throws IOException {
        File dir = fileManager.mkConfigDir(basePath);
        return FileUtils.readLines(new File(dir,name), StandardCharsets.UTF_8);
    }
    // 写入模板或方案 最终于生成的模板名称是这样子的 真实名.扩展名.时间戳.模板类型;  如果为新加的,则为 真实名.扩展名.模板类型
    public void writeContent(String name,String content) throws IOException {
        File dir = fileManager.mkConfigDir(basePath);
        String fileName = trueFileName(name);

        File file = new File(dir, fileName);
        FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8);
    }

    // 上传一个模板
    public void uploadTemplate(MultipartFile file) throws IOException {
        File dir = fileManager.mkConfigDir(basePath);
        String name = file.getOriginalFilename();
        String fileName = trueFileName(name);

        File templateFile = new File(dir, fileName);
//        file.transferTo(templateFile);
        FileCopyUtils.copy(file.getInputStream(),new FileOutputStream(templateFile));
    }

    private String trueFileName(String name) {
        String extension = FilenameUtils.getExtension(name);
        if ("schema".equals(extension)){
            // 方案是唯一的,直接覆盖,不用加时间戳
            return name;
        }
        String[] split = StringUtils.split(name, '.');
        String fileName = name;
        if (split.length == 3){
            // 新加一个模板
            fileName = StringUtils.join(Arrays.asList(split[0], split[1], System.currentTimeMillis(), split[2]), '.');
        }
        return fileName;
    }

    /**
     * 根据元数据和模板解析模板文件
     * @param currentTable
     * @param renameStrategy
     * @return
     */
    public String preview(PreviewCodeParam previewCodeParam, TableMetaData currentTable, RenameStrategy renameStrategy) throws IOException, TemplateException {
        ActualTableName actualTableName = currentTable.getActualTableName();
        Map<String, Object> context = new HashMap<>();
        commonTemplateData(context);
        JavaBeanInfo mapping = renameStrategy.mapping(currentTable);
        context.put("table",currentTable);
        context.put("bean",mapping);
        context.put("codeConfig",previewCodeParam);

        String templateName = previewCodeParam.getTemplate();
        String content = content(templateName);
        Template template = new Template(FilenameUtils.getBaseName(templateName), content, configuration);
        return freeMarkerTemplate.process(template,context);
    }

    private void commonTemplateData(Map<String, Object> context) {
        context.put("date", DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(System.currentTimeMillis()));
        context.put("time", DateFormatUtils.ISO_8601_EXTENDED_TIME_FORMAT.format(System.currentTimeMillis()));
        context.put("author", System.getProperty("user.name"));
    }

    /**
     * 根据需要的表, 生成模板代码
     * @param renameStrategy
     * @param filterTables
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public File processBatch(CodeGeneratorParam codeGeneratorParam,List<TableMetaData> filterTables,RenameStrategy renameStrategy) throws IOException, TemplateException {
        List<String> templateNames = codeGeneratorParam.getTemplates();
        List<Template> templates = new ArrayList<>(templateNames.size());
        for (String templateName : templateNames) {
            String content = content(templateName);
            Template template = new Template(FilenameUtils.getBaseName(templateName), content, configuration);
            templates.add(template);
        }

        // 获取生成文件的临时目录
        File dir = fileManager.mkTmpDir("code/generator");
        File generatorDir = new File(dir,System.currentTimeMillis()+"");

        CodeGeneratorConfig.PackageConfig packageConfig = codeGeneratorParam.getPackageConfig();
        boolean single = codeGeneratorParam.isSingle();
        for (Template template : templates) {
            if (single){
                Map<String, Object> context = new HashMap<>();
                context.put("tables",filterTables);
                String process = freeMarkerTemplate.process(template, context);
                String fileName = StringUtils.capitalize(FilenameUtils.getBaseName(template.getName()));
                File file = new File(generatorDir, fileName);
                FileUtils.writeStringToFile(file, process, StandardCharsets.UTF_8);
            }else {
                for (TableMetaData filterTable : filterTables) {
                    ActualTableName actualTableName = filterTable.getActualTableName();
                    Map<String, Object> context = new HashMap<>();
                    context.put("table", filterTable);
                    JavaBeanInfo mapping = renameStrategy.mapping(filterTable);
                    context.put("mapping", mapping);
                    commonTemplateData(context);
                    context.put("package", packageConfig);
                    String process = freeMarkerTemplate.process(template, context);

                    // 写入目标文件,文件名规则为 类名+模板名(mapper.xml.时间戳)
                    String className = mapping.getClassName();
                    String name = template.getName();
                    String fileNameSuffix = StringUtils.capitalize(FilenameUtils.getBaseName(name));

                    File file = new File(generatorDir, className + fileNameSuffix);
                    FileUtils.writeStringToFile(file, process, StandardCharsets.UTF_8);
                }
            }
        }

        return generatorDir;
    }

    private class FreeMarkerTemplate {
        private static final String extension = "ftl";

        public String process(Template template, Map<String,Object> context) throws IOException, TemplateException {
            StringBuilderWriter stringBuilderWriter = new StringBuilderWriter();
            template.process(context,stringBuilderWriter);
            return stringBuilderWriter.toString();
        }
    }
}
