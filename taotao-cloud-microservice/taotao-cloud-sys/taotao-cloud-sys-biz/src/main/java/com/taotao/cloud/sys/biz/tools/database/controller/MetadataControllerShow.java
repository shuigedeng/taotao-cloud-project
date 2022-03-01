package com.taotao.cloud.sys.biz.tools.database.controller;

import com.taotao.cloud.sys.biz.tools.core.service.file.FileManager;
import com.taotao.cloud.sys.biz.tools.database.service.ExcelDocService;
import com.taotao.cloud.sys.biz.tools.database.service.JdbcService;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.TableMetaData;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 使用 freemarker 工具, 将数据库文档转成 html word
 */
@Controller
@RequestMapping("/db/metadata")
@Validated
public class MetadataControllerShow {
    @Autowired
    private JdbcService jdbcService;
    @Autowired
    private Configuration configuration;
    @Autowired
    private FileManager fileManager;

    /**
     * 生成数据库文档
     * @param catalog  数据库 catalog
     * @param connName  连接名称
     * @param schemas  数据库 schema
     * @param templateName  需要生成的模板文件名称
     * @return
     */
    @GetMapping("/doc")
    public ModelAndView generateDoc(@NotNull String connName, String catalog, String[] schemas, @NotNull String templateName) throws IOException, SQLException {
        Set<String> schemasSet = Arrays.stream(schemas).collect(Collectors.toSet());
        List<TableMetaData> filterTables = jdbcService.filterSchemaTables(connName,catalog,schemasSet);
        // 使用过滤后的表生成文档
        ModelAndView modelAndView = new ModelAndView(templateName);
        modelAndView.addObject("connName",connName);
        modelAndView.addObject("catalog",catalog);
        modelAndView.addObject("schema",schemas);
        modelAndView.addObject("tables",filterTables);
        return modelAndView;
    }

    @GetMapping("/doc/download")
    public void downDoc(@NotNull String connName, String catalog, String[] schemas,@NotNull String templateName, HttpServletResponse response) throws IOException, SQLException, TemplateException {
        Set<String> schemasSet = Arrays.stream(schemas).collect(Collectors.toSet());
        List<TableMetaData> filterTables = jdbcService.filterSchemaTables(connName, catalog, schemasSet);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream; charset=utf-8");
        String extension = FilenameUtils.getExtension(templateName);
        response.setHeader("Content-Disposition","attachment; filename=" + new String((connName+"|"+catalog+"|"+ StringUtils.join(schemas,'_') +"."+extension).getBytes(), "ISO8859-1"));

        Template template = configuration.getTemplate(templateName+".ftl");
        Map<String,Object> model = new HashMap<>();
        model.put("connName",connName);
        model.put("catalog",catalog);
        model.put("schema",schemas);
        model.put("tables",filterTables);
        template.process(model,response.getWriter());
    }

    /**
     * 先处理成 html 文档,然后转成 word 文档
     * @param connName
     * @param catalog
     * @param schema
     * @param templateName
     * @return
     * @throws IOException
     * @throws SQLException
     * @throws TemplateException
     */
    @GetMapping("/doc/download/word")
    public ResponseEntity<UrlResource> downDocWord(@NotNull String connName, String catalog, String[] schemas,@NotNull String templateName) throws IOException, SQLException, TemplateException {
        Set<String> schemasSet = Arrays.stream(schemas).collect(Collectors.toSet());
        List<TableMetaData> filterTables = jdbcService.filterSchemaTables(connName, catalog, schemasSet);

        File databaseDocDir = fileManager.mkTmpDir("/database/doc");
        File htmlFile = new File(databaseDocDir,System.currentTimeMillis() + ".html");
        FileOutputStream fileOutputStream = new FileOutputStream(htmlFile);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);

        Template template = configuration.getTemplate(templateName+".ftl");
        Map<String,Object> model = new HashMap<>();
        model.put("connName",connName);
        model.put("catalog",catalog);
        model.put("schema",schemas);
        model.put("tables",filterTables);
        template.process(model,outputStreamWriter);
        outputStreamWriter.flush();outputStreamWriter.close();

        // 转换 html 成 word
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem();
        DirectoryEntry directory = poifsFileSystem.getRoot();
        //WordDocument名称不允许修改
        FileInputStream fileInputStream = new FileInputStream(htmlFile);
        directory.createDocument("WordDocument", fileInputStream);

        // 然后写到文件
        File docFile = new File(databaseDocDir,System.currentTimeMillis()+".doc");
        FileOutputStream docFileOutputStream = new FileOutputStream(docFile);
        poifsFileSystem.writeFilesystem(docFileOutputStream);
        docFileOutputStream.flush();docFileOutputStream.close();

        fileInputStream.close();

        UrlResource urlResource = new UrlResource(docFile.toURI());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", docFile.getName());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ResponseEntity<UrlResource> body = ResponseEntity.ok().headers(headers)
                .contentLength(docFile.length()).body(urlResource);
        return body;
    }

    @Autowired
    private ExcelDocService excelDocService;

    @PostMapping("/generate")
    @ResponseBody
    public String generate(@NotNull String connName,String catalog,String [] schemas) throws IOException, SQLException {
        Path generate = excelDocService.generate(connName,catalog,schemas);
        return generate.toString();
    }
}
