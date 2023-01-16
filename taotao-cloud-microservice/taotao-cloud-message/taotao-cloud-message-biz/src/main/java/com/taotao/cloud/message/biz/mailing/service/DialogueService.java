package com.taotao.cloud.message.biz.mailing.service;

import com.taotao.cloud.data.jpa.base.repository.BaseRepository;
import com.taotao.cloud.message.biz.mailing.entity.Dialogue;
import com.taotao.cloud.message.biz.mailing.repository.DialogueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *  PersonalDialogueService
 */
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
