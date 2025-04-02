package com.taotao.cloud.generator.maku.config.template;

import lombok.Data;
import net.maku.generator.config.template.DeveloperInfo;
import net.maku.generator.config.template.ProjectInfo;
import net.maku.generator.config.template.TemplateInfo;

import java.util.List;

/**
 * 代码生成信息
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
public class GeneratorInfo {
    private ProjectInfo project;
    private DeveloperInfo developer;
    private List<TemplateInfo> templates;
}
