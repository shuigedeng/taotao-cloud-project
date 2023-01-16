package com.taotao.cloud.sys.biz.controller.business.tools.mybtis;// package com.taotao.cloud.sys.biz.api.controller.tools.mybtis;
//
// import com.taotao.cloud.sys.biz.api.controller.tools.mybtis.dto.StatementIdInfo;
// import com.taotao.cloud.sys.biz.api.controller.tools.mybtis.dto.StatementInfo;
// import com.taotao.cloud.sys.biz.api.controller.tools.mybtis.service.MybatisDynamicCallService;
// import com.taotao.cloud.sys.biz.api.controller.tools.mybtis.service.MybatisService;
// import com.taotao.cloud.sys.biz.api.controller.tools.mybtis.service.MybatisXmlFileManager;
// import com.taotao.cloud.sys.biz.api.controller.tools.mybtis.dto.BoundSqlParam;
// import org.apache.commons.collections4.CollectionUtils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.validation.annotation.Validated;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;
// import org.springframework.web.multipart.MultipartHttpServletRequest;
// import org.springframework.web.multipart.commons.CommonsMultipartResolver;
//
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.validation.Valid;
// import java.io.IOException;
// import java.sql.SQLException;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Iterator;
// import java.util.List;
//
// @RestController
// @RequestMapping("/mybatis")
// @Validated
// public class MybatisController {
//     @Autowired
//     private MybatisService mybatisService;
//     @Autowired
//     private MybatisXmlFileManager mybatisXmlFileManager;
//     @Autowired
//     private MybatisDynamicCallService mybatisDynamicCallService;
//
//     /**
//      * 获取项目列表
//      * @return
//      */
//     @GetMapping("/projects")
//     public List<String> projects(){
//         return mybatisXmlFileManager.projects();
//     }
//
//     /**
//      * 获取项目文件
//      * @param project
//      * @return
//      */
//     @GetMapping("/{project}/files")
//     public List<String> projectFiles(@PathVariable("project") String project){
//         return mybatisXmlFileManager.projectXmlFiles(project);
//     }
//
//     /**
//      * 获取文件内容
//      * @param project
//      * @param fileName
//      * @return
//      * @throws IOException
//      */
//     @GetMapping("/{project}/{fileName}/content")
//     public String xmlFileContent(@PathVariable("project") String project,@PathVariable("fileName") String fileName) throws IOException {
//         return mybatisXmlFileManager.xmlFileContent(project,fileName);
//     }
//
//     /**
//      * 上传 xml 文件
//      * @param project
//      * @param request
//      */
//     @PostMapping("/{project}/uploadFiles")
//     public void uploadFiles(@PathVariable("project") String project, HttpServletRequest request) throws IOException {
//         CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
//         if(multipartResolver.isMultipart(request)) {
//             MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
//             final Iterator<String> fileNames = multiRequest.getFileNames();
//             List<MultipartFile> files = new ArrayList<>();
//             while (fileNames.hasNext()){
//                 final String fileName = fileNames.next();
//                 final MultipartFile file = multiRequest.getFile(fileName);
//                 files.add(file);
//             }
//
//             mybatisXmlFileManager.addXmlFiles(project,files);
//         }
//     }
//
//     /**
//      * 删除一些 xml 文件
//      * @param project
//      * @param fileNames
//      */
//     @PostMapping("/{project}/dropFiles")
//     public void dropFiles(@PathVariable("project") String project,String [] fileNames){
//         mybatisXmlFileManager.dropXmlFiles(project, Arrays.asList(fileNames));
//     }
//
//     /**
//      * 获取当前文件语句列表
//      * @param project
//      * @param fileName
//      * @param classLoaderName
//      * @return
//      */
//     @GetMapping("/{project}/{fileName}/statementInfo")
//     public StatementInfo statementInfo(@PathVariable("project") String project, @PathVariable("fileName") String fileName, String classLoaderName) throws IOException {
//         final List<StatementIdInfo> statementIdInfos = mybatisDynamicCallService.xmlFileStatementIds(project, fileName, classLoaderName);
//         if (CollectionUtils.isEmpty(statementIdInfos)){
//             return null;
//         }
//         final StatementIdInfo statementIdInfo = statementIdInfos.get(0);
//         final String id = statementIdInfo.getId();
//         final String namespace = id.substring(0, id.lastIndexOf('.'));
//         return new StatementInfo(namespace,statementIdInfos);
//     }
//
//     /**
//      * 填写参数,执行 statement ,得到绑定的 sql 语句
//      * @param boundSqlParam
//      * @return
//      * @throws ClassNotFoundException
//      * @throws IOException
//      * @throws SQLException
//      */
//     @PostMapping("/boundSql")
//     public String boundSql(@RequestBody @Valid BoundSqlParam boundSqlParam) throws ClassNotFoundException, IOException, SQLException {
// //        return mybatisService.boundSql(boundSqlParam);
//         return mybatisDynamicCallService.boundSql(boundSqlParam);
//     }
// }
