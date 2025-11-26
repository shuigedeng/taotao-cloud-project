/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.message.biz.channels.websockt.stomp.service;

import cn.herodotus.engine.data.core.repository.BaseRepository;
import cn.herodotus.engine.data.core.service.BaseService;
import cn.herodotus.engine.message.core.enums.NotificationCategory;
import cn.herodotus.engine.supplier.message.entity.Dialogue;
import cn.herodotus.engine.supplier.message.entity.DialogueContact;
import cn.herodotus.engine.supplier.message.entity.DialogueDetail;
import cn.herodotus.engine.supplier.message.entity.Notification;
import cn.herodotus.engine.supplier.message.repository.DialogueDetailRepository;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.ObjectUtils;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: PersonalDialogueDetailService </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/12/7 22:10
 */
@Service
public class DialogueDetailService extends BaseService<DialogueDetail, String> {

    private final DialogueDetailRepository dialogueDetailRepository;
    private final DialogueContactService dialogueContactService;
    private final DialogueService dialogueService;
    private final NotificationService notificationService;

    public DialogueDetailService(DialogueDetailRepository dialogueDetailRepository, DialogueContactService dialogueContactService, DialogueService dialogueService, NotificationService notificationService) {
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
     * 借鉴 Gitee 的私信设计
     * 1. 每个人都可以查看与自己有过私信往来的用户列表。自己可以查看与自己有过联系的人，对方也可以查看与自己有过联系的人
     * 2. 私信往来用户列表中，显示最新一条对话的内容
     * 3. 点开某一个用户，可以查看具体的对话详情。自己和私信对话用户看到的内容一致。
     * <p>
     * PersonalContact 存储私信双方的关系，存储两条。以及和对话的关联
     * PersonalDialogue 是一个桥梁连接 PersonalContact 和 PersonalDialogueDetail，同时存储一份最新对话副本
     * <p>
     * 本处的逻辑：
     * 发送私信时，首先要判断是否已经创建了 Dialogue
     * 1. 如果没有创建 Dialogue，就是私信双方第一对话，那么要先创建 Dialogue，同时要建立私信双方的联系 Contact。保存的私信与将生成好的 DialogueId进行关联。
     * 2. 如果已经有Dialogue，那么就直接保存私信对话，同时更新 Dialogue 中的最新信息。
     *
     * @param domain 数据对应实体
     * @return {@link DialogueDetail}
     */
    @Transactional
    @Override
    public DialogueDetail save(DialogueDetail domain) {

        if (StringUtils.isBlank(domain.getDialogueId())) {
            DialogueContact dialogueContact = dialogueContactService.findBySenderIdAndReceiverId(domain.getSenderId(), domain.getReceiverId());
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

        return this.findByPage(specification, pageable);
    }
}
