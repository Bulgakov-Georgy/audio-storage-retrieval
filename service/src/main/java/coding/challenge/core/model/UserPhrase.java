package coding.challenge.core.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity that represents User-Phrase many-to-many relationship.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPhrase {

    /**
     * UserPhrase identifier.
     */
    @EmbeddedId
    private UserPhraseId id;

    /**
     * Path to the phrase file in a storage system.
     */
    private String phraseFilePath;

    /**
     * Composite identifier for {@link UserPhrase}.
     */
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserPhraseId {

        /**
         * User identifier.
         */
        private long userId;

        /**
         * Phrase identifier.
         */
        private long phraseId;

    }

}
