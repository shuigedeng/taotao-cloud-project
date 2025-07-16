/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.generator.maku.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.generator.maku.common.page.PageResult;
import com.taotao.cloud.generator.maku.common.query.Query;
import com.taotao.cloud.generator.maku.common.service.impl.BaseServiceImpl;
import com.taotao.cloud.generator.maku.dao.ProjectModifyDao;
import com.taotao.cloud.generator.maku.entity.ProjectModifyEntity;
import com.taotao.cloud.generator.maku.service.ProjectModifyService;
import com.taotao.cloud.generator.maku.utils.ProjectUtils;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 项目名变更
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Service
@AllArgsConstructor
public class ProjectModifyServiceImpl extends BaseServiceImpl<ProjectModifyDao, ProjectModifyEntity>
        implements ProjectModifyService {

    @Override
    public PageResult<ProjectModifyEntity> page(Query query) {
        IPage<ProjectModifyEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public byte[] download(ProjectModifyEntity project) throws IOException {
        // 原项目根路径
        File srcRoot = new File(project.getProjectPath());

        // 临时项目根路径
        File destRoot = new File(ProjectUtils.getTmpDirPath(project.getModifyProjectName()));

        // 排除的文件
        List<String> exclusions = StrUtil.split(project.getExclusions(), ProjectUtils.SPLIT);

        // 获取替换规则
        Map<String, String> replaceMap = getReplaceMap(project);

        // 拷贝项目到新路径，并替换路径和文件名
        ProjectUtils.copyDirectory(srcRoot, destRoot, exclusions, replaceMap);

        // 需要替换的文件后缀
        List<String> suffixList = StrUtil.split(project.getModifySuffix(), ProjectUtils.SPLIT);

        // 替换文件内容数据
        ProjectUtils.contentFormat(destRoot, suffixList, replaceMap);

        // 生成zip文件
        File zipFile = ZipUtil.zip(destRoot);

        byte[] data = FileUtil.readBytes(zipFile);

        // 清空文件
        FileUtil.clean(destRoot.getParentFile().getParentFile());

        return data;
    }

    /**
     * 获取替换规则
     */
    private Map<String, String> getReplaceMap(ProjectModifyEntity project) {
        Map<String, String> map = new LinkedHashMap<>();

        // 项目路径替换
        String srcPath = "src/main/java/" + project.getProjectPackage().replaceAll("\\.", "/");
        String destPath =
                "src/main/java/" + project.getModifyProjectPackage().replaceAll("\\.", "/");
        map.put(srcPath, destPath);

        // 项目包名替换
        map.put(project.getProjectPackage(), project.getModifyProjectPackage());

        // 项目标识替换
        map.put(project.getProjectCode(), project.getModifyProjectCode());
        map.put(
                StrUtil.upperFirst(project.getProjectCode()),
                StrUtil.upperFirst(project.getModifyProjectCode()));

        return map;
    }

    @Override
    public boolean save(ProjectModifyEntity entity) {
        entity.setExclusions(ProjectUtils.EXCLUSIONS);
        entity.setModifySuffix(ProjectUtils.MODIFY_SUFFIX);
        entity.setCreateTime(new Date());
        return super.save(entity);
    }
}
