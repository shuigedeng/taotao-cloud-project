package com.taotao.cloud.generator.biz.generator.service;

import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * GeneratorService
 *
 * @author zhengkai.blog.csdn.net
 */
public interface GeneratorService {

    String getTemplateConfig() throws IOException;

    public Map<String, String> getResultByParams(Map<String, Object> params) throws IOException, TemplateException;

}
