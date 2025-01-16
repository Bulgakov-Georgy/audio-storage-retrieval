package coding.challenge.core.repository

import coding.challenge.core.model.Phrase
import org.springframework.data.repository.CrudRepository

/**
 * Represents repository for management of [Phrase] entity.
 */
interface PhraseRepository : CrudRepository<Phrase, Long>
