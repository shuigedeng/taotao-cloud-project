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

package com.taotao.cloud.member.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.member.infrastructure.persistent.po.MemberReceipt;

/** 会员发票业务层 */
public interface IMemberReceiptService extends IService<MemberReceipt> {

    /**
     * 查询会员发票列表
     *
     * @param memberReceiptPageQuery 会员发票信息
     * @return 会员发票分页
     */
    IPage<MemberReceipt> getPage(MemberReceiptPageQuery memberReceiptPageQuery);

    /**
     * 添加会员发票信息
     *
     * @param memberReceiptAddVO 会员发票信息
     * @param memberId 会员ID
     * @return 操作状态
     */
    Boolean addMemberReceipt(MemberReceiptAddVO memberReceiptAddVO, Long memberId);

    /**
     * 修改会员发票信息
     *
     * @param memberReceiptAddVO 会员发票信息
     * @param memberId 会员ID
     * @return 操作状态
     */
    Boolean editMemberReceipt(MemberReceiptAddVO memberReceiptAddVO, Long memberId);

    /**
     * 删除会员发票信息
     *
     * @param id 发票ID
     * @return 操作状态
     */
    Boolean deleteMemberReceipt(Long id);
}
