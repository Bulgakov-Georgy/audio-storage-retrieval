package coding.challenge.core.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

/**
 * Phrase entity.
 */
@Entity
data class Phrase(
    /**
     * Phrase identifier.
     */
    @Id
    @GeneratedValue
    val id: Long? = null,
)
