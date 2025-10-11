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

package com.taotao.cloud.member.biz.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.cloud.goods.api.model.vo.EsGoodsIndexVO;
import com.taotao.cloud.member.biz.model.entity.MemberBrowse;
import java.util.List;

/**
 * 会员浏览历史业务层
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:53:20
 */
public interface IMemberBrowseService extends IService<MemberBrowse> {

    /**
     * 保存浏览历史
     *
     * @param footPrint 用户足迹
     * @return 浏览历史
     */
    MemberBrowse saveFootprint(MemberBrowse footPrint);

    /**
     * 清空当前会员的足迹
     *
     * @return 处理结果
     */
    Boolean clean();

    /**
     * 根据ID进行清除会员的历史足迹
     *
     * @param ids 商品ID列表
     * @return 处理结果
     */
    Boolean deleteByIds(List<Long> ids);

    /**
     * 获取会员浏览历史分页
     *
     * @param PageQuery 分页
     * @return 会员浏览历史列表
     */
    List<EsGoodsIndexVO> footPrintPage(PageQuery PageQuery);

    /**
     * 获取当前会员的浏览记录数量
     *
     * @return 当前会员的浏览记录数量
     */
    Long getFootprintNum();
}
