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

package com.taotao.cloud.order.biz.service.business.aftersale;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.order.sys.model.page.aftersale.AfterSaleReasonPageQuery;
import com.taotao.cloud.order.biz.model.entity.aftersale.AfterSaleReason;
import java.util.List;

/**
 * 售后原因业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:49:03
 */
public interface AfterSaleReasonService extends IService<AfterSaleReason> {

    /**
     * 获取售后原因列表
     *
     * @param serviceType 售后类型
     * @return {@link List }<{@link AfterSaleReason }>
     * @since 2022-04-28 08:49:03
     */
    List<AfterSaleReason> afterSaleReasonList(String serviceType);

    /**
     * 修改售后原因
     *
     * @param afterSaleReason 售后原因
     * @return {@link Boolean }
     * @since 2022-04-28 08:49:03
     */
    Boolean editAfterSaleReason(AfterSaleReason afterSaleReason);

    /**
     * 分页查询售后原因
     *
     * @param afterSaleReasonPageQuery 查询条件
     * @return {@link IPage }<{@link AfterSaleReason }>
     * @since 2022-04-28 08:49:03
     */
    IPage<AfterSaleReason> pageQuery(AfterSaleReasonPageQuery afterSaleReasonPageQuery);
}
