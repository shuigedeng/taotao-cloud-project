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

package com.taotao.cloud.message.biz.channels.websockt.stomp.service;

import com.taotao.cloud.data.jpa.base.repository.BaseRepository;
import com.taotao.cloud.message.biz.channels.websockt.stomp.repository.AnnouncementRepository;
import com.taotao.cloud.message.biz.mailing.entity.Announcement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/** SystemAnnouncementService */
@Service
public class AnnouncementService extends BaseLayeredService<Announcement, String> {

    private static final Logger log = LoggerFactory.getLogger(AnnouncementService.class);

    private final AnnouncementRepository announcementRepository;

    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    @Override
    public BaseRepository<Announcement, String> getRepository() {
        return announcementRepository;
    }

    public List<Announcement> pullAnnouncements(Date stamp) {
        List<Announcement> announcements = announcementRepository.findAllByCreateTimeAfter(stamp);
        log.debug("[Websocket] |- Announcement Service pullAnnouncements.");
        return announcements;
    }
}
