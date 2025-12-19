package com.taotao.cloud.sys.biz.milliondataexport.mybatis;

import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;

import org.mybatis.spring.SqlSessionTemplate;

/**
 * AuthorsService
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Service
public class AuthorsService {

    private final SqlSessionTemplate sqlSessionTemplate;
    private final MybatisTest.AuthorsMapper authorsMapper;

    public AuthorsService( SqlSessionTemplate sqlSessionTemplate, AuthorsMapper authorsMapper ) {
        this.sqlSessionTemplate = sqlSessionTemplate;
        this.authorsMapper = authorsMapper;
    }

    /**
     * stream读数据写文件方式
     */
    public void streamDownload( HttpServletResponse httpServletResponse )
            throws IOException {
        AuthorsExample authorsExample = new AuthorsExample();
        authorsExample.createCriteria();
        HashMap<String, Object> param = new HashMap<>();
        param.put("oredCriteria", authorsExample.getOredCriteria());
        param.put("orderByClause", authorsExample.getOrderByClause());
        MybatisTest.CustomResultHandler customResultHandler = new MybatisTest.CustomResultHandler(
                new MybatisTest.DownloadProcessor(httpServletResponse));
        sqlSessionTemplate.select(
                "com.alphathur.mysqlstreamingexport.mapper.AuthorsMapper.streamByExample", param, customResultHandler);
        httpServletResponse.getWriter().flush();
        httpServletResponse.getWriter().close();
    }

    /**
     * 传统下载方式
     */
    public void traditionDownload( HttpServletResponse httpServletResponse )
            throws IOException {
        AuthorsExample authorsExample = new AuthorsExample();
        authorsExample.createCriteria();
        List<Authors> authors = authorsMapper.selectByExample(authorsExample);
        MybatisTest.DownloadProcessor downloadProcessor = new MybatisTest.DownloadProcessor(httpServletResponse);
        authors.forEach(downloadProcessor::processData);
        httpServletResponse.getWriter().flush();
        httpServletResponse.getWriter().close();
    }
}
