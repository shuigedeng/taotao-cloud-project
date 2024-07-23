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

package com.taotao.cloud.order.infrastructure.persistent.repository.inf;

import com.taotao.cloud.order.infrastructure.persistent.po.order.OrderInfoPO;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/10/22 12:46
 */
@Repository
public interface IOrderInfoRepository extends JpaRepository<OrderInfoPO, Long> {

    @Query(value = """
			select u from OrderInfoPO u where u.id = ?#{[0]}
		""")
    List<OrderInfoPO> findOrderInfoById(Long id);

    OrderInfoPO findByCode(String code);

    // @Query(value = """
    //        select new com.taotao.cloud.order.api.OrderDO(
    //			u.memberId,
    //			u.code,
    //			u.mainStatus,
    //			u.childStatus,
    //			u.amount,
    //			u.receiverName
    //		)
    //		from OrderInfo u
    //		where u.code = :code
    //	""")
    // List<OrderDO> findOrderInfoByBo(@Param("code") String code);
}
