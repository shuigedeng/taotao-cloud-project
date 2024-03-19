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

package com.taotao.cloud.sys.biz.service.business;

import com.taotao.cloud.sys.biz.model.vo.region.RegionParentVO;
import com.taotao.cloud.sys.biz.model.vo.region.RegionTreeVO;
import com.taotao.cloud.sys.biz.model.vo.region.RegionVO;
import com.taotao.cloud.sys.biz.model.entity.region.Region;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.util.List;
import java.util.Map;

/**
 * SysRegionService
 *
 * @author shuigedeng
 * @version 2022.08
 * @since 2022-08-10 10:24:46
 */
public interface IRegionService extends BaseSuperService<Region, Long> {

    /**
     * queryRegionByParentId
     *
     * @param parentId parentId
     * @return {@link List }<{@link RegionParentVO }>
     * @since 2022-08-10 10:29:30
     */
    List<RegionParentVO> queryRegionByParentId(Long parentId);

    /**
     * tree
     *
     * @param parentId 父id
     * @param depth 深度
     * @return {@link List }<{@link RegionParentVO }>
     * @since 2022-08-10 10:25:48
     */
    List<RegionParentVO> tree(Long parentId, Integer depth);

    /**
     * 得到所有城市
     *
     * @return {@link List }<{@link RegionVO }>
     * @since 2022-08-10 10:24:59
     */
    List<RegionVO> getAllCity();

    /**
     * 同步数据
     *
     * @param url url
     * @since 2022-08-10 10:26:03
     */
    void synchronizationData(String url);

    List<RegionTreeVO> treeOther();

    List<Region> getItem(Long parentId);

    /**
     * 得到区域
     *
     * @param cityCode 城市代码
     * @param townName 小镇名字
     * @return {@link Map }<{@link String }, {@link Object }>
     * @since 2022-08-10 10:26:07
     */
    Map<String, Object> getRegion(String cityCode, String townName);
}
