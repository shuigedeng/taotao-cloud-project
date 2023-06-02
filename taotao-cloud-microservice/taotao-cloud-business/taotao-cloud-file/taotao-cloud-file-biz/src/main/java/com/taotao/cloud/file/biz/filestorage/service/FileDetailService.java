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

package com.taotao.cloud.file.biz.filestorage.service;

import org.dromara.hutoolcore.bean.BeanUtil;
import org.dromara.hutoolcore.lang.Dict;
import org.dromara.hutoolcore.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.file.biz.filestorage.mapper.FileDetailMapper;
import com.taotao.cloud.file.biz.filestorage.model.FileDetail;
import com.taotao.cloud.oss.common.storage.FileInfo;
import com.taotao.cloud.oss.common.storage.recorder.FileRecorder;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

/** 用来将文件上传记录保存到数据库，这里使用了 MyBatis-Plus 和 Hutool 工具类 */
@Service
public class FileDetailService extends ServiceImpl<FileDetailMapper, FileDetail> implements FileRecorder {

    /** 保存文件信息到数据库 */
    @SneakyThrows
    @Override
    public boolean record(FileInfo info) {
        FileDetail detail = BeanUtil.copyProperties(info, FileDetail.class, "attr");

        // 这是手动获 取附加属性字典 并转成 json 字符串，方便存储在数据库中
        if (info.getAttr() != null) {
            detail.setAttr(new ObjectMapper().writeValueAsString(info.getAttr()));
        }
        boolean b = save(detail);
        if (b) {
            info.setId(detail.getId());
        }
        return b;
    }

    /** 根据 url 查询文件信息 */
    @SneakyThrows
    @Override
    public FileInfo getByUrl(String url) {
        FileDetail detail = getOne(new QueryWrapper<FileDetail>().eq(FileDetail.COL_URL, url));
        FileInfo info = BeanUtil.copyProperties(detail, FileInfo.class, "attr");

        // 这是手动获取数据库中的 json 字符串 并转成 附加属性字典，方便使用
        if (StrUtil.isNotBlank(detail.getAttr())) {
            info.setAttr(new ObjectMapper().readValue(detail.getAttr(), Dict.class));
        }
        return info;
    }

    /** 根据 url 删除文件信息 */
    @Override
    public boolean delete(String url) {
        remove(new QueryWrapper<FileDetail>().eq(FileDetail.COL_URL, url));
        return true;
    }
}
