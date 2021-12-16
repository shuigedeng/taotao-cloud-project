/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.system.api.dto.PictureDto;
import com.taotao.cloud.system.api.dto.PictureQueryCriteria;
import com.taotao.cloud.system.biz.entity.Picture;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PictureService extends IService<Picture> {

    /**
     * 查询数据分页
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(PictureQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     * @param criteria 条件参数
     * @return List<PictureDto>
     */
    List<Picture> queryAll(PictureQueryCriteria criteria);

    /**
     * 导出数据
     * @param all 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<PictureDto> all, HttpServletResponse response) throws IOException;


    /**
     * 上传文件
     * @param file /
     * @param username /
     * @return /
     */
    Picture upload(MultipartFile file, String username);

    /**
     * 根据ID查询
     * @param id /
     * @return /
     */
    Picture findById(Long id);

    /**
     * 多选删除
     * @param ids /
     */
    void deleteAll(Long[] ids);


    /**
     * 同步数据
     */
    void synchronize();
}
