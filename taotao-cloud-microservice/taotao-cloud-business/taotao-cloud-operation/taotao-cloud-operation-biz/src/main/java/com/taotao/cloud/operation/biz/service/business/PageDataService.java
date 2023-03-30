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

package com.taotao.cloud.operation.biz.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.operation.api.model.dto.PageDataDTO;
import com.taotao.cloud.operation.api.model.vo.PageDataListVO;
import com.taotao.cloud.operation.api.model.vo.PageDataVO;
import com.taotao.cloud.operation.biz.model.entity.PageData;

/**
 * 页面业务层
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-02 15:06:05
 */
public interface PageDataService extends IService<PageData> {

    /**
     * 添加店铺页面 用于初次开店，生成店铺首页
     *
     * @param storeId 店铺ID
     * @since 2022-06-02 15:06:05
     */
    void addStorePageData(String storeId);

    /**
     * 添加页面
     *
     * @param pageData 页面
     * @return {@link PageData }
     * @since 2022-06-02 15:06:05
     */
    PageData addPageData(PageData pageData);

    /**
     * 修改页面
     *
     * @param pageData 页面
     * @return {@link PageData }
     * @since 2022-06-02 15:06:05
     */
    PageData updatePageData(PageData pageData);

    /**
     * 发布页面
     *
     * @param id 页面ID
     * @return {@link PageData }
     * @since 2022-06-02 15:06:05
     */
    PageData releasePageData(String id);

    /**
     * 删除页面
     *
     * @param id 页面ID
     * @return boolean
     * @since 2022-06-02 15:06:05
     */
    boolean removePageData(String id);

    /**
     * 获取页面 用户前台页面展示
     *
     * @param pageDataDTO 页面数据DTO
     * @return {@link PageDataVO }
     * @since 2022-06-02 15:06:05
     */
    PageDataVO getPageData(PageDataDTO pageDataDTO);

    /**
     * 页面分页
     *
     * @param pageVO 分页
     * @param pageDataDTO 查询数据
     * @return {@link IPage }<{@link PageDataListVO }>
     * @since 2022-06-02 15:06:05
     */
    IPage<PageDataListVO> getPageDataList(PageVO pageVO, PageDataDTO pageDataDTO);
}
