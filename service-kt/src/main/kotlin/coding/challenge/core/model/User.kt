package coding.challenge.core.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

/**
 * User entity.
 */
@Entity
data class User(
    /**
     * User identifier.
     */
    @Id
    @GeneratedValue
    val id: Long? = null,
)