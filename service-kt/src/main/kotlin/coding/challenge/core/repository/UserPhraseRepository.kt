package coding.challenge.core.repository

import coding.challenge.core.model.UserPhrase
import org.springframework.data.repository.CrudRepository

/**
 * Represents repository for management of [UserPhrase] entity.
 */
interface UserPhraseRepository : CrudRepository<UserPhrase, UserPhrase.UserPhraseId>
