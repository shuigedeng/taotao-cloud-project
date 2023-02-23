
package com.taotao.cloud.message.biz.mailing.repository;

import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *  PersonalContactRepository
 */
public interface DialogueContactRepository extends BaseRepository<DialogueContact, String> {

	@Transactional(rollbackFor = TransactionalRollbackException.class)
	@Modifying
	@Query("delete from DialogueContact c where c.dialogue.dialogueId = :id")
	void deleteAllByDialogueId(@Param("id") String dialogueId);

	@QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
	Optional<DialogueContact> findBySenderIdAndReceiverId(String senderId, String receiverId);
}
