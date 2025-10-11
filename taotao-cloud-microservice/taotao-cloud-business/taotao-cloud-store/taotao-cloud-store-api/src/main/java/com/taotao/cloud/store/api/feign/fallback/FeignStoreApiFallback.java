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

package com.taotao.cloud.store.api.feign.fallback;

import com.taotao.boot.common.model.result.PageResult;
import com.taotao.cloud.store.api.feign.IFeignStoreApi;
import com.taotao.cloud.store.api.model.dto.CollectionDTO;
import com.taotao.cloud.store.api.model.dto.StoreBankDTO;
import com.taotao.cloud.store.api.model.dto.StoreCompanyDTO;
import com.taotao.cloud.store.api.model.dto.StoreOtherInfoDTO;
import com.taotao.cloud.store.api.model.query.StorePageQuery;
import com.taotao.cloud.store.api.model.vo.StoreVO;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class FeignStoreApiFallback implements FallbackFactory<IFeignStoreApi> {

    @Override
    public IFeignStoreApi create(Throwable throwable) {
        return new IFeignStoreApi() {

            @Override
            public StoreVO findSotreById(Long id) {
                return null;
            }

            @Override
            public Boolean updateStoreCollectionNum(CollectionDTO collectionDTO) {
                return null;
            }

            @Override
            public StoreVO getStoreDetail() {
                return null;
            }

            @Override
            public StoreVO findSotreByMemberId(Long memberId) {
                return null;
            }

            @Override
            public PageResult<StoreVO> findByConditionPage(StorePageQuery storePageQuery) {
                return null;
            }

            @Override
            public boolean applyFirstStep(StoreCompanyDTO storeCompanyDTO) {
                return false;
            }

            @Override
            public boolean applySecondStep(StoreBankDTO storeBankDTO) {
                return false;
            }

            @Override
            public boolean applyThirdStep(StoreOtherInfoDTO storeOtherInfoDTO) {
                return false;
            }
        };
    }
}
