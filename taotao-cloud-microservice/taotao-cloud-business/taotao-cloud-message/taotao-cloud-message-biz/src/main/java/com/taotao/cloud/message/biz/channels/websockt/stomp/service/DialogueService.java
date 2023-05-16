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
import com.taotao.cloud.message.biz.channels.websockt.stomp.repository.DialogueRepository;
import com.taotao.cloud.message.biz.mailing.entity.Dialogue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/** PersonalDialogueService */
@Service
public class DialogueService extends BaseLayeredService<Dialogue, String> {

    private static final Logger log = LoggerFactory.getLogger(DialogueService.class);

    private final DialogueRepository dialogueRepository;

    public DialogueService(DialogueRepository dialogueRepository) {
        this.dialogueRepository = dialogueRepository;
    }

    @Override
    public BaseRepository<Dialogue, String> getRepository() {
        return dialogueRepository;
    }

    public Dialogue createDialogue(String content) {
        Dialogue dialogue = new Dialogue();
        dialogue.setLatestNews(content);

        log.debug("[Websocket] |- Dialogue Service createDialogue.");
        return this.save(dialogue);
    }

    public Dialogue updateDialogue(String dialogueId, String content) {
        Dialogue dialogue = this.findById(dialogueId);
        dialogue.setLatestNews(content);
        log.debug("[Websocket] |- updateDialogue Service createDialog.");
        return this.save(dialogue);
    }
}
