
package com.taotao.cloud.message.biz.mailing.repository;


/**
 *  PersonalDialogueDetailRepository
 */
public interface DialogueDetailRepository extends BaseRepository<DialogueDetail, String> {

	void deleteAllByDialogueId(String dialogueId);
}
