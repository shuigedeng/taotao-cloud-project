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

package com.taotao.cloud.wechat.biz.mp.service.material;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.mp.controller.admin.material.vo.MpMaterialPageReqVO;
import cn.iocoder.yudao.module.mp.controller.admin.material.vo.MpMaterialUploadNewsImageReqVO;
import cn.iocoder.yudao.module.mp.controller.admin.material.vo.MpMaterialUploadPermanentReqVO;
import cn.iocoder.yudao.module.mp.controller.admin.material.vo.MpMaterialUploadTemporaryReqVO;
import cn.iocoder.yudao.module.mp.dal.dataobject.material.MpMaterialDO;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import jakarta.validation.Valid;
import me.chanjar.weixin.common.api.WxConsts;

/**
 * 公众号素材 Service 接口
 *
 * @author 芋道源码
 */
public interface MpMaterialService {

    /**
     * 获得素材的 URL
     *
     * <p>该 URL 来自我们自己的文件服务器存储的 URL，不是公众号存储的 URL
     *
     * @param accountId 公众号账号编号
     * @param mediaId 公众号素材 id
     * @param type 文件类型 {@link WxConsts.MediaFileType}
     * @return 素材的 URL
     */
    String downloadMaterialUrl(Long accountId, String mediaId, String type);

    /**
     * 上传临时素材
     *
     * @param reqVO 请求
     * @return 素材
     * @throws IOException 文件操作发生异常
     */
    MpMaterialDO uploadTemporaryMaterial(@Valid MpMaterialUploadTemporaryReqVO reqVO) throws IOException;

    /**
     * 上传永久素材
     *
     * @param reqVO 请求
     * @return 素材
     * @throws IOException 文件操作发生异常
     */
    MpMaterialDO uploadPermanentMaterial(@Valid MpMaterialUploadPermanentReqVO reqVO) throws IOException;

    /**
     * 上传图文内容中的图片
     *
     * @param reqVO 上传请求
     * @return 图片地址
     */
    String uploadNewsImage(MpMaterialUploadNewsImageReqVO reqVO) throws IOException;

    /**
     * 获得素材分页
     *
     * @param pageReqVO 分页请求
     * @return 素材分页
     */
    PageResult<MpMaterialDO> getMaterialPage(MpMaterialPageReqVO pageReqVO);

    /**
     * 获得素材列表
     *
     * @param mediaIds 素材 mediaId 列表
     * @return 素材列表
     */
    List<MpMaterialDO> getMaterialListByMediaId(Collection<String> mediaIds);

    /**
     * 删除素材
     *
     * @param id 编号
     */
    void deleteMaterial(Long id);
}
