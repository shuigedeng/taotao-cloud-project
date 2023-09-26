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

package com.taotao.cloud.message.biz.channels.websockt.stomp.entity;

import cn.herodotus.engine.message.core.constants.MessageConstants;
import cn.herodotus.engine.supplier.message.domain.BaseSenderEntity;
import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.UuidGenerator;

/**
 * <p>Description: 私信联系 </p>
 * <p>
 * 私信双相关系存储。
 *
 * @author : gengwei.zheng
 * @date : 2022/12/7 11:03
 */
@Schema(name = "私信联系")
@Entity
@Table(name = "msg_dialogue_contact", indexes = {
        @Index(name = "msg_dialogue_contact_id_idx", columnList = "contact_id"),
        @Index(name = "msg_dialogue_contact_sid_idx", columnList = "sender_id"),
})
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = MessageConstants.REGION_MESSAGE_DIALOGUE_CONTACT)
public class DialogueContact extends BaseSenderEntity {

    @Schema(name = "联系ID")
    @Id
    @UuidGenerator
    @Column(name = "contact_id", length = 64)
    private String contactId;

    @Schema(name = "接收人ID")
    @Column(name = "receiver_id", length = 64)
    private String receiverId;

    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = MessageConstants.REGION_MESSAGE_DIALOGUE)
    @Schema(title = "对话ID")
    @ManyToOne
    @JoinColumn(name = "dialogue_id", nullable = false)
    private Dialogue dialogue;

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Dialogue getDialogue() {
        return dialogue;
    }

    public void setDialogue(Dialogue dialogue) {
        this.dialogue = dialogue;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("contactId", contactId)
                .add("receiverId", receiverId)
                .add("dialogue", dialogue)
                .toString();
    }
}
