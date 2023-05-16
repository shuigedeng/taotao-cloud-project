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
import com.taotao.cloud.message.biz.channels.websockt.stomp.repository.DialogueDetailRepository;
import com.taotao.cloud.message.biz.mailing.entity.Dialogue;
import com.taotao.cloud.message.biz.mailing.entity.DialogueContact;
import com.taotao.cloud.message.biz.mailing.entity.DialogueDetail;
import com.taotao.cloud.message.biz.mailing.entity.Notification;
import com.taotao.cloud.websocket.stomp.core.NotificationCategory;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/** PersonalDialogueDetailService */
@Service
public class DialogueDetailService extends BaseLayeredService<DialogueDetail, String> {

    private static final Logger log = LoggerFactory.getLogger(DialogueDetailService.class);

    private final DialogueDetailRepository dialogueDetailRepository;
    private final DialogueContactService dialogueContactService;
    private final DialogueService dialogueService;
    private final NotificationService notificationService;

    public DialogueDetailService(
            DialogueDetailRepository dialogueDetailRepository,
            DialogueContactService dialogueContactService,
            DialogueService dialogueService,
            NotificationService notificationService) {
        this.dialogueDetailRepository = dialogueDetailRepository;
        this.dialogueContactService = dialogueContactService;
        this.dialogueService = dialogueService;
        this.notificationService = notificationService;
    }

    @Override
    public BaseRepository<DialogueDetail, String> getRepository() {
        return dialogueDetailRepository;
    }

    private Notification convertDialogueDetailToNotification(DialogueDetail dialogueDetail) {
        Notification notification = new Notification();
        notification.setUserId(dialogueDetail.getReceiverId());
        notification.setContent(dialogueDetail.getContent());
        notification.setSenderId(dialogueDetail.getSenderId());
        notification.setSenderName(dialogueDetail.getSenderName());
        notification.setSenderAvatar(dialogueDetail.getSenderAvatar());
        notification.setCategory(NotificationCategory.DIALOGUE);
        return notification;
    }

    /**
     * 借鉴 Gitee 的私信设计 1. 每个人都可以查看与自己有过私信往来的用户列表。自己可以查看与自己有过联系的人，对方也可以查看与自己有过联系的人 2.
     * 私信往来用户列表中，显示最新一条对话的内容 3. 点开某一个用户，可以查看具体的对话详情。自己和私信对话用户看到的内容一致。
     *
     * <p>PersonalContact 存储私信双方的关系，存储两条。以及和对话的关联 PersonalDialogue 是一个桥梁连接 PersonalContact 和
     * PersonalDialogueDetail，同时存储一份最新对话副本
     *
     * <p>本处的逻辑： 发送私信时，首先要判断是否已经创建了 Dialogue 1. 如果没有创建 Dialogue，就是私信双方第一对话，那么要先创建
     * Dialogue，同时要建立私信双方的联系 Contact。保存的私信与将生成好的 DialogueId进行关联。 2. 如果已经有Dialogue，那么就直接保存私信对话，同时更新
     * Dialogue 中的最新信息。
     *
     * @param domain 数据对应实体
     * @return
     */
    @Transactional
    @Override
    public DialogueDetail save(DialogueDetail domain) {

        if (StringUtils.isBlank(domain.getDialogueId())) {
            DialogueContact dialogueContact =
                    dialogueContactService.findBySenderIdAndReceiverId(domain.getSenderId(), domain.getReceiverId());
            if (ObjectUtils.isNotEmpty(dialogueContact) && ObjectUtils.isNotEmpty(dialogueContact.getDialogue())) {
                String dialogueId = dialogueContact.getDialogue().getDialogueId();
                domain.setDialogueId(dialogueId);
                dialogueService.updateDialogue(dialogueId, domain.getContent());
            } else {
                Dialogue dialogue = dialogueService.createDialogue(domain.getContent());
                domain.setDialogueId(dialogue.getDialogueId());
                dialogueContactService.createContact(dialogue, domain);
            }
        } else {
            dialogueService.updateDialogue(domain.getDialogueId(), domain.getContent());
        }

        notificationService.save(convertDialogueDetailToNotification(domain));

        return super.save(domain);
    }

    @Transactional
    public void deleteDialogueById(String dialogueId) {
        dialogueContactService.deleteByDialogueId(dialogueId);
        dialogueService.deleteById(dialogueId);
        dialogueDetailRepository.deleteAllByDialogueId(dialogueId);
        log.debug("[Websocket] |- DialogueDetail Service deleteAllByDialogueId.");
    }

    public Page<DialogueDetail> findByCondition(int pageNumber, int pageSize, String dialogueId) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Specification<DialogueDetail> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("dialogueId"), dialogueId));

            Predicate[] predicateArray = new Predicate[predicates.size()];
            criteriaQuery.where(criteriaBuilder.and(predicates.toArray(predicateArray)));
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createTime")));
            return criteriaQuery.getRestriction();
        };

        log.debug("[Websocket] |- DialogueDetail Service findByCondition.");
        return this.findByPage(specification, pageable);
    }
}
