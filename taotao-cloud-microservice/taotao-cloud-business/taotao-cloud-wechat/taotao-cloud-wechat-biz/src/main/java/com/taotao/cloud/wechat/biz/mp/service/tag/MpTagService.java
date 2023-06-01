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

package com.taotao.cloud.wechat.biz.mp.service.tag;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.mp.controller.admin.tag.vo.MpTagCreateReqVO;
import cn.iocoder.yudao.module.mp.controller.admin.tag.vo.MpTagPageReqVO;
import cn.iocoder.yudao.module.mp.controller.admin.tag.vo.MpTagUpdateReqVO;
import cn.iocoder.yudao.module.mp.dal.dataobject.tag.MpTagDO;
import java.util.List;
import jakarta.validation.Valid;

/**
 * 公众号标签 Service 接口
 *
 * @author fengdan
 */
public interface MpTagService {

    /**
     * 创建公众号标签
     *
     * @param createReqVO 创建标签信息
     * @return 标签编号
     */
    Long createTag(@Valid MpTagCreateReqVO createReqVO);

    /**
     * 更新公众号标签
     *
     * @param updateReqVO 更新标签信息
     */
    void updateTag(@Valid MpTagUpdateReqVO updateReqVO);

    /**
     * 删除公众号标签
     *
     * @param id 编号
     */
    void deleteTag(Long id);

    /**
     * 获得公众号标签分页
     *
     * @param pageReqVO 分页查询
     * @return 公众号标签分页
     */
    PageResult<MpTagDO> getTagPage(MpTagPageReqVO pageReqVO);

    List<MpTagDO> getTagList();

    /**
     * 同步公众号标签
     *
     * @param accountId 公众号账号的编号
     */
    void syncTag(Long accountId);
}
