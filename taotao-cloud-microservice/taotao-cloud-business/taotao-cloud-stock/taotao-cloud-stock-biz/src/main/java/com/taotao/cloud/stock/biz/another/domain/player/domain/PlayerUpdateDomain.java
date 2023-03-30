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

package com.taotao.cloud.stock.biz.another.domain.player.domain;

import com.taotao.cloud.ddd.biz.domain.base.domain.BaseDomain;
import com.taotao.cloud.ddd.biz.domain.base.vo.MaintainUpdateVO;
import com.taotao.cloud.ddd.biz.domain.player.vo.GamePerformanceVO;
import com.taotao.cloud.ddd.biz.util.validate.BizValidator;
import com.taotao.cloud.stock.biz.another.client.base.error.BizException;

// 修改领域对象
public class PlayerUpdateDomain extends BaseDomain implements BizValidator {

    private String playerId;
    private String playerName;
    private Integer height;
    private Integer weight;
    private String updator;
    private Date updatetime;
    private GamePerformanceVO gamePerformance;
    private MaintainUpdateVO maintainInfo;

    @Override
    public void validate() {
        if (StringUtils.isEmpty(playerId)) {
            throw new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT);
        }
        if (StringUtils.isEmpty(playerName)) {
            throw new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT);
        }
        if (null == height) {
            throw new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT);
        }
        if (height > 300) {
            throw new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT);
        }
        if (null == weight) {
            throw new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT);
        }
        if (null != gamePerformance) {
            gamePerformance.validate();
        }
        if (null == maintainInfo) {
            throw new BizException(ErrorCodeBizEnum.ILLEGAL_ARGUMENT);
        }
        maintainInfo.validate();
    }
}
