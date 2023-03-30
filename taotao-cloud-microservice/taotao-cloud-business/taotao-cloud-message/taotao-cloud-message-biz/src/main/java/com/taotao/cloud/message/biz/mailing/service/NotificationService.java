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
import com.taotao.cloud.message.biz.mailing.entity.Announcement;
import com.taotao.cloud.message.biz.mailing.entity.Notification;
import com.taotao.cloud.message.biz.mailing.entity.PullStamp;
import com.taotao.cloud.message.biz.mailing.repository.NotificationRepository;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/** NotificationQueueService */
@Service
public class NotificationService extends BaseLayeredService<Notification, String> {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final PullStampService pullStampService;
    private final AnnouncementService announcementService;

    public NotificationService(
            NotificationRepository notificationRepository,
            PullStampService pullStampService,
            AnnouncementService announcementService) {
        this.notificationRepository = notificationRepository;
        this.pullStampService = pullStampService;
        this.announcementService = announcementService;
    }

    @Override
    public BaseRepository<Notification, String> getRepository() {
        return notificationRepository;
    }

    public void pullAnnouncements(String userId) {
        PullStamp pullStamp = pullStampService.getPullStamp(userId);
        List<Announcement> systemAnnouncements =
                announcementService.pullAnnouncements(pullStamp.getLatestPullTime());
        if (CollectionUtils.isNotEmpty(systemAnnouncements)) {
            List<Notification> notificationQueues =
                    convertAnnouncementsToNotifications(userId, systemAnnouncements);
            this.saveAll(notificationQueues);
        }
    }

    public Page<Notification> findByCondition(
            int pageNumber,
            int pageSize,
            String userId,
            NotificationCategory category,
            Boolean read) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Specification<Notification> specification =
                (root, criteriaQuery, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    predicates.add(criteriaBuilder.equal(root.get("userId"), userId));

                    if (ObjectUtils.isNotEmpty(category)) {
                        predicates.add(criteriaBuilder.equal(root.get("category"), category));
                    }

                    if (ObjectUtils.isNotEmpty(read)) {
                        predicates.add(criteriaBuilder.equal(root.get("read"), read));
                    }

                    Predicate[] predicateArray = new Predicate[predicates.size()];
                    criteriaQuery.where(criteriaBuilder.and(predicates.toArray(predicateArray)));
                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createTime")));
                    return criteriaQuery.getRestriction();
                };

        log.debug("[Websocket] |- Notification Service findByCondition.");
        return this.findByPage(specification, pageable);
    }

    private List<Notification> convertAnnouncementsToNotifications(
            String userId, List<Announcement> announcements) {
        return announcements.stream()
                .map(announcement -> convertAnnouncementToNotification(userId, announcement))
                .collect(Collectors.toList());
    }

    private Notification convertAnnouncementToNotification(
            String userId, Announcement announcement) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setContent(announcement.getContent());
        notification.setSenderId(announcement.getSenderId());
        notification.setSenderName(announcement.getSenderName());
        notification.setSenderAvatar(announcement.getSenderAvatar());
        notification.setCategory(NotificationCategory.ANNOUNCEMENT);
        return notification;
    }

    public int setAllRead(String userId) {
        int result = notificationRepository.updateAllRead(userId);
        log.debug("[Websocket] |- Notification Service setAllRead.");
        return result;
    }
}
