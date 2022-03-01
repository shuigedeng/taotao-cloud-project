package com.taotao.cloud.sys.biz.tools.mybatis.controller;

import com.taotao.cloud.sys.biz.tools.mybatis.dtos.BoundSqlParam;
import com.taotao.cloud.sys.biz.tools.mybatis.dtos.BoundSqlResponse;
import com.taotao.cloud.sys.biz.tools.mybatis.dtos.ProjectDto;
import com.taotao.cloud.sys.biz.tools.mybatis.service.MybatisService;
import org.apache.ibatis.mapping.ParameterMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/mybatis")
@Validated
public class MybatisController {
    @Autowired
    private MybatisService mybatisService;

    @GetMapping("/reload")
    public void reload() throws IOException {
        mybatisService.reload();
    }

    /**
     * 获取当前已经加载的项目
     * @return
     */
    @GetMapping("/projects")
    public List<ProjectDto> projects(){
        return mybatisService.projects();
    }

    /**
     * 上传 mapper 文件,需要指定类加载器名称
     * @param file
     * @param project
     * @param classloaderName
     * @throws IOException
     */
    @PostMapping("/uploadMapperFile")
    public void uploadMapperFile(MultipartFile file, @NotNull String project, @NotNull String classloaderName) throws IOException {
        mybatisService.newProjectFile(project,classloaderName,file);
    }

    /**
     * 获取所有的 bound sqlId
     * @param project
     * @return
     */
    @GetMapping("/statementIds")
    public List<String> statementIds(@NotNull String project){
        return mybatisService.statementIds(project);
    }

    /**
     * 获取某个 statement 语句需要填写的参数
     * @param project
     * @param statementId
     * @ignore
     * @return
     */
    @GetMapping("/statementParams")
    public List<ParameterMapping> statementParams(@NotNull String project, @NotNull String statementId){
        return mybatisService.statemenetParams(project,statementId);
    }

    /**
     * 填写参数,执行 statement ,得到绑定的 sql 语句
     * @param boundSqlParam
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws SQLException
     */
    @PostMapping("/boundSql")
    public BoundSqlResponse boundSql(@RequestBody @Valid BoundSqlParam boundSqlParam) throws ClassNotFoundException, IOException, SQLException {
        return mybatisService.boundSql(boundSqlParam);
    }
}
