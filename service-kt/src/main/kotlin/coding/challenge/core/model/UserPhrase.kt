package coding.challenge.core.model

import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity

/**
 * Entity that represents User-Phrase many-to-many relationship.
 */
@Entity
data class UserPhrase(
    /**
     * UserPhrase identifier.
     */
    @EmbeddedId
    var id: UserPhraseId? = null,

    /**
     * Path to the phrase file in a storage system.
     */
    var phraseFilePath: String? = null,
) {

    /**
     * Composite identifier for [UserPhrase].
     */
    @Embeddable
    data class UserPhraseId(
        /**
         * User identifier.
         */
        var userId: Long? = null,

        /**
         * Phrase identifier.
         */
        var phraseId: Long? = null,
    )

}
