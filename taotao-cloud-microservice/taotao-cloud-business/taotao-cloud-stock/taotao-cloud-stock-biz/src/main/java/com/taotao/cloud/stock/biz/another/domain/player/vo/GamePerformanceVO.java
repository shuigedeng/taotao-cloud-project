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

package com.taotao.cloud.stock.biz.another.domain.player.vo;

import com.taotao.cloud.ddd.biz.util.validate.BizValidator;
import com.taotao.cloud.stock.biz.another.client.base.error.BizException;

// 比赛表现值对象
public class GamePerformanceVO implements BizValidator {

    // 跑动距离
    private Double runDistance;
    // 传球成功率
    private Double passSuccess;
    // 进球数
    private Integer scoreNum;

    @Override
    public void validate() {
        if (null == runDistance) {
            throw new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT);
        }
        if (null == passSuccess) {
            throw new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT);
        }
        if (Double.compare(passSuccess, 100) > 0) {
            throw new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT);
        }
        if (null == runDistance) {
            throw new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT);
        }
        if (null == scoreNum) {
            throw new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT);
        }
    }
}
