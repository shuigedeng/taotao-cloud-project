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

package com.taotao.cloud.message.biz.mailing.service;

import com.taotao.cloud.data.jpa.base.repository.BaseRepository;
import com.taotao.cloud.message.biz.mailing.entity.PullStamp;
import com.taotao.cloud.message.biz.mailing.repository.PullStampRepository;
import java.util.Date;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

/** MessagePullStampService */
@Service
public class PullStampService extends BaseLayeredService<PullStamp, String> {

    private final PullStampRepository pullStampRepository;

    public PullStampService(PullStampRepository pullStampRepository) {
        this.pullStampRepository = pullStampRepository;
    }

    @Override
    public BaseRepository<PullStamp, String> getRepository() {
        return pullStampRepository;
    }

    public PullStamp findByUserId(String userId) {
        return pullStampRepository.findByUserId(userId).orElse(null);
    }

    public PullStamp getPullStamp(String userId) {

        PullStamp stamp = findByUserId(userId);
        if (ObjectUtils.isEmpty(stamp)) {
            stamp = new PullStamp();
            stamp.setUserId(userId);
        }
        stamp.setLatestPullTime(new Date());

        return this.save(stamp);
    }
}
