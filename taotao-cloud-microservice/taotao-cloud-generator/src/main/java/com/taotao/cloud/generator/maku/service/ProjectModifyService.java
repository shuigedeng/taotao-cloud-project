package com.taotao.cloud.generator.maku.service;

import com.taotao.cloud.generator.maku.common.page.PageResult;
import com.taotao.cloud.generator.maku.common.query.Query;
import com.taotao.cloud.generator.maku.common.service.BaseService;
import com.taotao.cloud.generator.maku.entity.ProjectModifyEntity;

import java.io.IOException;

/**
 * 项目名变更
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public interface ProjectModifyService extends BaseService<ProjectModifyEntity> {

    PageResult<ProjectModifyEntity> page(Query query);

    byte[] download(ProjectModifyEntity project) throws IOException;

}
